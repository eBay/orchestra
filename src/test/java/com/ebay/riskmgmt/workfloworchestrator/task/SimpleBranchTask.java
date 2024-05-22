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
/*
 * Created by pishrivastava on 9/24/19 - 11:19 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */
import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SimpleBranchTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBranchTask.class);

	@Override
	public String getName() {
		return "SimpleBranchTask";
	}

	@Override
	public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {
		return branchTaskReport();
	}

	private TaskReport branchTaskReport() {
		return new TaskReport() {
			@Override
			public TaskStatus getStatus() {
				return TaskStatus.COMPLETED;
			}

			@Override
			public Throwable getError() {
				return null;
			}

			@Override
			public String getTaskOutcome() {
				Random r = new Random();
				int nextInt = r.nextInt(100);
				LOGGER.info("Random Number is "+nextInt);
				if (nextInt < 33)
					return "A";
				else if (nextInt > 33 && nextInt < 67)
					return "B";
				else
					return "C";
			}
		};
	}
}
