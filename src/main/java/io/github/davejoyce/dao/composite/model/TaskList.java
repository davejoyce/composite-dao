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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A named list of 0 or more <tt>Task</tt> entries owned by a particular
 * <tt>User</tt>.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name="TASK_LIST")
public class TaskList extends TaskItem {

	private static final long serialVersionUID = -6692300157954479523L;

	/**
	 * Default constructor.
	 */
	public TaskList() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param user
	 * @param itemId
	 * @param title
	 */
	public TaskList(User user, String itemId, String title) {
		super(user, itemId, title);
	}

}
