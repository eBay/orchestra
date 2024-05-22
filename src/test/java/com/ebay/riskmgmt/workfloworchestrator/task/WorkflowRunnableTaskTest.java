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
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TaskExecutionUtil.class)
public class WorkflowRunnableTaskTest {

	private WorkflowContext workflowContext;
	private Task task;

	@Before
	public void setUp() throws Exception {
		workflowContext = WorkflowContextBuilder.getWorkflowContext();
		task = new NoOpTask();
	}

	@Test
	public void getName() {
		WorkflowRunnableTask workflowRunnableTask = new WorkflowRunnableTask(task, workflowContext);
		final String name = workflowRunnableTask.getClass().getSimpleName();
		Assert.assertEquals(name, "WorkflowRunnableTask");
	}

	@Test
	public void runWithoutExceptionTest() throws WorkflowException {
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		when(TaskExecutionUtil.processTask(task, workflowContext))
				.thenReturn(new DefaultTaskReport(TaskStatus.COMPLETED));
		WorkflowRunnableTask workflowRunnableTask = new WorkflowRunnableTask(task, workflowContext);
		workflowRunnableTask.run();
		Assert.assertTrue(true);
	}

	@Test
	public void runWithExceptionTest() throws WorkflowException {
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		when(TaskExecutionUtil.processTask(task, workflowContext))
				.thenThrow(new WorkflowException("Junit custom test"));
		WorkflowRunnableTask workflowRunnableTask = new WorkflowRunnableTask(task, workflowContext);
		workflowRunnableTask.run();
		Assert.assertTrue(true);
	}
}