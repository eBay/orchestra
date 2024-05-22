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

/**
 * @author pishrivastava
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartComponent;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartVisitor;
import com.ebay.riskmgmt.workfloworchestrator.task.AsyncTaskExecutionUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.DefaultTaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.NoOpTask;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskExecutionUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReportPredicate;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import com.ebay.riskmgmt.workfloworchestrator.task.TimesPredicate;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepeatFlow extends AbstractWorkflow {

	private static Logger logger = Logger.getLogger(RepeatFlow.class.getName());
	private Task work;
	private TaskReportPredicate predicate;

	RepeatFlow(String name, Task work, TaskReportPredicate predicate, boolean async) {
		super(name);
		this.work = work;
		this.predicate = predicate;
		setAsyncExecution(async);
	}

	@Override
	public FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitRepeatFlow(this);
	}

	/**
	 * {@inheritDoc}
	 * @param workflowContext
	 */
	public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {

		if (isAsyncExecution()) {
			logger.log(Level.INFO, "Async execution. Repeating the task " + getName());
			setAsyncExecution(false);
			AsyncTaskExecutionUtil.processTaskAsync(this, workflowContext);
			return new DefaultTaskReport(TaskStatus.COMPLETED);

		} else {
			TaskReport workReport;
			logger.log(Level.INFO, "Repeating the task " + work.getName());
			do {
				workReport = TaskExecutionUtil.processTask(work, workflowContext);
			} while (predicate.apply(workReport));
			return workReport;
		}
	}

	public Task getWork() {
		return work;
	}

	public static class Builder {

		private String name;
		private Task work;
		private TaskReportPredicate predicate;
		private boolean async;

		private Builder() {
			this.name = UUID.randomUUID().toString();
			this.work = new NoOpTask();
			this.predicate = new TaskReportPredicate(false);
		}

		public static Builder aNewRepeatFlow() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder repeat(Task work) {
			this.work = work;
			return this;
		}

		public Builder executeAsync(boolean async) {
			this.async = async;
			return this;
		}

		public Builder times(int times) {
			return until(TimesPredicate.times(times));
		}

		public Builder until(TaskReportPredicate predicate) {
			this.predicate = predicate;
			return this;
		}

		public RepeatFlow build() {
			return new RepeatFlow(name, work, predicate, async);
		}
	}
}
