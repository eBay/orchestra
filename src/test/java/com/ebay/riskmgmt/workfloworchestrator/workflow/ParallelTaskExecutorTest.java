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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author pishrivastava
 *
 */

public class ParallelTaskExecutorTest {

	private WorkflowContext workflowContext = new WorkflowContext();

	@Test
	public void call() throws Exception {

		// given
		HelloWorldTask task1 = new HelloWorldTask("task1", TaskStatus.COMPLETED);
		HelloWorldTask task2 = new HelloWorldTask("task2", TaskStatus.FAILED);
		HelloWorldDecoratedTask task3 = new HelloWorldDecoratedTask("task3", TaskStatus.COMPLETED);
		ParallelTaskExecutor parallelTaskExecutor = ParallelTaskExecutor.getInstance();


		List<Task> list = new ArrayList<>();
		list.add(task1);
		list.add(task2);
		list.add(task3);

		// when
		List<TaskReport> taskReports = parallelTaskExecutor.executeInParallel(workflowContext, list, 5000L);

		// then
		assertEquals(3, taskReports.size());
		assertTrue(task1.isExecuted());
		assertTrue(task2.isExecuted());
		assertTrue(task3.isExecuted());
	}

	@Test
	public void validTestWithConfig() throws Exception {

		// given
		HelloWorldTask task1 = new HelloWorldTask("task1", TaskStatus.COMPLETED);
		HelloWorldTask task2 = new HelloWorldTask("task2", TaskStatus.FAILED);
		ParallelTaskExecutor.init(10, 20);
		ParallelTaskExecutor parallelTaskExecutor = ParallelTaskExecutor.getInstance();


		List<Task> list = new ArrayList<>();
		list.add(task1);
		list.add(task2);

		// when
		List<TaskReport> taskReports = parallelTaskExecutor.executeInParallel(workflowContext, list, 5000L);

		// then
		assertEquals(2, taskReports.size());
		assertEquals(true, task1.isExecuted());
		assertEquals(true, task2.isExecuted());
	}

	@Test
	public void parallelCallsPassWithException() throws Exception {

		TestTaskWithException task1 = new TestTaskWithException("task1", TaskStatus.COMPLETED);
		TestTaskWithException task2 = new TestTaskWithException("task2", TaskStatus.FAILED);
		ParallelTaskExecutor parallelTaskExecutor = ParallelTaskExecutor.getInstance();

		List<Task> list = new ArrayList<>();
		list.add(task1);
		list.add(task2);

		List<TaskReport> taskReports = parallelTaskExecutor.executeInParallel(workflowContext, list, 500L);

		assertEquals(2, taskReports.size());
		final TaskReport taskReport1 = taskReports.get(0);
		final TaskReport taskReport2 = taskReports.get(1);

		assertEquals(true, taskReport1.getError() != null
		|| taskReport2.getError() != null);
	}

	class HelloWorldTask implements Task {

		private String name;
		private TaskStatus status;
		private boolean executed;

		HelloWorldTask(String name, TaskStatus status) {
			this.name = name;
			this.status = status;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public TaskReport execute(WorkflowContext workflowContext) {
			executed = true;
			return new DefaultTaskReport(status);
		}

		public boolean isExecuted() {
			return executed;
		}
	}

	class HelloWorldDecoratedTask extends AbstractTask {

		private String name;
		private TaskStatus status;
		private boolean executed;

		HelloWorldDecoratedTask(String name, TaskStatus status) {
			this.name = name;
			this.status = status;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public TaskReport execute(WorkflowContext workflowContext) {
			executed = true;
			return new DefaultTaskReport(status);
		}

		public boolean isExecuted() {
			return executed;
		}

		@Override
		public void postExecution(WorkflowContext workflowContext) {
			super.postExecution(workflowContext);
		}

		@Override
		public void preExecution(WorkflowContext workflowContext) {
			super.preExecution(workflowContext);
		}
	}

	class TestTaskWithException implements Task {

		private String name;
		private TaskStatus status;
		private boolean executed;

		TestTaskWithException(String name, TaskStatus status) {
			this.name = name;
			this.status = status;
		}


		@Override
		public String getName() {
			return name;
		}

		@Override
		public TaskReport execute(WorkflowContext workflowContext) {
			executed = true;
			return (TaskReport) new DefaultTaskReport(status, new Exception("Test Exception"));
		}
	}

	class RequestAttributeTask implements Task {

		private String name;
		private TaskStatus status;
		private boolean executed;

		RequestAttributeTask(String name, TaskStatus status) {
			this.name = name;
			this.status = status;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public TaskReport execute(WorkflowContext workflowContext) {
			executed = true;
			return new DefaultTaskReport(status);
		}

		public boolean isExecuted() {
			return executed;
		}
	}

}
