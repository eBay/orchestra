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
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConditionalFlow extends AbstractWorkflow {

	private static final Logger LOGGER = Logger.getLogger(ConditionalFlow.class.getName());
	private Task toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
	private TaskReportPredicate predicate;

	ConditionalFlow(String name, Task toExecute, Task nextOnPredicateSuccess,
	                Task nextOnPredicateFailure, TaskReportPredicate predicate, boolean async) {
		super(name);
		this.toExecute = toExecute;
		this.nextOnPredicateSuccess = nextOnPredicateSuccess;
		this.nextOnPredicateFailure = nextOnPredicateFailure;
		this.predicate = predicate;
		setAsyncExecution(async);
	}

	/**
	 * {@inheritDoc}
	 * @param workflowContext
	 */
	public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {

		if (isAsyncExecution()) {
			LOGGER.log(Level.INFO, "Async execution. Executing task " + getName());
			setAsyncExecution(false);
			AsyncTaskExecutionUtil.processTaskAsync(this, workflowContext);
			return new DefaultTaskReport(TaskStatus.COMPLETED);

		} else {

			LOGGER.log(Level.INFO, "Executing task " + toExecute.getName());

			TaskReport jobReport = TaskExecutionUtil.processTask(toExecute, workflowContext);
			if (predicate.apply(jobReport)) {
				jobReport = TaskExecutionUtil.processTask(nextOnPredicateSuccess, workflowContext);
			} else {
				if (nextOnPredicateFailure != null
						&& !(nextOnPredicateFailure instanceof NoOpTask)) { // else
					// is
					// optional
					jobReport = TaskExecutionUtil.processTask(nextOnPredicateFailure, workflowContext);
				}
			}
			return jobReport;
		}
	}

	public Task getToExecute() {
		return toExecute;
	}

	public Task getNextOnPredicateSuccess() {
		return nextOnPredicateSuccess;
	}

	public Task getNextOnPredicateFailure() {
		return nextOnPredicateFailure;
	}

	@Override
	public FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitConditionalFlow(this);
	}

	public static class Builder {

		private String name;
		private Task toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
		private TaskReportPredicate predicate;
		private boolean async;

		private Builder() {
			this.name = UUID.randomUUID().toString();
			this.toExecute = new NoOpTask();
			this.nextOnPredicateSuccess = new NoOpTask();
			this.nextOnPredicateFailure = new NoOpTask();
			this.predicate = new TaskReportPredicate(false);
		}

		public static Builder aNewConditionalFlow() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder execute(Task work) {
			this.toExecute = work;
			return this;
		}

		public Builder executeAsync(boolean async) {
			this.async = async;
			return this;
		}

		public Builder when(TaskReportPredicate predicate) {
			this.predicate = predicate;
			return this;
		}

		public Builder then(Task work) {
			this.nextOnPredicateSuccess = work;
			return this;
		}

		public Builder otherwise(Task work) {
			this.nextOnPredicateFailure = work;
			return this;
		}

		public ConditionalFlow build() {
			return new ConditionalFlow(name, toExecute, nextOnPredicateSuccess,
					nextOnPredicateFailure, predicate, async);
		}
	}
}
