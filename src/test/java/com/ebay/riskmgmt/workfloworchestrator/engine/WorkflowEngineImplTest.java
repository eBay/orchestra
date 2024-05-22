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

package com.ebay.riskmgmt.workfloworchestrator.engine;

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class WorkflowEngineImplTest {

	@InjectMocks
	private WorkflowEngineImpl workflowEngine;

	@Mock
	private Workflow workflow;

	@Mock
	private TaskReport taskReport;

	private WorkflowContext workflowContext;

	/*@Before
	public void setUp() throws WorkflowException {
		workflowContext = new WorkflowContext();
		initMocks(this);
		when(workflow.execute(workflowContext)).thenReturn(taskReport);
	}*/

	@Test
	public void run() throws Exception {
		// when
		workflowEngine.run(workflow, workflowContext);

		// then
		Mockito.verify(workflow).execute(workflowContext);
	}

	@Test
	public void testRunMethod() throws WorkflowException {
		Assert.assertEquals(workflowEngine.run(workflow, workflowContext), null);
	}
}