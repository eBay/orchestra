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
 * Created by pishrivastava on 12/23/19 - 2:09 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class AsyncTaskExecutor {


	private static final String CONFIG_CATEGORY_NAME = "AsyncTaskExecutor";
	private static final String CONFIG_CATEGORY_GROUP = "com.ebay.riskmgmt.workfloworchestrator";

	private static final String TASK_EXECUTOR_ID = "AsyncTaskExecutor";

	private static final int EXECUTOR_CORE_SIZE = 20;
	private static final int EXECUTOR_MAX_SIZE = 50;

	private static final Logger LOGGER = Logger.getLogger(AsyncTaskExecutor.class.getName());
	private static final ExecutorService executorService;

	/**
	 * Initializes the task executor.
	 */
	static {
		executorService = Executors.newCachedThreadPool();
	}


	/**
	 * Private constructor to prevent external initialization.
	 */
	private AsyncTaskExecutor() {
		// No instance initialization necessary.
	}

	/**
	 * Creates the config bean for the task executor.
	 *
	 * @return Config bean
	 */



	/**
	 * Gets the task executor.
	 *
	 * @return Task executor
	 */
	public static ExecutorService getTaskExecutor() {
		return executorService;
	}

}
