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

import com.ebay.riskmgmt.workfloworchestrator.task.DefaultTaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParallelFlowReportTest {

	private ParallelFlowReport parallelFlowReport;

	@Before
	public void setUp() throws Exception {
		parallelFlowReport = new ParallelFlowReport();
		parallelFlowReport.add(new DefaultTaskReport(TaskStatus.COMPLETED));
	}

	@Test
	public void getReports() {
		final List<TaskReport> reports = parallelFlowReport.getReports();
		assertEquals(1, reports.size());
	}

	@Test
	public void add() {
		List<TaskReport> taskReports = new ArrayList<>();
		taskReports.add(new DefaultTaskReport(TaskStatus.COMPLETED));
		ParallelFlowReport parallelFlowReport = new ParallelFlowReport(taskReports);
		assertEquals(1, parallelFlowReport.getReports().size());
	}

	@Test
	public void addAll() {
		List<TaskReport> taskReports = new ArrayList<>();
		taskReports.add(new DefaultTaskReport(TaskStatus.COMPLETED));
 		parallelFlowReport.addAll(taskReports);
		assertEquals(2, parallelFlowReport.getReports().size());
	}

	@Test
	public void getStatus() {
		final TaskStatus status = parallelFlowReport.getStatus();
		assertSame(status, TaskStatus.COMPLETED);
	}

	@Test
	public void getError() {
		parallelFlowReport.add(new DefaultTaskReport(TaskStatus.FAILED, new Exception("Failed Task")));
		final Throwable error = parallelFlowReport.getError();
		assertNotNull(error);
	}
}