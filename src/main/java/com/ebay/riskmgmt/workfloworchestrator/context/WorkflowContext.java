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

package com.ebay.riskmgmt.workfloworchestrator.context;
/*
 * Created by pishrivastava on 2019-04-12 - 12:14
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBean;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBeanKey;
import com.ebay.riskmgmt.workfloworchestrator.context.core.beans.DataBeanMap;

import java.io.InputStream;


public class WorkflowContext<T, U> {

	private long userId;

	private String evaluationId;

	private String contextName;

	private String version;

	private String contextFile;

	private String contextFilePath;
	
	private InputStream contextFileInputStream;

	private T request;

	private U response;

	private DataBeanMap<DataBeanKey, ? extends DataBean> dataBeanMap = new DataBeanMap<>();

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContextFile() {
		return contextFile;
	}

	public void setContextFile(String contextFile) {
		this.contextFile = contextFile;
	}

	public String getContextFilePath() {
		return contextFilePath;
	}

	public void setContextFilePath(String contextFilePath) {
		this.contextFilePath = contextFilePath;
	}

	public InputStream getContextFileInputStream() {
		return contextFileInputStream;
	}

	public void setContextFileInputStream(InputStream contextFileInputStream) {
		this.contextFileInputStream = contextFileInputStream;
	}

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}

	public U getResponse() {
		return response;
	}

	public void setResponse(U response) {
		this.response = response;
	}

	public DataBeanMap<DataBeanKey, ? extends DataBean> getDataBeanMap() {
		return dataBeanMap;
	}

	public void setDataBeanMap(DataBeanMap<DataBeanKey, ? extends DataBean> dataBeanMap) {
		this.dataBeanMap = dataBeanMap;
	}

}
