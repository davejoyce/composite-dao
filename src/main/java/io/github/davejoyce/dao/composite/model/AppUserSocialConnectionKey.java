package io.github.davejoyce.dao.composite.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class AppUserSocialConnectionKey implements Serializable {

	private static final long serialVersionUID = -140107446458672095L;

	private AppUser appUser = null;
	private String providerId = null,
			       providerUserId = null;
	
	public AppUserSocialConnectionKey() {}

	public AppUserSocialConnectionKey(final AppUser appUser,
			                          final String providerId,
			                          final String providerUserId) {
		this();
		Validate.notNull(appUser, "appUser argument must not be null!");
		Validate.notNull(providerId, "providerId argument must not be null!");
		this.appUser = appUser;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
	}

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="APP_USER_ID", nullable=false)
	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	@Column(name="PROVIDER_ID", length=255, nullable=false)
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Column(name="PROVIDER_USER_ID", length=255)
	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		           .append(this.getAppUser())
		           .append(providerId)
		           .append(providerUserId)
		           .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		final AppUserSocialConnectionKey other = (AppUserSocialConnectionKey) obj;
		return new EqualsBuilder()
		           .append(getAppUser(), other.getAppUser())
		           .append(providerId, other.providerId)
		           .append(providerUserId, other.providerUserId)
		           .isEquals();
	}

}