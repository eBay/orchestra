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
 * Created by pishrivastava on 2019-04-08 - 14:49
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;
import jdk.jfr.internal.LogLevel;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;


public class ParallelTaskExecutor {

	private static final Logger LOGGER = Logger.getLogger(ParallelTaskExecutor.class.getName());
	private static final String PARALLEL_TASK_EXECUTOR_POOL = "ParallelTaskExecutorPool";
	private static final String PARALLEL_TASK_EXECUTOR = "Parallel Task Executor";

	private volatile static ParallelTaskExecutor instance = null;
	private int coreSize = 50;
	private int maxSize = 100;
	private TaskExecutor taskExecutor;


	private ParallelTaskExecutor() {
		//this.taskExecutor = create(PARALLEL_TASK_EXECUTOR_POOL, PARALLEL_TASK_EXECUTOR, coreSize, maxSize);
	}

	private ParallelTaskExecutor(int coreSize, int maxSize) {
		//this.taskExecutor = create(PARALLEL_TASK_EXECUTOR_POOL, PARALLEL_TASK_EXECUTOR, coreSize, maxSize);
	}

	public static ParallelTaskExecutor getInstance() {
		if (instance == null) {
			synchronized (ParallelTaskExecutor.class) {
				instance = new ParallelTaskExecutor();
			}
		}
		return instance;
	}

	public static void init(int coreSize, int maxSize) {
		if (instance == null) {
			synchronized (ParallelTaskExecutor.class) {
				instance = new ParallelTaskExecutor(coreSize, maxSize);
			}
		}
	}




	private <V> List<Future<V>> executeAll(List<? extends ExecutorTask<V>> taskList, long timeout) {
		List<Future<V>> futures = null;
		try {

			ExecutorService executorService = Executors.newCachedThreadPool();
			futures = executorService.invokeAll(taskList, timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			//LOGGER.log(Level.ERROR, "Parallel Executor's executeAll method got interrupted. There may be few task already completed but not possible to track", e);
		}
		return futures;
	}


		public List<TaskReport> executeInParallel(WorkflowContext workflowContext, List<Task> tasks, long timeout) {

		List<TaskReport> workReports = new ArrayList<>();
		List<ExecutorTask<TaskReport>> parallelTasks = buildExecutorTasksNew(tasks, workflowContext);
		try {

			List<Future<TaskReport>> loggingFutures = executeAll(parallelTasks, timeout);

			if (loggingFutures != null && !loggingFutures.isEmpty()) {
				for (Future future : loggingFutures) {
					try {
						if (future.isCancelled()) {
						} else {
							TaskReport taskReport = (TaskReport) future.get();
							workReports.add(taskReport);
						}
					} catch (Exception ex) {
//						LOGGER.log(LogLevel.WARN, "Unable to get work report of task " + future.getTask().getName(), ex);
					}

				}
			}

		} catch (RuntimeException ex) {
			//LOGGER.log(LogLevel.WARN, "Parallel task execution failed ", ex);

		}

		return workReports;
	}



	private List<ExecutorTask<TaskReport>> buildExecutorTasksNew(List<Task> tasks, final WorkflowContext workflowContext) {
		List<ExecutorTask<TaskReport>> parallelTasks = new ArrayList<>();
		for (final Task task : tasks) {
			ExecutorTask<TaskReport> executorTask = new ExecutorTask<TaskReport>() {
				@Override
				public TaskReport call() throws Exception {
					return TaskExecutionUtil.processTask(task, workflowContext);
				}
			};
			parallelTasks.add(executorTask);
		}
		return parallelTasks;
	}
}
