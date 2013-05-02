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
package io.github.davejoyce.dao.composite.repository;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass of entity type-specific <em>composite</em> repositories.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public abstract class AbstractCompositeBasicCrudRepository<T, ID extends Serializable>
	implements BasicCrudRepository<T, ID> {

	protected final Logger logger;

	private BasicCrudRepository<T, ID> firstRepository = null,
			                           secondRepository = null;

	/**
	 * Construct an empty composite repository. Initializes the logging
	 * facility.
	 * <p><strong>NOTE:</strong> The repositories of which this object is
	 * composed must be set prior to invocation of methods on this object.</p>
	 */
	public AbstractCompositeBasicCrudRepository() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * Construct a composite repository, composed of the two specified
	 * repositories. Initializes the logging facility.
	 * 
	 * @param firstRepository first repository to be considered
	 * @param secondRepository second repository to be considered
	 */
	public AbstractCompositeBasicCrudRepository(BasicCrudRepository<T, ID> firstRepository,
			                                    BasicCrudRepository<T, ID> secondRepository) {
		this();
		this.setFirstRepository(firstRepository);
		this.setSecondRepository(secondRepository);
		logger.info("Initialized with 2 component repositories [first = {}, second = {}]",
				    this.firstRepository.getClass().getName(),
				    this.secondRepository.getClass().getName());
	}

	/**
	 * Set the first component repository.
	 * 
	 * @param firstRepository first repository to be considered
	 * @throws IllegalArgumentException if repository argument is {@literal null}
	 */
	public void setFirstRepository(BasicCrudRepository<T, ID> firstRepository) {
		Validate.notNull(firstRepository, "First repository argument cannot be null");
		this.firstRepository = firstRepository;
		logger.debug("First component repository: {}", firstRepository.getClass().getName());
	}

	/**
	 * Set the second component repository.
	 * 
	 * @param secondRepository second repository to be considered
	 * @throws IllegalArgumentException if repository argument is {@literal null}
	 */
	public void setSecondRepository(BasicCrudRepository<T, ID> secondRepository) {
		Validate.notNull(secondRepository, "Second repository argument cannot be null");
		this.secondRepository = secondRepository;
		logger.debug("Second component repository: {}", secondRepository.getClass().getName());
	}

}
