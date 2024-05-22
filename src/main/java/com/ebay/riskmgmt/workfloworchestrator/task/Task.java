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

package com.ebay.riskmgmt.workfloworchestrator.task;

/**
 * @author pishrivastava
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartComponent;
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartVisitor;

/**
 * Implementations of this interface must:
 * <ul>
 * <li>catch exceptions and return {@link TaskStatus#FAILED}</li>
 * <li>make sure the work in finished in a finite amount of time</li>
 * </ul>
 *
 * Task name must be unique within a workflow.
 */
public interface Task {

	String getName();

	TaskReport execute(WorkflowContext workflowContext) throws WorkflowException;

	default FlowchartComponent accept(FlowchartVisitor visitor) {
		return visitor.visitTask(this);
	}

}
