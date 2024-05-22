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
 * Created by pishrivastava on 2019-05-08 - 23:01
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SimpleSleepTask extends AbstractTask {

	private long sleepTime;

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Autowired
	public SimpleSleepTask(@Qualifier("sleepTime") long sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public String getName() {
		return "Sleep: "+sleepTime;
	}

	@Override
	public TaskReport execute(WorkflowContext workflowContext) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new DefaultTaskReport(TaskStatus.COMPLETED);
	}



	@Override
	public void postExecution(WorkflowContext workflowContext) {
		System.out.println("SimpleSleepTask: Overridden method postExecution");
	}

	@Override
	public void preExecution(WorkflowContext workflowContext) {
		super.preExecution(workflowContext);
	}
}
