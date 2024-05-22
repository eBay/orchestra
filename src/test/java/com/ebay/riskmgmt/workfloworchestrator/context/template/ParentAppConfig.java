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

package com.ebay.riskmgmt.workfloworchestrator.context.template;
/*
 * Created by pishrivastava on 9/12/19 - 2:47 PM
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.service.PrintTask;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import({ServiceAppConfig.class, E2EServiceAppConfig.class})
public class ParentAppConfig {

	@Bean(name = "parentPrintStartTask")
	@Scope("prototype")
	public Task getParentPrintStartTask(String id) {
		return new PrintTask(id);
	}

	@Bean(name = "parentPrintEndTask")
	@Scope("prototype")
	public Task getParentPrintEndTask(String id) {
		return new PrintTask(id);
	}

	@Bean(name = "parentPrintTask1")
	@Scope("prototype")
	public Task getParentPrintTask1(String id) {
		return new PrintTask(id);
	}

	@Bean(name = "parentPrintTask2")
	@Scope("prototype")
	public Task getParentPrintTask2(String id) {
		return new PrintTask(id);
	}
}
