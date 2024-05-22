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

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.service.SleepTask;
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TaskExecutionUtilTest {

	private SleepTask sleepTask = new SleepTask(1L);

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void processTask() throws WorkflowException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		final TaskReport taskReport = TaskExecutionUtil.processTask(sleepTask, context);
		Assert.assertTrue(taskReport.getStatus() == TaskStatus.COMPLETED);
	}

	@Test
	public void processTaskHeadTest() throws WorkflowException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		final TaskReport taskReport = TaskExecutionUtil.processTask(sleepTask, context);
		Assert.assertTrue(taskReport.getStatus() == TaskStatus.COMPLETED);
	}

	@Test
	public void processInsertHeadTest() throws WorkflowException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		Task task = new DummyTask();
		final TaskReport taskReport = TaskExecutionUtil.processTask(task, context);
		Assert.assertTrue(taskReport.getStatus() == TaskStatus.COMPLETED);
	}

	@Test
	public void nullLogRequestTest() throws WorkflowException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		final TaskReport taskReport = TaskExecutionUtil.processTask(new DummyTask(), context);
		Assert.assertTrue(taskReport.getStatus() == TaskStatus.COMPLETED);
	}




	class DummyTask implements Task {

		@Override
		public String getName() {
			return "DummyTask";
		}

		@Override
		public TaskReport execute(WorkflowContext workflowContext) {
			return new DefaultTaskReport(TaskStatus.COMPLETED);
		}

	}
}