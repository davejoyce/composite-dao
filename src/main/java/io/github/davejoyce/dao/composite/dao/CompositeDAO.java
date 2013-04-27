package io.github.davejoyce.dao.composite.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass of type-specific composite data access objects. This
 * class and its subclasses are the central focus of this application's API.
 * <p>Provides a logging facility for objects which extend this class.</p>
 * 
 * @param <T> Persistent entity type
 * @param <I> Unique identifier type
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class CompositeDAO<T,I> implements DAO<T,I> {

	protected final Logger logger;
	private final DAO<T,I> firstDAO, secondDAO;

	public CompositeDAO(DAO<T,I> firstDAO, DAO<T,I> secondDAO) {
		this.firstDAO = firstDAO;
		this.secondDAO = secondDAO;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * @return the first component DAO
	 */
	public final DAO<T,I> getFirstDAO() {
		return firstDAO;
	}

	/**
	 * @return the second component DAO
	 */
	public final DAO<T,I> getSecondDAO() {
		return secondDAO;
	}

}
