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

package com.ebay.riskmgmt.workfloworchestrator.workflow;

/**
 * @author pishrivastava
 *
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskExecutionUtil.class, AsyncTaskExecutionUtil.class})
public class ParallelFlowTest {

	@Mock
	private Task task1, task2;

	@Mock
	private ParallelTaskExecutor parallelTaskExecutor;

	private WorkflowContext workflowContext;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(AsyncTaskExecutionUtil.class);
		PowerMockito.mockStatic(TaskExecutionUtil.class);
		PowerMockito.when(TaskExecutionUtil.processTask(Mockito.any(Task.class), Mockito.any(WorkflowContext.class)))
				.thenReturn(new DefaultTaskReport(TaskStatus.COMPLETED));
		workflowContext = new WorkflowContext();
		initMocks(this);
	}


	@Test
	public void testCall() throws Exception {

		List<Task> tasks = Arrays.asList(task1, task2);
		ParallelFlow parallelFlow = new ParallelFlow("pf", tasks, 500L, false, parallelTaskExecutor);

		// when
		TaskReport parallelFlowReport = parallelFlow.execute(workflowContext);

		// then
		Assert.assertNotNull(parallelFlowReport);
		Mockito.verify(parallelTaskExecutor).executeInParallel(workflowContext, tasks, 500L);
	}

	@Test
	public void testAsyncCall() throws Exception {

		List<Task> tasks = Arrays.asList(task1, task2);
		ParallelFlow parallelFlow = new ParallelFlow("pf", tasks, 500L, true, parallelTaskExecutor);

		// when
		TaskReport parallelFlowReport = parallelFlow.execute(workflowContext);

		// then
		Assert.assertNotNull(parallelFlowReport);
		Assert.assertEquals(parallelFlowReport.getStatus(), TaskStatus.COMPLETED);
	}

	@Test
	public void builderTest() throws Exception {

		ParallelFlow.Builder builder = ParallelFlow.Builder.aNewParallelFlow();
		Assert.assertNotNull(builder);

		builder.execute(task1, task2);
		builder.named("Parallel Flow Builder");
		builder.timeout(500L);

		final ParallelFlow parallelFlow1 = builder.build();
		Assert.assertEquals(parallelFlow1.getName(), "Parallel Flow Builder");
	}

}
