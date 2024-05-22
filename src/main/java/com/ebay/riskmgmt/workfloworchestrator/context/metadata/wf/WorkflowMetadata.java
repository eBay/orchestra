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

package com.ebay.riskmgmt.workfloworchestrator.context.metadata.wf;
/*
 * Created by pishrivastava on 3/18/19 - 4:26 PM
 */

public class WorkflowMetadata {

	private String desc;
	private WorkflowBuilderType type;
	private boolean async;
	private RepeatWorkFlow repeatWF;
	private ParallelWorkFlow parallelWF;
	private ConditionalWorkFlow conditionalWF;
	private SequentialWorkFlow sequentialWF;
	private BranchWorkFlow branchWF;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public WorkflowBuilderType getType() {
		return type;
	}

	public void setType(WorkflowBuilderType type) {
		this.type = type;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public RepeatWorkFlow getRepeatWF() {
		return repeatWF;
	}

	public void setRepeatWF(RepeatWorkFlow repeatWF) {
		this.repeatWF = repeatWF;
	}

	public ParallelWorkFlow getParallelWF() {
		return parallelWF;
	}

	public void setParallelWF(ParallelWorkFlow parallelWF) {
		this.parallelWF = parallelWF;
	}

	public ConditionalWorkFlow getConditionalWF() {
		return conditionalWF;
	}

	public void setConditionalWF(ConditionalWorkFlow conditionalWF) {
		this.conditionalWF = conditionalWF;
	}

	public SequentialWorkFlow getSequentialWF() {
		return sequentialWF;
	}

	public void setSequentialWF(SequentialWorkFlow sequentialWF) {
		this.sequentialWF = sequentialWF;
	}

	public BranchWorkFlow getBranchWF() {
		return branchWF;
	}

	public void setBranchWF(BranchWorkFlow branchWF) {
		this.branchWF = branchWF;
	}
}
