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

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class NoOpTaskTest {

	private NoOpTask noOpTask;

	private WorkflowContext workflowContext = new WorkflowContext();

	@Before
	public void setUp() throws Exception {
		noOpTask = new NoOpTask();
	}

	@Test
	public void getName() {
		try {
			String name = noOpTask.getName();
			Assert.assertNotNull(name);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void execute() {
		try {
			Object obj = noOpTask.execute(workflowContext);
			Assert.assertNotNull(obj);
		} catch (Exception e) {
			Assert.fail();
		}
	}
}