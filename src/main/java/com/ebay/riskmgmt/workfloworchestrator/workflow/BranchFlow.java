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

package com.ebay.riskmgmt.workfloworchestrator.workflow;
/*
 * Created by pishrivastava on 9/20/19 - 3:14 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartComponent;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartVisitor;
import com.ebay.riskmgmt.workfloworchestrator.task.AsyncTaskExecutionUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.DefaultTaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.NoOpTask;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskExecutionUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BranchFlow extends AbstractWorkflow {

	private static final Logger LOGGER = Logger.getLogger(BranchFlow.class.getName());
	private Task toExecute;
	private Map<String, Task> taskCasesMap = new HashMap<>();


	BranchFlow(String name, Task toExecute, Map<String, Task> taskCasesMap, boolean async) {
		super(name);
		this.toExecute = toExecute;
		this.taskCasesMap.putAll(taskCasesMap);
		setAsyncExecution(async);
	}

	public Task getToExecute() {
		return toExecute;
	}

	public Map<String, Task> getTaskCasesMap() {
		return Collections.unmodifiableMap(taskCasesMap);
	}

	@Override
	public FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitBranchFlow(this);
	}

	@Override
	public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {

		if (isAsyncExecution()) {
			LOGGER.log(Level.INFO, "Async Execution. Executing task " + getName());
			setAsyncExecution(false);
			AsyncTaskExecutionUtil.processTaskAsync(this, workflowContext);
			return new DefaultTaskReport(TaskStatus.COMPLETED);
		} else {
			LOGGER.log(Level.INFO, "Executing task " + toExecute.getName());
			TaskReport jobReport = TaskExecutionUtil.processTask(toExecute, workflowContext);
			String taskOutcome = jobReport.getTaskOutcome();
			LOGGER.log(Level.INFO, "Task outcome is " + taskOutcome);

			if (taskCasesMap.get(taskOutcome) != null) {
				return TaskExecutionUtil.processTask(taskCasesMap.get(taskOutcome), workflowContext);
			} else {
				LOGGER.log(Level.SEVERE, "No such task/workflow defined: " + taskOutcome);
				return new DefaultTaskReport(TaskStatus.FAILED);
			}
		}
	}

	public static class Builder {

		private String name;
		private Task toExecute;
		private Map<String, Task> taskCasesMap = new HashMap<>();
		private boolean async;

		private Builder() {
			this.name = UUID.randomUUID().toString();
			this.toExecute = new NoOpTask();
		}

		public static Builder aNewBranchFlow() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder executeAsync(boolean async) {
			this.async = async;
			return this;
		}

		public Builder toExecute(Task task) {
			this.toExecute = task;
			return this;
		}

		public Builder addCases(Map<String, Task> taskCasesMap) {
			this.taskCasesMap = taskCasesMap;
			return this;
		}

		public BranchFlow build() {
			return new BranchFlow(name, toExecute, taskCasesMap, async);
		}
	}
}
