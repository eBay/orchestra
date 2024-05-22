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

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author pishrivastava
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AsyncTaskExecutionUtil.class, TaskExecutionUtil.class})
public class RepeatFlowTest {

    private WorkflowContext workflowContext = new WorkflowContext();
	
    @Test
    public void testRepeatUntil() throws Exception {
        // given
    	Task task = Mockito.mock(Task.class);

        TaskReportPredicate predicate = new TaskReportPredicate(false);
        RepeatFlow repeatFlow = RepeatFlow.Builder.aNewRepeatFlow()
                .repeat(task)
                .until(predicate)
                .build();

        // when
        repeatFlow.execute(workflowContext);

        // then
        Mockito.verify(task, Mockito.times(1)).execute(workflowContext);
    }

    @Test
    public void RepeatUntilAsyncTest() throws Exception {
        Task task = Mockito.mock(Task.class);
        PowerMockito.mockStatic(AsyncTaskExecutionUtil.class);
        PowerMockito.mockStatic(TaskExecutionUtil.class);

        TaskReportPredicate predicate = new TaskReportPredicate(false);
        RepeatFlow repeatFlow = RepeatFlow.Builder.aNewRepeatFlow()
                .repeat(task)
                .until(predicate)
                .executeAsync(true)
                .build();

        final TaskReport report = repeatFlow.execute(workflowContext);
        Assert.assertEquals(report.getStatus(), TaskStatus.COMPLETED);

    }

    @Test
    public void testRepeatTimes() throws Exception {
        // given
    	
    	Task task = Mockito.mock(Task.class);
        RepeatFlow repeatFlow = RepeatFlow.Builder.aNewRepeatFlow()
                .repeat(task)
                .times(3)
                .build();

        // when
        repeatFlow.execute(workflowContext);

        // then
        Mockito.verify(task, Mockito.times(3)).execute(workflowContext);
    }

}

