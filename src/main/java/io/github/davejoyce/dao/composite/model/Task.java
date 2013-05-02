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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A single activity to be accomplished by a <tt>User</tt>.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@Entity
@Table(name = "TASK")
public class Task extends TaskItem {

	private static final long serialVersionUID = 323281870423956674L;

	private TaskItem parent;
	private String notes;
	private Date due;
	private String position;
	private Date updated;
	private TaskStatus status;
	private Date completed;

	/**
	 * 
	 */
	public Task() {
		super();
		this.status = TaskStatus.NEEDS_ACTION;
	}

	/**
	 * @param user
	 * @param itemId
	 * @param title
	 */
	public Task(User user, String itemId, String title) {
		super(user, itemId, title);
		this.status = TaskStatus.NEEDS_ACTION;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne(optional=false)
	@JoinColumns({
		@JoinColumn(name="PARENT_USER_ID", referencedColumnName="USER_ID"),
		@JoinColumn(name="PARENT_ITEM_ID", referencedColumnName="ITEM_ID")
	})
	public TaskItem getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(TaskItem parent) {
		this.parent = parent;
	}

	/**
	 * @return the notes
	 */
	@Column(name="NOTES", length=1024)
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the due
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="DUE_DT")
	public Date getDue() {
		return due;
	}

	/**
	 * @param due the due to set
	 */
	public void setDue(Date due) {
		this.due = due;
	}

	/**
	 * @return the position
	 */
	@Column(name="POSITION")
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the updated
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DT", nullable=false)
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the status
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="STATUS", nullable=false)
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	/**
	 * @return the completed
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="COMPLETED_DT")
	public Date getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Date completed) {
		this.completed = completed;
		if (null != completed) {
			setStatus(TaskStatus.COMPLETED);
		}
	}

}
