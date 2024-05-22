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

package com.ebay.riskmgmt.workfloworchestrator.context.metadata.wf;
/*
 * Created by pishrivastava on 2019-03-24 - 21:16
 */

public class ConditionalWorkFlow {

    private String whenCondition;
    private String thenExecutable;
    private String otherwiseExecutable;
    private String executable;

    public String getWhenCondition() {
        return whenCondition;
    }

    public void setWhenCondition(String whenCondition) {
        this.whenCondition = whenCondition;
    }

    public String getThenExecutable() {
        return thenExecutable;
    }

    public void setThenExecutable(String thenExecutable) {
        this.thenExecutable = thenExecutable;
    }

    public String getOtherwiseExecutable() {
        return otherwiseExecutable;
    }

    public void setOtherwiseExecutable(String otherwiseExecutable) {
        this.otherwiseExecutable = otherwiseExecutable;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

}
