/*
 * Copyright 2024 eBay Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ebay.riskmgmt.workfloworchestrator.context.template;
/*
 * Created by pishrivastava on 3/1/19 - 2:52 PM
 */

import com.ebay.riskmgmt.workfloworchestrator.context.ExecutableStore;
import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.context.metadata.ArgumentMetadata;
import com.ebay.riskmgmt.workfloworchestrator.context.metadata.TaskMetadata;
import com.ebay.riskmgmt.workfloworchestrator.context.metadata.wf.*;
import com.ebay.riskmgmt.workfloworchestrator.service.LoggingUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskInstanceGenerator;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReportPredicate;
import com.ebay.riskmgmt.workfloworchestrator.workflow.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowTemplateParser {

	private static final Logger logger = Logger.getLogger(WorkflowTemplateParser.class.getName());
	private static final String CLASSPATH = "classpath:";

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * @param workflowContext request level context which gets passed along every task.
	 * @return boolean value for parse success.
	 * @throws ParserException throws exception on invalid input stream.
	 */
	public Workflow parse(WorkflowContext workflowContext) throws ParserException {

		try {
			// Load the template from the YAML file
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource(CLASSPATH + workflowContext.getContextFilePath() + File.separator + workflowContext.getContextFile());
			InputStream contextFileInputStream = resource.getInputStream();
			ExecutableStore executableStore = new ExecutableStore();
			WorkflowTemplate template = loadWorkflowTemplateFromYamlFile(contextFileInputStream);

			// Set the context properties
			setWorkflowProperties(template, workflowContext);

			constructGraph(workflowContext, template, executableStore);

			// Start the engine
			return getTheEngine(template, executableStore);

		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Could not parse the file.", exception);
			throw new ParserException(exception.getMessage(), exception);
		}
	}

	/**
	 * This is a recursive method called for loading all tasks and work flows mentioned in the config file.
	 * @param workflowContext workflow context of the current request.
	 * @param currTemplate the template being processed currently.
	 * @param executableStore single store which contains all tasks and work flows across all templates.
	 * @throws Exception throws exception if any of the config file is not found.
	 */
	private void constructGraph(WorkflowContext workflowContext, WorkflowTemplate currTemplate, ExecutableStore executableStore) throws Exception {
		// Load other dependency YAML files
		WorkflowTemplate template = loadDependentYaml(workflowContext, currTemplate, executableStore);

		// Load all tasks
		loadAllTasks(template, executableStore);

		//Load the workFlows
		loadAllWorkFlows(workflowContext, template, executableStore);
	}

	private WorkflowTemplate loadDependentYaml(WorkflowContext workflowContext, WorkflowTemplate template, ExecutableStore executableStore) throws Exception {
		if (template.getImportFiles() != null) {
			for (String fileName : template.getImportFiles()) {
				if (StringUtils.isNotEmpty(fileName)) {
					fileName = fileName.trim();
					ResourceLoader resourceLoader = new DefaultResourceLoader();
					Resource resource = resourceLoader.getResource(CLASSPATH + workflowContext.getContextFilePath() + File.separator + fileName);
					InputStream contextFileInputStream = resource.getInputStream();
					WorkflowTemplate childTemplate = loadWorkflowTemplateFromYamlFile(contextFileInputStream);
					constructGraph(workflowContext, childTemplate, executableStore);
				}
			}
		}
		return template;
	}

	private void setWorkflowProperties(WorkflowTemplate template, WorkflowContext workflowContext) {
		workflowContext.setContextName(template.getName());
		workflowContext.setVersion(template.getVersion());
	}

	private WorkflowTemplate loadWorkflowTemplateFromYamlFile(InputStream inputStream) throws Exception {
		try {
			Yaml yaml = new Yaml();
			WorkflowTemplate wf = yaml.loadAs(inputStream, WorkflowTemplate.class);
			LoggingUtil.log(wf);
			return wf;
		} catch (Exception exception) {
			throw new Exception("Could not load the YAML file. " + inputStream.getClass());
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	private Workflow getTheEngine(WorkflowTemplate wf, ExecutableStore executableStore) throws Exception {
		final String engine = wf.getEngine();
		Workflow mainWorkFlow = executableStore.getWfMap().get(engine);
		if (mainWorkFlow != null) {
			return mainWorkFlow;
		} else {
			logger.log(Level.SEVERE, "Invalid engine name. Value should be a workflow. Current value is "+engine);
			throw new Exception("Engine not found");
		}
	}

	private Map<String, Workflow> loadAllWorkFlows(WorkflowContext workflowContext, WorkflowTemplate template, ExecutableStore executableStore) throws InstantiationException {
		Map<String, Workflow> wfMap = executableStore.getWfMap();
		Map<String, Task> taskMap = executableStore.getTaskMap();

		for (Map.Entry<String, WorkflowMetadata> wfEntry : template.getWorkflow().entrySet()) {
			String name = wfEntry.getKey();
			WorkflowMetadata workflowMetadata = wfEntry.getValue();

			final WorkflowBuilderType workflowMetadataType = workflowMetadata.getType();

			switch (workflowMetadataType) {
				case repeat:
					if (workflowMetadata.getRepeatWF() != null) {
						RepeatWorkFlow repeatWF = workflowMetadata.getRepeatWF();
						String executable = repeatWF.getExecutable();
						if (!validExecutable(executableStore, executable)) {
							logger.log(Level.SEVERE, "Invalid task - "+executable);
						} else {
							Workflow workflow = RepeatFlow.Builder.aNewRepeatFlow()
									.times(repeatWF.getTimes()).named(name)
									.executeAsync(workflowMetadata.isAsync())
									.repeat(taskMap.containsKey(executable) ?
											taskMap.get(executable) : wfMap.get(executable)).build();
							wfMap.put(name, workflow);
						}
					}
					break;

				case parallel:
					if (workflowMetadata.getParallelWF() != null) {
						ParallelWorkFlow parallelWF = workflowMetadata.getParallelWF();
						List<Task> taskList = new ArrayList<>();
						for (Map.Entry<String, ParallelTaskProperty> entry : parallelWF.getExecutables().entrySet()) {
							if (!validExecutable(executableStore, entry.getKey())) {
								logger.log(Level.SEVERE, "Task definition missing in the YAML config - "+entry.getKey());
							} else if (wfMap.containsKey(entry.getKey())) {
								taskList.add(wfMap.get(entry.getKey()));
							} else {

								// check for multiple instance based tasks
								ParallelTaskProperty taskProperty = entry.getValue();
								if (taskProperty != null
										&& StringUtils.isNotEmpty(taskProperty.getElementType())
										&& StringUtils.isNotEmpty(taskProperty.getInstanceGenerator())) {

									// Get the taskInstanceGenerator instance
									TaskInstanceGenerator taskInstanceGenerator = (TaskInstanceGenerator) applicationContext.getBean(taskProperty.getInstanceGenerator());
									final List list = taskInstanceGenerator.getCollection(workflowContext);

									if (list == null || list.isEmpty()) {
										throw new InstantiationException("TaskInstanceGenerator is not well implemented");
									}

									for (Object obj : list) {
										String taskName = entry.getKey();
										Task task = (Task) applicationContext.getBean(taskName, obj);
										taskMap.put(task + obj.toString(), task);
										taskList.add(task);
									}

								} else {
									taskList.add(taskMap.get(entry.getKey()));
								}
							}
						}
						ParallelFlow.Builder parallelFlow = ParallelFlow.Builder.aNewParallelFlow()
								.named(name)
								.executeAsync(workflowMetadata.isAsync())
								.execute(taskList);
						if (parallelWF.getTimeout() != null && parallelWF.getTimeout() > 0L) {
							parallelFlow.timeout(parallelWF.getTimeout());
						}
						Workflow workflow = parallelFlow.build();
						wfMap.put(name, workflow);
					}
					break;

				case conditional:
					if (workflowMetadata.getConditionalWF() != null) {
						ConditionalWorkFlow conditionalWF = workflowMetadata.getConditionalWF();
						String conditionalWFExecutable = conditionalWF.getExecutable();
						if (!validExecutable(executableStore, conditionalWFExecutable)) {
							logger.log(Level.SEVERE, "Invalid Executable "+conditionalWFExecutable);
							continue;
						}

						//String whenCondition = conditionalWF.getWhenCondition(); // TODO use task predicate
						String thenDoTaskName = conditionalWF.getThenExecutable();
						String otherwiseDoTaskName = conditionalWF.getOtherwiseExecutable();

						if (!validExecutable(executableStore, thenDoTaskName)
								|| !validExecutable(executableStore, otherwiseDoTaskName)) {
							logger.log(Level.SEVERE, "Invalid if-else task");
							continue;
						}

						Task executableTask = taskMap.containsKey(conditionalWFExecutable) ?
								taskMap.get(conditionalWFExecutable) : wfMap.get(conditionalWFExecutable);
						Task thenDoTask = taskMap.containsKey(thenDoTaskName) ?
								taskMap.get(thenDoTaskName) : wfMap.get(thenDoTaskName);
						Task otherwiseDoTask = taskMap.containsKey(otherwiseDoTaskName) ?
								taskMap.get(otherwiseDoTaskName) : wfMap.get(otherwiseDoTaskName);

						Workflow workflow = ConditionalFlow.Builder.aNewConditionalFlow()
								.named(name).executeAsync(workflowMetadata.isAsync()).execute(executableTask)
								.when(TaskReportPredicate.completed()).then(thenDoTask).otherwise(otherwiseDoTask).build();
						wfMap.put(name, workflow);
					}
					break;

				case sequential:
					if (workflowMetadata.getSequentialWF() != null) {
						SequentialWorkFlow sequentialWF = workflowMetadata.getSequentialWF();
						final List<String> workFlows = sequentialWF.getExecutables();
						List<Task> wfList = new ArrayList<>();
						for (String wfName : workFlows) {
							if (!validExecutable(executableStore, wfName)) {
								logger.log(Level.SEVERE, "Invalid task or workflow. Skipping it - "+wfName);
								continue;
							}

							if (wfMap.containsKey(wfName)) {
								wfList.add(wfMap.get(wfName));
							} else if (taskMap.containsKey(wfName)) {
								wfList.add(taskMap.get(wfName));
							}
						}
						Workflow workflow = SequentialFlow.Builder.aNewSequentialFlow().named(workflowMetadata.getDesc()).executeAsync(workflowMetadata.isAsync()).execute(wfList).build();
						wfMap.put(name, workflow);
					}
					break;

				case branch:
					if (workflowMetadata.getBranchWF() != null) {
						BranchWorkFlow branchFlow = workflowMetadata.getBranchWF();
						final String executable = branchFlow.getExecutable();
						final Map<String, String> switchCases = branchFlow.getSwitchCases();

						if (!validExecutable(executableStore, executable)) {
							logger.log(Level.SEVERE, "Invalid task or workflow. Skipping it - " + executable);
							break;
						}

						Task toExecuteTask = taskMap.containsKey(executable) ?
								taskMap.get(executable) : wfMap.get(executable);

						Map<String, Task> map = new HashMap<>();

						for (Map.Entry<String, String> entry : switchCases.entrySet()) {

							if (!validExecutable(executableStore, entry.getValue())) {
								logger.log(Level.SEVERE, "Invalid task or workflow in Switch case for branch workflow. Skipping it - " + executable);
								continue;
							}

							if (wfMap.containsKey(entry.getValue())) {
								map.put(entry.getKey(), wfMap.get(entry.getValue()));
							} else if (taskMap.containsKey(entry.getValue())) {
								map.put(entry.getKey(), taskMap.get(entry.getValue()));
							}
						}

						Workflow workflow = BranchFlow.Builder.aNewBranchFlow().named(workflowMetadata.getDesc()).executeAsync(workflowMetadata.isAsync()).toExecute(toExecuteTask).addCases(map).build();
						wfMap.put(name, workflow);
					}
					break;
			}

		}
		return wfMap;
	}

	private boolean validExecutable(ExecutableStore executableStore, String executable) {
		return executableStore.getTaskMap().containsKey(executable) || executableStore.getWfMap().containsKey(executable);
	}

	private Map<String, Task> loadAllTasks(WorkflowTemplate wf, ExecutableStore executableStore) throws InstantiationException {

		Map<String, Task> taskMap = executableStore.getTaskMap();
		for (Map.Entry<String, TaskMetadata> taskEntry : wf.getTask().entrySet()) {
			String name = taskEntry.getKey().trim();

			TaskMetadata taskMetadata = taskEntry.getValue();

			Task task;
			if (taskMetadata != null && taskMetadata.getArgs() != null) {
				List<ArgumentMetadata> arguments = taskMetadata.getArgs();
				final String dataType = arguments.get(0) != null ?
						arguments.get(0).getArgType().toLowerCase() : null;
				final Object dataValue = arguments.get(0) != null ?
						arguments.get(0).getValue() : null;

				task = instantiateTaskClass(name, dataType, dataValue);
			} else {
				task = (Task) applicationContext.getBean(name);
			}

			System.out.println(task.getName());
			taskMap.put(name, task);
		}
		return taskMap;
	}


	private Task instantiateTaskClass(String beanName, String dataType, Object dataValue) throws InstantiationException {
		Task task;
		if (dataType == null || dataValue == null) {
			throw new InstantiationException("Invalid or null argument");
		}

		switch (dataType.toLowerCase()) {
			case "long":
				Long longArg = Long.parseLong(dataValue.toString());
				task = (Task) applicationContext.getBean(beanName, longArg);
				break;

			case "double":
				Double doubleArg = Double.parseDouble(dataValue.toString());
				task = (Task) applicationContext.getBean(beanName, doubleArg);
				break;

			case "float":
				Float floatArg = Float.parseFloat(dataValue.toString());
				task = (Task) applicationContext.getBean(beanName, floatArg);
				break;

			case "integer":
				Integer intArg = Integer.parseInt(dataValue.toString());
				task = (Task) applicationContext.getBean(beanName, intArg);
				break;

			case "string":
				String strArg = (String) dataValue;
				task = (Task) applicationContext.getBean(beanName, strArg);
				break;

			default:
			case "object":
				task = (Task) applicationContext.getBean(beanName, dataValue);
				break;

		}
		return task;
	}
}
