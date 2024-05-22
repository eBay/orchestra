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
/*
 * Created by pishrivastava on 3/1/19 - 1:41 PM
 */

import com.ebay.riskmgmt.workfloworchestrator.context.WorkflowContext;
import com.ebay.riskmgmt.workfloworchestrator.task.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SleepTask extends AbstractTask {

    private static Logger logger = Logger.getLogger(SleepTask.class.getName());
    private long duration;

    public SleepTask(Long duration) {
        this.duration = duration;
    }

    @Override
    public String getName() {
        return "Sleep task";
    }

    @Override
    public TaskReport execute(WorkflowContext workflowContext) {
        try {
            logger.log(Level.INFO, "Sleeping for "+duration+" ms.");
            Thread.sleep(duration);
            logger.log(Level.INFO, "Sleep over, awake now.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to complete task", e);
            return new DefaultTaskReport(TaskStatus.FAILED);
        }
        return new DefaultTaskReport(TaskStatus.COMPLETED);
    }

    @Override
    public void postExecution(WorkflowContext workflowContext) {
        System.out.println("SleepTask: Overridden method postExecution");
    }

    @Override
    public void preExecution(WorkflowContext workflowContext) {
        super.preExecution(workflowContext);
    }
}
