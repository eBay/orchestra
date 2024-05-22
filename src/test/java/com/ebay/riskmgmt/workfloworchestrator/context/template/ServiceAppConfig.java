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
 * Created by pishrivastava on 2019-05-08 - 22:56
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.service.PrintTask;
import com.ebay.riskmgmt.workfloworchestrator.service.PrintTaskInstanceGenerator;
import com.ebay.riskmgmt.workfloworchestrator.service.SimpleSleepTask;
import com.ebay.riskmgmt.workfloworchestrator.spring.CoreAppConfig;
import com.ebay.riskmgmt.workfloworchestrator.task.SimpleBranchTask;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskInstanceGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(CoreAppConfig.class)
public class ServiceAppConfig {


	@Bean(name = "simplePrintTask")
	@Scope("prototype")
	public Task getPrint(String id) {
		return new PrintTask(id);
	}

	@Bean(name = "simpleSleepTask")
	@Scope("prototype")
	public Task getSleepTask(long time) {
		return new SimpleSleepTask(time);
	}

	@Bean(name = "simpleBranchTask")
	public Task getBranchTask() {
		return new SimpleBranchTask();
	}


	@Bean(name = "PrintTaskInstanceGenerator")
	public TaskInstanceGenerator getTaskGenerator() {
		return new PrintTaskInstanceGenerator();
	}

	@Bean
	@Qualifier("id")
	public String id() {
		return "anything";
	}

	@Bean
	@Qualifier("time")
	public long time() {
		return 0l;
	}

}
