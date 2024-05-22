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

package com.ebay.riskmgmt.workfloworchestrator.service;

import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class PrintMessageTaskTest {

	@Test
	public void testConstructors() {
		PrintMessageTask printMessageTask = new PrintMessageTask("Msg");
		Assert.assertTrue(printMessageTask.getMessage().equalsIgnoreCase("Msg"));

		printMessageTask = new PrintMessageTask(1D);
		Assert.assertTrue(printMessageTask.getMessage().equalsIgnoreCase("1.0"));

		printMessageTask = new PrintMessageTask(1);
		Assert.assertTrue(printMessageTask.getMessage().equalsIgnoreCase("1"));

		printMessageTask = new PrintMessageTask(1f);
		Assert.assertTrue(printMessageTask.getMessage().equalsIgnoreCase("1.0"));

		printMessageTask = new PrintMessageTask();
		Assert.assertTrue(printMessageTask.getMessage() == null);
	}

	@Test
	public void executeTest() {
		PrintMessageTask printMessageTask = new PrintMessageTask("Print Task");
		TaskReport taskReport = printMessageTask.execute(WorkflowContextBuilder.getWorkflowContext());
		Assert.assertTrue(taskReport.getStatus() == TaskStatus.COMPLETED);
	}

}