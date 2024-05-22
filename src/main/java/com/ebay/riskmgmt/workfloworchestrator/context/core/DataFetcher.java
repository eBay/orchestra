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

package com.ebay.riskmgmt.workfloworchestrator.context.core;
/*
 * Created by pishrivastava on 2019-05-02 - 17:29
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */


import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.InputBean;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.OutputBean;

public interface DataFetcher<T extends OutputBean, U extends InputBean> {

    /**
     * @param input of a type which extends DTO
     * @return an output of a type which extends DataBean
     */
    T fetchData(U input);
}
