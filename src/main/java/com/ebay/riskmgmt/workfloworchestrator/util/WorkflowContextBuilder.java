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

package com.ebay.riskmgmt.workfloworchestrator.util;


import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBean;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBeanKey;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBeanMap;


/*
 * Created by pishrivastava on 2019-04-22 - 13:01
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

public class WorkflowContextBuilder {

	private static final String YAML_FILE_PATH = "src/test/resources/wfcontext/";
	private static final String RIGHT_CONFIG = "right-config-context.yaml";

	public static WorkflowContext getWorkflowContext() {

		WorkflowContext<String, String> workflowContext = new WorkflowContext<>();
		workflowContext.setUserId(0L);
		workflowContext.setEvaluationId("001");
		workflowContext.setVersion("1.0");
		workflowContext.setContextFilePath(YAML_FILE_PATH);
		workflowContext.setContextFile(RIGHT_CONFIG);
		//workflowContext.setContextFile(YAML_FILE_PATH + RIGHT_CONFIG);
		workflowContext.setContextName("Sample Context");
		workflowContext.setRequest("Request");
		workflowContext.setResponse("Response");
		workflowContext.setDataBeanMap(new DataBeanMap<DataBeanKey, DataBean>());

		return workflowContext;
	}
}
