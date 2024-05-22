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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A predicate that returns true after a given number of times.
 */
public class TimesPredicate extends TaskReportPredicate {

	private int times;

	private AtomicInteger counter = new AtomicInteger();

	public TimesPredicate(int times) {
		this.times = times;
	}

	public static TimesPredicate times(int times) {
		return new TimesPredicate(times);
	}

	@Override
	public boolean apply(TaskReport taskReport) {
		return counter.incrementAndGet() < times;
	}
}
