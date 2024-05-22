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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingUtil {

	private static Logger logger = Logger.getLogger(LoggingUtil.class.getName());

	public static void log(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		if (obj == null) {
			return;
		}
		try {
			String what = obj.getClass().getCanonicalName();
			logger.log(Level.FINE, "====================================BEGIN " + what + " ================================================");
			logger.log(Level.FINE, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			logger.log(Level.FINE, "=====================================END " + what + " =================================================");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception in LoggingUtil log function:  ", e);
		}
	}

	public static String objectToString(final Object object) throws IOException {
		if (object != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		}

		return null;
	}

}
