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

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class LoggingUtilTest {

	@Mock
	private Object obj;

	@Test
	public void log() {
		try {
			LoggingUtil.log("Hello");
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void logNull() {
		try {
			LoggingUtil.log(null);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void objectToStringTest() {
		try {
			final String string = LoggingUtil.objectToString("John Smith");
			Assert.assertTrue("\"John Smith\"".equals(string));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void objectToStringNullTest() {
		try {
			final String string = LoggingUtil.objectToString(null);
			Assert.assertTrue(string == null);
		} catch (Exception e) {
			Assert.fail();
		}
	}
}