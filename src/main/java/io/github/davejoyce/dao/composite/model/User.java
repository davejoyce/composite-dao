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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Uniquely identified application user.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name="APP_USER")
public class User {

	private String id;

	public User() {}

	public User(final String id) {
		this.id = id;
	}

	/**
	 * @return the itemId
	 */
	@Id
	@Column(name="ID", nullable=false,insertable=true,updatable=false)
	public String getId() {
		return id;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
