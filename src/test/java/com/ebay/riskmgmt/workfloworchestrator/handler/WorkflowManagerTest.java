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

package com.ebay.riskmgmt.workfloworchestrator.handler;

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.context.template.ParserException;
import com.ebay.riskmgmt.workfloworchestrator.context.template.WorkflowTemplateParser;
import com.ebay.riskmgmt.workfloworchestrator.task.DefaultTaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class WorkflowManagerTest {

	@InjectMocks
	private WorkflowManager wfMgr;

	@Mock
	private WorkflowTemplateParser parser;

	private WorkflowContext context;

	@Mock
	private Workflow workflow;

	@Before
	public void setUp() throws Exception {

		context = WorkflowContextBuilder.getWorkflowContext();
		workflow = new Workflow() {
			@Override
			public String getName() {
				return "test workflow";
			}

			@Override
			public TaskReport execute(WorkflowContext workflowContext) {
				return new DefaultTaskReport(TaskStatus.COMPLETED);
			}
		};

		PowerMockito.when(parser.parse(Mockito.any(WorkflowContext.class))).thenReturn(workflow);
	}

	@Test
	public void testExecute() {
		try {
			wfMgr.execute(context);
		} catch (WorkflowException e) {
			Assert.fail();
		}
		Assert.assertTrue(true);
	}

	@Test
	public void parserExceptionTest() throws ParserException {
		PowerMockito.when(parser.parse(Mockito.any(WorkflowContext.class))).thenThrow(new ParserException("test", new Exception()));
		try {
			wfMgr.execute(context);
		} catch (WorkflowException e) {
			Assert.assertTrue(true);
		}
	}
}