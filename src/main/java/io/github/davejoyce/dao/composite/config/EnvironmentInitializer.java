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

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Sets active Spring configuration profile(s) by detecting the CloudFoundry
 * instance type.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	public void initialize(ConfigurableApplicationContext context) {
		DeployedEnvironment deployEnv = DeployedEnvironment.detect(new CloudEnvironment().getInstanceInfo());
		ConfigurableEnvironment env = context.getEnvironment();
		System.setProperty("targetEnv", deployEnv.getEnvironmentName());
		env.setActiveProfiles(deployEnv.getEnvironmentName());
	}

}
