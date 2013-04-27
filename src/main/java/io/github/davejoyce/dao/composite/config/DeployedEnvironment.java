/**
 * 
 */
package io.github.davejoyce.dao.composite.config;

import org.cloudfoundry.runtime.env.ApplicationInstanceInfo;

/**
 * @author dave
 *
 */
public enum DeployedEnvironment {

	LOCAL("localhost"),
	CF_MICRO("cloudfoundry"),
	CF_PUBLIC("cloudfoundry");

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
