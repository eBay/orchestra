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
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskExecutionUtil.class, AsyncTaskExecutionUtil.class})
public class ConditionalFlowTest {

	@Mock
	private Task toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;

	@Mock
	private TaskReportPredicate predicate;

	private WorkflowContext workflowContext;

	@Before
	public void setUp() throws Exception {

		PowerMockito.mockStatic(AsyncTaskExecutionUtil.class);
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(new DefaultTaskReport(TaskStatus.COMPLETED));
		workflowContext = WorkflowContextBuilder.getWorkflowContext();
		initMocks(this);
	}

	@Test
	public void executeSuccessTest() throws WorkflowException {

		ConditionalFlow conditionalFlow = new ConditionalFlow("condition-flow-task",
				toExecute, nextOnPredicateSuccess, nextOnPredicateFailure, predicate, false);
		final TaskReport report = conditionalFlow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, report.getStatus());
	}

	@Test
	public void executeSuccess2Test() throws WorkflowException {

		PowerMockito.when(predicate.apply(Mockito.any(TaskReport.class))).thenReturn(true);
		ConditionalFlow conditionalFlow = new ConditionalFlow("condition-flow-task",
				toExecute, nextOnPredicateSuccess, nextOnPredicateFailure, predicate, false);
		final TaskReport report = conditionalFlow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, report.getStatus());
	}

	@Test
	public void executeSuccessAsyncTest() throws WorkflowException {

		ConditionalFlow conditionalFlow = new ConditionalFlow("condition-flow-task",
				toExecute, nextOnPredicateSuccess, nextOnPredicateFailure, predicate, true);
		final TaskReport report = conditionalFlow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, report.getStatus());
	}
}