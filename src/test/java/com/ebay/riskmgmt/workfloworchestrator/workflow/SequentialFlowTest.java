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

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AsyncTaskExecutionUtil.class)
public class SequentialFlowTest {
	private SequentialFlow sequentialFlow;

	@Mock
	private Task failedTask;

	@Mock
	private Task succeedTask;

	@Mock
	private TaskReport failedTaskReport;

	@Mock
	private TaskReport succeedTaskReport;

	private WorkflowContext workflowContext;

	@Before
	public void setUp() throws Exception {
		workflowContext = new WorkflowContext();
		initMocks(this);
		when(failedTask.execute(workflowContext)).thenReturn(failedTaskReport);
		when(succeedTask.execute(workflowContext)).thenReturn(succeedTaskReport);
		when(failedTaskReport.getStatus()).thenReturn(TaskStatus.FAILED);
		when(succeedTaskReport.getStatus()).thenReturn(TaskStatus.COMPLETED);
	}

	@Test
	public void testCall_whenFailedTaskOnly() throws WorkflowException {
		sequentialFlow = new SequentialFlow("test", asList(failedTask), false);
		assertEquals(sequentialFlow.execute(workflowContext), failedTaskReport);
	}

	@Test
	public void testCall_whenSucceedTaskOnly() throws WorkflowException {
		sequentialFlow = new SequentialFlow("test", asList(succeedTask), false);
		assertEquals(sequentialFlow.execute(workflowContext), succeedTaskReport);
	}

	@Test
	public void testCall_whenSucceedTaskOnlyAsync() throws WorkflowException {
		PowerMockito.mockStatic(AsyncTaskExecutionUtil.class);
		sequentialFlow = new SequentialFlow("test", asList(succeedTask), true);
		final TaskReport report = sequentialFlow.execute(workflowContext);
		assertEquals(report.getTaskOutcome(), succeedTaskReport.getTaskOutcome());
	}

	@Test
	public void testCall_whenFailedTaskAndSucceedTask() throws WorkflowException {
		sequentialFlow = new SequentialFlow("test", asList(failedTask, succeedTask), false);
		assertEquals(sequentialFlow.execute(workflowContext), failedTaskReport);
	}

	@Test
	public void testCall_whenSucceedTaskAndFailedTask() throws WorkflowException {
		sequentialFlow = new SequentialFlow("test", asList(succeedTask, failedTask), false);
		assertEquals(sequentialFlow.execute(workflowContext), failedTaskReport);
	}

	@Test
	public void testBuilder() throws WorkflowException {
		SequentialFlow.Builder builder = SequentialFlow.Builder.aNewSequentialFlow();
		builder.execute(failedTask);
		builder.named("test");
		builder.then(succeedTask);

		SequentialFlow sequentialFlow = builder.build();
		assertEquals(failedTaskReport, sequentialFlow.execute(workflowContext));
	}
}