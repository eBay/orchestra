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

package com.ebay.riskmgmt.workfloworchestrator.task;
/*
 * Created by pishrivastava on 2019-04-17 - 17:10
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;

import java.util.logging.Logger;

public class TaskExecutionUtil {

	private static final Logger logger = Logger.getLogger(TaskExecutionUtil.class.getName());

	/**
	 * @param task            The task to be executed
	 * @param workflowContext WorkflowContext
	 * @return Task Report which has the success/failure status.
	 */
	public static TaskReport processTask(Task task, WorkflowContext workflowContext) throws WorkflowException {

		TaskReport taskReport = new DefaultTaskReport(TaskStatus.COMPLETED);
		checkForPreExecution(task, workflowContext);
		taskReport = task.execute(workflowContext);
		checkForPostExecution(task, workflowContext);
		return taskReport;
	}

	private static void checkForPostExecution(Task task, WorkflowContext workflowContext) {
		if (task instanceof AbstractTask) {
			((AbstractTask) task).postExecution(workflowContext);
		}
	}

	private static void checkForPreExecution(Task task, WorkflowContext workflowContext) {
		if (task instanceof AbstractTask) {
			((AbstractTask) task).preExecution(workflowContext);
		}
	}

}
