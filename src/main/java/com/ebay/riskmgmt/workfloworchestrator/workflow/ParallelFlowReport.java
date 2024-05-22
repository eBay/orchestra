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

import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ParallelFlowReport implements TaskReport {

	private List<TaskReport> reports;

	/**
	 * Create a new {@link ParallelFlowReport}.
	 */
	public ParallelFlowReport() {
		this.reports = new ArrayList<>();
	}

	/**
	 * Create a new {@link ParallelFlowReport}.
	 *
	 * @param reports
	 *            of works executed in parallel
	 */
	public ParallelFlowReport(List<TaskReport> reports) {
		this.reports = reports;
	}

	/**
	 * Get partial reports.
	 *
	 * @return partial reports
	 */
	public List<TaskReport> getReports() {
		return reports;
	}

	void add(TaskReport workReport) {
		reports.add(workReport);
	}

	void addAll(List<TaskReport> workReports) {
		reports.addAll(workReports);
	}

	/**
	 * Return the status of the parallel flow.
	 *
	 * The status of a parallel flow is defined as follows:
	 *
	 * <ul>
	 * <li>{@link org.TaskStatus.flows.work.WorkStatus#COMPLETED}: If all works
	 * have successfully completed</li>
	 * <li>{@link org.TaskStatus.flows.work.WorkStatus#FAILED}: If one of the
	 * works has failed</li>
	 * </ul>
	 *
	 * @return workflow status
	 */
	public TaskStatus getStatus() {
		/*
		 * Need to define the prioritization of task reports from config.
		 * For now, if any of the parallel task pass, move ahead.
		 */
		for (TaskReport report : reports) {
			if (report.getStatus().equals(TaskStatus.COMPLETED)) {
				return TaskStatus.COMPLETED;
			}
		}
		return TaskStatus.FAILED;
	}

	/**
	 * Return the first error of partial reports.
	 *
	 * @return the first error of partial reports.
	 */
	@Override
	public Throwable getError() {
		for (TaskReport report : reports) {
			Throwable error = report.getError();
			if (error != null) {
				return error;
			}
		}
		return null;
	}

	/**
	 * Report may have an additional outcome. Mandatory for branching flows.
	 *
	 * @return Outcome as string. Null for Parallel Flow.
	 */
	@Override
	public String getTaskOutcome() {
		return null;
	}
}
