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

package com.ebay.riskmgmt.workfloworchestrator.context.template;

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowTemplateParserTest {

	@InjectMocks
	private WorkflowTemplateParser workflowTemplateParser;

	private static final String YAML_FILE_PATH = "wfcontext";
	private static final String RIGHT_CONFIG = "right-config-context.yaml";
	private static final String RIGHT_PARENT_CONFIG = "parent.yaml";
	private static final String INVALID_CLASS_CONFIG = "invalid-class-context.yaml";
	private static final String INVALID_TASK_CONFIG = "invalid-task-config-context.yaml";
	private static final String INVALID_WF_CONFIG = "invalid-wf-config-context.yaml";
	private static final String INVALID_ENGINE_CONFIG = "invalid-engine-config-context.yaml";

	@Before
	public void setUp() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ParentAppConfig.class);

		workflowTemplateParser = (WorkflowTemplateParser) ctx.getBean("workflowTemplateParser");
	}

	@Test
	public void correctParentConfigTest() {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(RIGHT_PARENT_CONFIG);
		try {
			final Workflow workflow = workflowTemplateParser.parse(context);
			Assert.assertTrue(null != workflow);
			Assert.assertTrue(workflow.getName().equalsIgnoreCase("main sequential parent workflow"));
		} catch (ParserException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}


	@Test
	public void correctConfigTest() {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(RIGHT_CONFIG);
		try {
			final Workflow workflow = workflowTemplateParser.parse(context);
			Assert.assertTrue(null != workflow);
			Assert.assertTrue(workflow.getName().equalsIgnoreCase("main sequential workflow"));
		} catch (ParserException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@Test
	public void invalidTaskConfigTest() {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(INVALID_TASK_CONFIG);
		try {
			workflowTemplateParser.parse(context);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ParserException);
		}
	}

	@Test
	public void invalidClassConfigTest() throws IOException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(INVALID_CLASS_CONFIG);
		try {
			workflowTemplateParser.parse(context);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ParserException);
		}
	}

	@Test
	public void invalidEngineConfigTest() throws IOException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(INVALID_ENGINE_CONFIG);
		try {
			workflowTemplateParser.parse(context);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ParserException);
		}
	}

	@Test
	public void invalidWfConfigTest() throws IOException {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(INVALID_WF_CONFIG);
		try {
			workflowTemplateParser.parse(context);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ParserException);
		}
	}
}