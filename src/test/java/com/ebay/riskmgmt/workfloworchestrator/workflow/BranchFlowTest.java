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
import com.ebay.riskmgmt.workfloworchestrator.service.PrintMessageTask;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskExecutionUtil.class, AsyncTaskExecutionUtil.class})
public class BranchFlowTest {

	private Task toExecute = new SimpleBranchTask();

	@Mock
	private Map<String, Task> taskCaseMap;

	private WorkflowContext workflowContext;

	@Before
	public void setUp() throws Exception {

		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(new DefaultTaskReport(TaskStatus.COMPLETED));
		workflowContext = WorkflowContextBuilder.getWorkflowContext();
		initMocks(this);
	}

	@Test
	public void executeFailedTaskTest() throws WorkflowException {

		BranchFlow branchFlow = new BranchFlow("Branch Flow", toExecute, taskCaseMap, false);

		final TaskReport report = branchFlow.execute(workflowContext);
		assertSame(TaskStatus.FAILED, report.getStatus());
	}

	@Test
	public void executeSuccessTaskTest() throws WorkflowException {
		Map<String, Task> map = new HashMap<>();
		map.put("A", new NoOpTask());
		map.put("B", new PrintMessageTask());
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(branchTaskReport());

		BranchFlow branchFlow = new BranchFlow("Branch Flow", toExecute, map, false);
		final TaskReport report = branchFlow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, report.getStatus());
	}

	@Test
	public void executeSuccessTaskAsyncTest() throws WorkflowException {
		Map<String, Task> map = new HashMap<>();
		map.put("A", new NoOpTask());
		map.put("B", new PrintMessageTask());
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.mockStatic(AsyncTaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(branchTaskReport());

		BranchFlow branchFlow = new BranchFlow("Branch Flow", toExecute, map, true);
		final TaskReport report = branchFlow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, report.getStatus());
	}

	@Test
	public void builderTest() throws WorkflowException {
		Map<String, Task> map = new HashMap<>();
		map.put("A", new NoOpTask());
		map.put("B", new PrintMessageTask());
		map.put("C", new PrintMessageTask());
		BranchFlow.Builder branchFlowBuilder = BranchFlow.Builder.aNewBranchFlow().toExecute(new SimpleBranchTask()).addCases(map).named("Branch Flow");
		assertNotNull(branchFlowBuilder);

		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(branchTaskReport());

		BranchFlow flow = branchFlowBuilder.build();
		TaskReport taskReport = flow.execute(workflowContext);
		assertSame(TaskStatus.COMPLETED, taskReport.getStatus());


	}

	private TaskReport branchTaskReport() {
		return new TaskReport() {
			@Override
			public TaskStatus getStatus() {
				return TaskStatus.COMPLETED;
			}

			@Override
			public Throwable getError() {
				return null;
			}

			@Override
			public String getTaskOutcome() {
				return "A";
			}
		};
	}
}