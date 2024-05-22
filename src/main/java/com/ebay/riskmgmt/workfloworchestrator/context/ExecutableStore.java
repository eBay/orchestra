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
/*
 * Created by pishrivastava on 9/13/19 - 12:02 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store all workflows and tasks.
 */
public class ExecutableStore {

	private Map<String, Task> taskMap = new HashMap<>();
	private Map<String, Workflow> wfMap = new HashMap<>();

	public Map<String, Task> getTaskMap() {
		return taskMap;
	}

	public Map<String, Workflow> getWfMap() {
		return wfMap;
	}
}
