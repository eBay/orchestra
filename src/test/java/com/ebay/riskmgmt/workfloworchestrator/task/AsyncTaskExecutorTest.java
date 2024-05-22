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

// import com.ebay.kernel.bean.configuration.BeanConfigCategoryInfo;
// import com.ebay.kernel.bean.configuration.ConfigCategoryCreateException;
// import com.ebay.kernel.executor.ExecutorPropertyBean;
// import com.ebay.kernel.executor.ExecutorType;
// import com.ebay.kernel.executor.TaskExecutor;
// import com.ebay.kernel.message.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class AsyncTaskExecutorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getTaskExecutor() {
		ExecutorService taskExecutor = AsyncTaskExecutor.getTaskExecutor();
		Assert.assertNotNull(taskExecutor);
	}

}