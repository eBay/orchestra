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
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.task.TaskInstanceGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(CoreAppConfig.class)
public class E2EServiceAppConfig {


	@Bean(name = "simplePrintTask-String")
	@Scope("prototype")
	public Task getPrintString(String idString) {
		return new PrintTask(idString);
	}


	@Bean(name = "simplePrintTask-Integer")
	@Scope("prototype")
	public Task getPrintInteger(Integer idInteger) {
		return new PrintTask(idInteger);
	}

	@Bean(name = "simplePrintTask-Double")
	@Scope("prototype")
	public Task getPrintDouble(Double idDouble) {
		return new PrintTask(idDouble);
	}

	@Bean(name = "simplePrintTask-Float")
	@Scope("prototype")
	public Task getPrintFloat(Float idFloat) {
		return new PrintTask(idFloat);
	}

	@Bean(name = "simplePrintTask-Long")
	@Scope("prototype")
	public Task getPrintLong(Long idLong) {
		return new PrintTask(idLong);
	}

	@Bean(name = "simplePrintTask-Object")
	@Scope("prototype")
	public Task getPrintObject(Object idObject) {
		return new PrintTask(idObject);
	}

	@Bean(name = "simpleSleepTask")
	@Scope("prototype")
	public Task getSleepTask(long time) {
		return new SimpleSleepTask(time);
	}

	@Bean(name = "PrintTaskInstanceGenerator")
	public TaskInstanceGenerator getTaskGenerator() {
		return new PrintTaskInstanceGenerator();
	}

	@Bean
	@Qualifier("idString")
	public String idString() {
		return "anything";
	}

	@Bean
	@Qualifier("idInteger")
	public Integer idInteger() {
		return 0;
	}

	@Bean
	@Qualifier("idDouble")
	public Double idDouble() {
		return 0d;
	}

	@Bean
	@Qualifier("idFloat")
	public Float idFloat() {
		return 0f;
	}

	@Bean
	@Qualifier("idLong")
	public Long idLong() {
		return 0L;
	}

	@Bean
	@Qualifier("idObject")
	public Object idObject() {
		return "Object";
	}

	@Bean
	@Qualifier("time")
	public long time() {
		return 0l;
	}

}
