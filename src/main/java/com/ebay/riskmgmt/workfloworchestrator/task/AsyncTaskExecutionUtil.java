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
 * Created by pishrivastava on 12/23/19 - 2:19 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTaskExecutionUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskExecutionUtil.class);

	/**
	 * @param task            The task to be executed
	 * @param workflowContext WorkflowContext
	 */
	public static void processTaskAsync(Task task, WorkflowContext workflowContext) {
		try {
			WorkflowRunnableTask runnableTask = new WorkflowRunnableTask(task, workflowContext);
			AsyncTaskExecutor.getTaskExecutor().submit(runnableTask);
		} catch (Exception ex) {
			LOGGER.error("Error processing task asynchronously", ex);
		}
	}
}
