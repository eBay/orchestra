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
/*
 * Created by pishrivastava on 3/1/19 - 1:41 PM
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;

import com.ebay.riskmgmt.workfloworchestrator.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationTask implements Task {

	private static Logger logger = LoggerFactory.getLogger(ValidationTask.class);

	@Override
	public String getName() {
		return "Validation task";
	}

	@Override
	public TaskReport execute(WorkflowContext workflowContext) {
		try {
			logger.info("Dummy Validation Task");
		} catch (Exception e) {
			logger.error("Unable to complete task", e);
			return new DefaultTaskReport(TaskStatus.FAILED);
		}
		return new DefaultTaskReport(TaskStatus.COMPLETED);
	}
}
