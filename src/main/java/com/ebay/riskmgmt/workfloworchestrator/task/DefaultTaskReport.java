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

public class DefaultTaskReport implements TaskReport {

	private TaskStatus status;
	private Throwable error;

	/**
	 * Create a new {@link DefaultTaskReport}.
	 *
	 * @param status of work
	 */
	public DefaultTaskReport(TaskStatus status) {
		this.status = status;
	}

	/**
	 * Create a new {@link DefaultTaskReport}.
	 *
	 * @param status of work
	 * @param error  if any
	 */
	public DefaultTaskReport(TaskStatus status, Throwable error) {
		this(status);
		this.error = error;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public Throwable getError() {
		return error;
	}

	@Override
	public String getTaskOutcome() {
		return null;
	}

	@Override
	public String toString() {
		return "DefaultWorkReport {" +
				"status=" + status +
				", error=" + (error == null ? "''" : error) +
				'}';
	}
}
