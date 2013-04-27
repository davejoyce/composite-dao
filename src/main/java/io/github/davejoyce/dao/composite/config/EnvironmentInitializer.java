/**
 * 
 */
package io.github.davejoyce.dao.composite.config;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author dave
 *
 */
public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	public void initialize(ConfigurableApplicationContext context) {
		DeployedEnvironment deployEnv = DeployedEnvironment.detect(new CloudEnvironment().getInstanceInfo());
		ConfigurableEnvironment env = context.getEnvironment();
		env.setActiveProfiles(deployEnv.getEnvironmentName());
	}

}
