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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Concrete parent class of <tt>TaskList</tt> and <tt>Task</tt> entities. Common
 * persistent attributes are maintained here - namely the unique composite key
 * and required title.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@IdClass(TaskItemId.class)
@Table(name="TASK_ITEM")
public class TaskItem implements Serializable {

	private static final long serialVersionUID = 5002710078782185114L;

	private String itemId;
	private String title;
	private User user;

	/**
	 * Default constructor.
	 */
	public TaskItem() {}

	/**
	 * Constructor.
	 * 
	 * @param user
	 * @param itemId
	 * @param title
	 */
	public TaskItem(User user, String itemId, String title) {
		this.user = user;
		this.itemId = itemId;
		this.title = title;
	}

	/**
	 * @return the user
	 */
	@Id
	@ManyToOne(optional=false)
	@JoinColumn(name="USER_ID", nullable=false, updatable=false)
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the itemId
	 */
	@Id
	@Column(name="ITEM_ID", nullable=false)
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the title
	 */
	@Column(name="TITLE", nullable=false)
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
	}

}
