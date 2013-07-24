/*
 * Copyright 2013 David Joyce
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
package io.github.davejoyce.dao.composite.config;

import org.cloudfoundry.runtime.env.ApplicationInstanceInfo;

/**
 * Typesafe enumeration of deployment environment types.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public enum DeployedEnvironment {

	LOCAL("development"),
	CF_MICRO("cloud"),
	CF_PUBLIC("cloud");

	private final String environmentName;

	private DeployedEnvironment(final String environmentName) {
		this.environmentName = environmentName;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public static DeployedEnvironment detect(ApplicationInstanceInfo info) {
		DeployedEnvironment de = null;
		if (null == info) {
			de = LOCAL;
		} else if ("172.16.52.136".equals(info.getHost())) {
			de = CF_MICRO;
		} else {
			de = CF_PUBLIC;
		}
		return de;
	}

}
