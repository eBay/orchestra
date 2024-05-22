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
/*
 * Created by pishrivastava on 9/12/19 - 2:59 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.handler.WorkflowManager;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;
import com.ebay.riskmgmt.workfloworchestrator.util.WorkflowContextBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ParentE2ETest {

	private static final String YAML_FILE_PATH = "wfcontext";
	private static final String FILE_NAME = "parent.yaml";

	public static void main(String[] args) {
		ParentE2ETest endToEndTest = new ParentE2ETest();
		endToEndTest.exec();
	}

	private void exec() {
		WorkflowContext context = WorkflowContextBuilder.getWorkflowContext();
		context.setContextFilePath(YAML_FILE_PATH);
		context.setContextFile(FILE_NAME);
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ParentAppConfig.class);

		WorkflowManager manager = (WorkflowManager) ctx.getBean("workflowManager");
		try {
			manager.execute(context);
		} catch (WorkflowException e) {
			e.printStackTrace();
		}
	}
}
