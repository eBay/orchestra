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

package com.ebay.riskmgmt.workfloworchestrator.context.core.beans;
/*
 * Created by pishrivastava on 2019-05-02 - 17:22
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

/**
 * Super class for requests and responses for all data fetchers.
 */
public abstract class DataBean implements Cloneable {

    private String id;
    /*
     * Response exists for the request entry
     */
    private boolean responseExists;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isResponseExists() {
        return responseExists;
    }

    public void setResponseExists(boolean responseExists) {
        this.responseExists = responseExists;
    }
}
