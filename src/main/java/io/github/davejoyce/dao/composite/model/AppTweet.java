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
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Index;

/**
 * Application stored tweet of an {@link AppUser}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name="APP_TWEET")
@org.hibernate.annotations.Table(
		appliesTo="APP_TWEET",
		indexes={
			@Index(name="AT_APP_USER_CREATED_DT_IDX", columnNames={"APP_USER_ID", "CREATED_DT"})
		}
)
public class AppTweet implements Serializable {

	private static final long serialVersionUID = -4913248831382157279L;

	private Long id = null;
	private AppUser appUser = null;
	private String text = null;

	private Long fromAppUserId = null;
	private String fromAppUserName = null;

	private Long toAppUserId = null;
	private String toAppUserName = null;

	private Date createdDate = null;

	public AppTweet() {}

	public AppTweet(Long id, AppUser appUser, String text) {
		this(id, appUser, text, Calendar.getInstance().getTime());
	}

	public AppTweet(Long id, AppUser appUser, String text, Date createdDate) {
		this.id = id;
		this.appUser = appUser;
		this.text = text;
		this.createdDate = createdDate;
	}

	@Id
	@Column(name="APP_TWEET_ID", nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="APP_USER_ID", nullable=false)
	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	@Column(name="TEXT", length=140)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name="FROM_APP_USER_ID")
	public Long getFromAppUserId() {
		return fromAppUserId;
	}

	public void setFromAppUserId(Long fromAppUserId) {
		this.fromAppUserId = fromAppUserId;
	}

	@Column(name="FROM_APP_USER_NAME", length=15)
	public String getFromAppUserName() {
		return fromAppUserName;
	}

	public void setFromAppUserName(String fromAppUserName) {
		this.fromAppUserName = fromAppUserName;
	}

	@Column(name="TO_APP_USER_ID")
	public Long getToAppUserId() {
		return toAppUserId;
	}

	public void setToAppUserId(Long toAppUserId) {
		this.toAppUserId = toAppUserId;
	}

	@Column(name="TO_APP_USER_NAME", length=15)
	public String getToAppUserName() {
		return toAppUserName;
	}

	public void setToAppUserName(String toAppUserName) {
		this.toAppUserName = toAppUserName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT", nullable=false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
		           .append(id)
		           .append(appUser)
		           .append(text)
		           .append(createdDate)
		           .toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		           .append(id)
		           .append(appUser)
		           .append(text)
		           .append(createdDate)
		           .append(fromAppUserId)
		           .append(fromAppUserName)
		           .append(toAppUserId)
		           .append(toAppUserName)
		           .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		AppTweet other = (AppTweet) obj;
		return new EqualsBuilder()
		           .append(id, other.id)
		           .append(appUser, other.appUser)
		           .append(text, other.text)
		           .append(createdDate, other.createdDate)
		           .append(fromAppUserId, other.fromAppUserId)
		           .append(fromAppUserName, other.fromAppUserName)
		           .append(toAppUserId, other.toAppUserId)
		           .append(toAppUserName, other.toAppUserName)
		           .isEquals();
	}

}
