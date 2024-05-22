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
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskExecutionUtil;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sequentially executes tasks. Stops execution of pending tasks in case of any
 * failure in current task.
 */
public class SequentialFlow extends AbstractWorkflow {

	private static final Logger LOGGER = Logger.getLogger(SequentialFlow.class.getName());

	private List<Task> works = new ArrayList<>();

	SequentialFlow(String name, List<Task> works, boolean async) {
		super(name);
		this.works.addAll(works);
		setAsyncExecution(async);
	}

	/**
	 * {@inheritDoc}
	 * @param workflowContext
	 */
	public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {
		TaskReport workReport = null;

		if (isAsyncExecution()) {
			setAsyncExecution(false);
			AsyncTaskExecutionUtil.processTaskAsync(this, workflowContext);
			workReport = new DefaultTaskReport(TaskStatus.COMPLETED);
		} else {
			for (Task work : works) {
				workReport = TaskExecutionUtil.processTask(work, workflowContext);
				if (workReport != null && TaskStatus.FAILED.equals(workReport.getStatus())) {
					LOGGER.log(Level.SEVERE, "Work " + work.toString() + " has failed, skipping subsequent works");
					break;
				}
			}
		}
		return workReport;
	}

	public List<Task> getWorks() {
		return Collections.unmodifiableList(works);
	}

	@Override
	public FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitSequentialFlow(this);
	}

	public static class Builder {

		private String name;
		private List<Task> works;
		private boolean async;

		private Builder() {
			this.name = UUID.randomUUID().toString();
			this.works = new ArrayList<>();
		}

		public static Builder aNewSequentialFlow() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder execute(Task work) {
			this.works.add(work);
			return this;
		}

		public Builder execute(List<Task> works) {
			this.works.addAll(works);
			return this;
		}

		public Builder executeAsync(boolean async) {
			this.async = async;
			return this;
		}

		public Builder then(Task work) {
			this.works.add(work);
			return this;
		}

		public SequentialFlow build() {
			return new SequentialFlow(name, works, async);
		}
	}
}
