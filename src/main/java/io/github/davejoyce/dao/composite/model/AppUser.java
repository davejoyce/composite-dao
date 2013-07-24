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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Uniquely identified application user.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name="APP_USER")
public class AppUser implements Serializable {

	private static final long serialVersionUID = 1512453493359804850L;

	private String userId = null;
	private Date createdDate = null;
	private boolean active = false;

	public AppUser() {}

	public AppUser(final String userId) {
		this(userId, Calendar.getInstance().getTime());
	}

	public AppUser(final String userId, final Date createdDate) {
		this();
		this.setUserId(userId);
		this.setCreateDate(createdDate);
	}

	@Id
	@Column(name="APP_USER_ID", length=50, nullable=false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String username) {
		Validate.notNull(userId, "userId argument must not be null!");
		this.userId = username;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT", nullable=false, insertable=true, updatable=false)
	public Date getCreateDate() {
		return createdDate;
	}

	public void setCreateDate(Date createdDate) {
		Validate.notNull(createdDate, "createdDate argument must not be null!");
		this.createdDate = createdDate;
	}

	@Column(name="ACTIVE", nullable=false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		           .append(userId)
		           .append(createdDate)
		           .append(active)
		           .toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		final AppUser other = (AppUser) obj;
		return new EqualsBuilder()
		           .append(userId, other.userId)
		           .append(createdDate, other.createdDate)
		           .append(active, other.active)
		           .isEquals();
	}

}
