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
package io.github.davejoyce.dao.composite.social.connect.jpa;

import io.github.davejoyce.dao.composite.model.AppUserSocialConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * JPA 2.0 implementation of Spring Social {@linkplain UsersConnectionRepository}.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class JpaUsersConnectionRepository implements UsersConnectionRepository {

	private static final String QUERY_FIND_USERIDS_WITH_CONNECTION = "SELECT ausc FROM AppUserSocialConnection ausc WHERE ausc.key.providerId = :providerId AND ausc.key.providerUserId = :providerUserId";
	private static final String QUERY_FIND_USERIDS_CONNECTED_TO = "SELECT ausc FROM AppUserSocialConnection ausc WHERE ausc.key.providerId = :providerId AND ausc.key.providerUserId IN (:providerUserIds)";

	private final EntityManager entityManager;
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;
	private final Logger logger;

	public JpaUsersConnectionRepository(final EntityManager entityManager, final ConnectionFactoryLocator connectionFactoryLocator, final TextEncryptor textEncryptor) {
		Validate.notNull(entityManager, "EntityManager argument must not be null!");
		Validate.notNull(connectionFactoryLocator, "ConnectionFactoryLocator argument must not be null!");
		Validate.notNull(textEncryptor, "TextEncryptor argument must not be null!");
		this.entityManager = entityManager;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(QUERY_FIND_USERIDS_WITH_CONNECTION, AppUserSocialConnection.class)
				                                                 .setParameter("providerId", key.getProviderId())
				                                                 .setParameter("providerUserId", key.getProviderUserId());
		logger.debug("Finding application user IDs for provider '{}' and providerUserId '{}'...", key.getProviderId(), key.getProviderUserId());
		List<AppUserSocialConnection> appUserSocialConnections = query.getResultList();
		List<String> userIds = new ArrayList<String>(appUserSocialConnections.size());
		for (AppUserSocialConnection appUserSocialConnection : appUserSocialConnections) {
			userIds.add(appUserSocialConnection.getAppUser().getUserId());
		}
		logger.debug("Found {} application user IDs", Integer.valueOf(userIds.size()));
		return userIds;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(QUERY_FIND_USERIDS_CONNECTED_TO, AppUserSocialConnection.class)
				                                                 .setParameter("providerId", providerId)
				                                                 .setParameter("providerUserIds", providerUserIds);
		List<AppUserSocialConnection> appUserSocialConnections = query.getResultList();
		Set<String> userIds = new HashSet<String>();
		for (AppUserSocialConnection appUserSocialConnection : appUserSocialConnections) {
			userIds.add(appUserSocialConnection.getAppUser().getUserId());
		}
		return userIds;
	}

	/**
	 * {@inheritDoc}
	 */
	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new JpaConnectionRepository(userId, entityManager, connectionFactoryLocator, textEncryptor);
	}

}
