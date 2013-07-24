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
package io.github.davejoyce.dao.composite.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A single social network connection for a particular {@link AppUser}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name="APP_USER_SOCIAL_CONNECTION", uniqueConstraints={
	@UniqueConstraint(columnNames={"APP_USER_ID", "PROVIDER_ID", "RANK"})
})
public class AppUserSocialConnection implements Serializable {

	private static final long serialVersionUID = -492130362527581378L;

	private AppUserSocialConnectionKey key = null;
	private int rank = 0;
	private String displayName = null,
			       profileUrl = null,
			       imageUrl = null,
			       accessToken = null,
			       secret = null,
			       refreshToken = null;
	private Long expireTime = null;

	public AppUserSocialConnection() {}

	public AppUserSocialConnection(final AppUser appUser,
			                       final String providerId,
			                       final String providerUserId,
			                       final int rank,
			                       final String accessToken) {
		this();
		this.key = new AppUserSocialConnectionKey(appUser, providerId, providerUserId);
		this.rank = rank;
		this.accessToken = accessToken;
	}

	@EmbeddedId
	public AppUserSocialConnectionKey getKey() {
		return key;
	}

	public void setKey(AppUserSocialConnectionKey key) {
		this.key = key;
	}

	@Transient
	public AppUser getAppUser() {
		return (null == key) ? null : key.getAppUser();
	}

	@Transient
	public String getProviderId() {
		return (null == key) ? null : key.getProviderId();
	}

	@Transient
	public String getProviderUserId() {
		return (null == key) ? null : key.getProviderUserId();
	}

	@Column(name="RANK", nullable=false)
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Column(name="DISPLAY_NAME", length=255)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name="PROFILE_URL", length=512)
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	@Column(name="IMAGE_URL", length=512)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name="ACCESS_TOKEN", length=255, nullable=false)
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name="SECRET", length=255)
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name="REFRESH_TOKEN", length=255)
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Column(name="EXPIRE_TM")
	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		           .append(null == key ? null : key.getAppUser())
		           .append(null == key ? null : key.getProviderId())
		           .append(null == key ? null : key.getProviderUserId())
		           .toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		           .append(key)
		           .append(rank)
		           .append(displayName)
		           .append(profileUrl)
		           .append(imageUrl)
		           .append(accessToken)
		           .append(secret)
		           .append(refreshToken)
		           .append(expireTime)
		           .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		final AppUserSocialConnection other = (AppUserSocialConnection) obj;
		return new EqualsBuilder()
		           .append(key, other.key)
		           .append(rank, other.rank)
		           .append(displayName, other.displayName)
		           .append(profileUrl, other.profileUrl)
		           .append(imageUrl, other.imageUrl)
		           .append(accessToken, other.accessToken)
		           .append(secret, other.secret)
		           .append(refreshToken, other.refreshToken)
		           .append(expireTime, other.expireTime)
		           .isEquals();
	}

}
