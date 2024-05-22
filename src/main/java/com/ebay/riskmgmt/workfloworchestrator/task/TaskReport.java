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

public interface TaskReport {

	/**
	 * Get task execution status.
	 *
	 * @return execution status
	 */
	TaskStatus getStatus();

	/**
	 * Get error if any.
	 *
	 * @return error
	 */
	Throwable getError();

	/**
	 * Report may have an additional outcome. Mandatory for branching flows.
	 *
	 * @return Task outcome as string. Not to be confused with TaskStatus.
	 */
	String getTaskOutcome();
}
