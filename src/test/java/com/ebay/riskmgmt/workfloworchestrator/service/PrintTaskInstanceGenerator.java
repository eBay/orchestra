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
 * Created by pishrivastava on 2019-04-24 - 15:16
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskInstanceGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrintTaskInstanceGenerator implements TaskInstanceGenerator<String> {
	@Override
	public List<String> getCollection(WorkflowContext workflowContext) {

		List<String> list = new ArrayList<>();
		list.add("Hello Olympic!");
		list.add("Hello Yukon!");
		list.add("Hello Tanggula!");
		list.add("This is Workflow Orchestration");
		return list;
	}
}
