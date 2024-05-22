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
 * Created by pishrivastava on 12/23/19 - 2:11 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowRunnableTask implements Runnable {

	private Task task;
	private WorkflowContext workflowContext;

	private static final Logger logger = Logger.getLogger(WorkflowRunnableTask.class.getName());

	public WorkflowRunnableTask(Task task, WorkflowContext workflowContext) {
		this.task = task;
		this.workflowContext = workflowContext;
	}

//	@Override
//	public String getName() {
//		return this.getClass().getSimpleName();
//	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		try {
			TaskExecutionUtil.processTask(task, workflowContext);

		} catch (WorkflowException e) {
			logger.log(Level.SEVERE, "Exception while executing the task in async way. ", e);
		}
	}
}
