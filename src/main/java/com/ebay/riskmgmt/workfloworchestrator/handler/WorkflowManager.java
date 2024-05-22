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
/*
 * Created by pishrivastava on 2019-04-29 - 15:14
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.context.template.ParserException;
import com.ebay.riskmgmt.workfloworchestrator.context.template.WorkflowTemplateParser;
import com.ebay.riskmgmt.workfloworchestrator.engine.WorkflowEngineBuilder;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.WorkflowException;
import com.ebay.riskmgmt.workfloworchestrator.workflow.ParallelTaskExecutor;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowManager {

	private static final Logger logger = Logger.getLogger(WorkflowManager.class.getName());

	@Autowired
	private WorkflowTemplateParser parser;

	public void execute(WorkflowContext workflowContext) throws WorkflowException {
		try {
			Workflow engine = parser.parse(workflowContext);
			buildAndExecute(workflowContext, engine);
			publishStats(workflowContext);
		} catch (ParserException e) {
			logger.log(Level.SEVERE, "Could not parse the config file", e);
			throw new WorkflowException("Workflow failed", e);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Aborting the workflow", ex);
			throw new WorkflowException("Workflow aborted", ex);
		}
	}

	private void publishStats(WorkflowContext workflowContext) {
		logger.log(Level.INFO, "Publishing the stats for " + workflowContext.getContextName());
	}

	private void buildAndExecute(WorkflowContext workflowContext, Workflow engine) throws WorkflowException {
		logger.log(Level.INFO, "===============Workflow Execution Starts=======================");
		TaskReport workReport = WorkflowEngineBuilder.aNewWorkFlowEngine().build().run(engine, workflowContext);
		logger.log(Level.INFO, "===============Workflow Execution Ends=======================");
		logger.log(Level.INFO, "Final Report : " + workReport.getStatus().toString());
	}

}
