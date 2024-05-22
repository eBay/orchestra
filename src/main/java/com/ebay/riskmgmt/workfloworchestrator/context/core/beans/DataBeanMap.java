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
 * Created by pishrivastava on 2019-05-02 - 17:26
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBeanMap<K extends DataBeanKey, V extends DataBean> {

	private Map<K, V> table = new ConcurrentHashMap<>();

	public void addToMap(K key, V value) {
		table.put(key, value);
	}

	public V getDataBean(K key) {
		return table.get(key);
	}

}
