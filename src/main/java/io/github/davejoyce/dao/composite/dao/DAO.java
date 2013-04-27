/**
 * 
 */
package io.github.davejoyce.dao.composite.dao;

import io.github.davejoyce.dao.composite.Criteria;

import java.util.List;

/**
 * Defines basic behavior of type-specific data access objects (DAO).
 * 
 * @param <T> Persistent entity type
 * @param <I> Unique identifier type
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface DAO<T,I> {

	public T find(I id);

	public List<T> search(Criteria criteria);

	public T insert(T t);

	public T update(T t);

	public void delete(T t);

}
