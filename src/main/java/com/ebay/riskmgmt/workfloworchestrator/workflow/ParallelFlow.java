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
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelFlow extends AbstractWorkflow {

	private static final Logger LOGGER = Logger.getLogger(ParallelFlow.class.getCanonicalName());

	private List<Task> works = new ArrayList<>();
	private ParallelTaskExecutor workExecutor;
	private long timeout;

	ParallelFlow(String name, List<Task> works, Long timeout, boolean async, ParallelTaskExecutor parallelTaskExecutor) {
		super(name);
		this.works.addAll(works);
		this.timeout = timeout;
		setAsyncExecution(async);
		this.workExecutor = parallelTaskExecutor;
	}

	/**
	 * {@inheritDoc}
	 * @param workflowContext
	 */
	public TaskReport execute(WorkflowContext workflowContext) {
		if (isAsyncExecution()) {
			LOGGER.log(Level.INFO, "Async execution. Executing parallel tasks: " + getName());
			setAsyncExecution(false);
			AsyncTaskExecutionUtil.processTaskAsync(this, workflowContext);
			return new DefaultTaskReport(TaskStatus.COMPLETED);
		} else {
			LOGGER.log(Level.INFO, "Executing parallel tasks");
			ParallelFlowReport workFlowReport = new ParallelFlowReport();
			List<TaskReport> workReports = workExecutor.executeInParallel(workflowContext, works, timeout);
			workFlowReport.addAll(workReports);
			return workFlowReport;
		}
	}

	public List<Task> getWorks() {
		return Collections.unmodifiableList(works);
	}

	@Override
	public FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitParallelFlow(this);
	}

	public static class Builder {

		private String name;
		private List<Task> works;
		private long timeout;
		private boolean async;

		private Builder() {
			this.name = UUID.randomUUID().toString();
			this.works = new ArrayList<>();
			this.timeout = 5000L;
		}

		public static Builder aNewParallelFlow() {
			return new Builder();
		}

		public Builder named(String name) {
			this.name = name;
			return this;
		}

		public Builder timeout(Long timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder executeAsync(boolean async) {
			this.async = async;
			return this;
		}

		public Builder execute(Task... works) {
			this.works.addAll(Arrays.asList(works));
			return this;
		}

		public Builder execute(List<Task> works) {
			this.works.addAll(works);
			return this;
		}

		public ParallelFlow build() {
			return new ParallelFlow(name, works, timeout, async, ParallelTaskExecutor.getInstance());
		}
	}
}
