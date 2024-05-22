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
 * Created by pishrivastava on 2019-05-08 - 01:24
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.DefaultTaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskReport;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PrintTask implements Task {

	private String id;

	private static Logger logger = Logger.getLogger(PrintTask.class.getName());

	@Autowired
	public PrintTask(@Qualifier("idString") Integer id) {
		this.id = id.toString();
	}

	@Autowired
	public PrintTask(@Qualifier("idLong") Long id) {
		this.id = id.toString();
	}

	@Autowired
	public PrintTask(@Qualifier("idDouble") Double id) {
		this.id = id.toString();
	}

	@Autowired
	public PrintTask(@Qualifier("idFloat") Float id) {
		this.id = id.toString();
	}

	@Autowired
	public PrintTask(@Qualifier("idObject") Object id) {
		this.id = id.toString();
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public TaskReport execute(WorkflowContext workflowContext) {
		logger.log(Level.INFO, id);
		return new DefaultTaskReport(TaskStatus.COMPLETED);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
