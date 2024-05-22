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
 * Created by pishrivastava on 3/6/19 - 4:29 PM
 */

import com.ebay.riskmgmt.workfloworchestrator.context.metadata.TaskMetadata;
import com.ebay.riskmgmt.workfloworchestrator.context.metadata.wf.WorkflowMetadata;

import java.util.List;
import java.util.Map;

public class WorkflowTemplate {

	private String name;
	private String version;
	private List<String> importFiles;
	private Map<String, TaskMetadata> task;
	private Map<String, WorkflowMetadata> workflow;
	private String engine;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getImportFiles() {
		return importFiles;
	}

	public void setImportFiles(List<String> importFiles) {
		this.importFiles = importFiles;
	}

	public Map<String, TaskMetadata> getTask() {
		return task;
	}

	public void setTask(Map<String, TaskMetadata> task) {
		this.task = task;
	}

	public Map<String, WorkflowMetadata> getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Map<String, WorkflowMetadata> workflow) {
		this.workflow = workflow;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}
}
