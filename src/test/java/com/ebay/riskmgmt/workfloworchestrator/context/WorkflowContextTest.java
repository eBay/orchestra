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

package com.ebay.riskmgmt.workfloworchestrator.context;

import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class WorkflowContextTest {

	@InjectMocks
	private WorkflowContext<String, String> context;

	@Before
	public void setUp() throws Exception {
		context = WorkflowContextBuilder.getWorkflowContext();
	}

	@Test
	public void getterSetterTest() {
		Assert.assertTrue(context.getContextName().equals("Sample Context"));
		Assert.assertTrue(context.getDataBeanMap() != null);
		//Assert.assertTrue(context.getContextFile().equals("src/test/resources/wfcontext/right-config-context.yaml"));
		Assert.assertTrue(context.getEvaluationId().equals("001"));
		Assert.assertTrue(context.getUserId() == 0L);
		Assert.assertTrue(context.getRequest().equals("Request"));
		Assert.assertTrue(context.getResponse().equals("Response"));
		Assert.assertTrue(context.getVersion().equals("1.0"));
	}
}