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

/**
 * @author pishrivastava
 */

public class TaskReportPredicate {

	private boolean status;

	public TaskReportPredicate() {
		this.status = false;
	}

	public TaskReportPredicate(boolean status) {
		this.status = status;
	}

	public static TaskReportPredicate completed() {
		return new TaskReportPredicate(true);
	}

	/**
	 * Apply the predicate (True / False Function) on the given task report.
	 *
	 * @param taskReport on which the predicate should be applied
	 * @return true if the predicate applies on the given report, false
	 * otherwise
	 */
	public boolean apply(TaskReport taskReport) {
		if (taskReport == null || taskReport.getStatus() == null) {
			return status;
		} else return taskReport.getStatus() == TaskStatus.COMPLETED;
	}

	//public static TaskReportPredicate ALWAYS_TRUE;
	//public static TaskReportPredicate ALWAYS_FALSE;
	//public static TaskReportPredicate COMPLETED;//taskReport.getStatus().equals(TaskStatus.COMPLETED);
	//public static TaskReportPredicate FAILED; //taskReport -> taskReport.getStatus().equals(TaskStatus.FAILED);

}