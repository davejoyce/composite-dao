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

import io.github.davejoyce.dao.composite.model.AppUser;
import io.github.davejoyce.dao.composite.model.AppUserSocialConnection;
import io.github.davejoyce.dao.composite.model.AppUserSocialConnectionKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * JPA 2.0 implementation of Spring Social {@linkplain ConnectionRepository}.
 * 
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
class JpaConnectionRepository implements ConnectionRepository {

	private static final String QUERY_SELECT_FROM = "SELECT ausc FROM AppUserSocialConnection ausc ";
	private static final String QUERY_FIND_ALL_CONNECTIONS = QUERY_SELECT_FROM + "WHERE ausc.key.appUser.userId = :userId ORDER BY ausc.key.providerId, ausc.rank";
	private static final String QUERY_FIND_CONNECTIONS = QUERY_SELECT_FROM + "WHERE ausc.key.appUser.userId = :userId AND ausc.key.providerId = :providerId ORDER BY ausc.rank";
	private static final String QUERY_FIND_PRIMARY_CONNECTION = QUERY_SELECT_FROM + "WHERE ausc.key.appUser.userId = :userId AND ausc.key.providerId = :providerId AND ausc.rank = 1";
	private static final String QUERY_NEXT_RANK = "SELECT COALESCE((MAX(ausc.rank) + 1), 1) AS rank FROM AppUserSocialConnection ausc WHERE ausc.key.appUser.userId = :userId AND ausc.key.providerId = :providerId";
	private static final String DELETE_CONNECTIONS = "DELETE FROM AppUserSocialConnection ausc WHERE ausc.key.appUser.userId = :userId AND ausc.key.providerId = :providerId";

	private final String userId;
	private final EntityManager entityManager;	
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;
	private final ServiceProviderConnectionTransformer transformer = new ServiceProviderConnectionTransformer();

	public JpaConnectionRepository(final String userId,
			                       final EntityManager entityManager,
			                       final ConnectionFactoryLocator connectionFactoryLocator,
			                       final TextEncryptor textEncryptor) {
		this.userId = userId;
		this.entityManager = entityManager;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	/**
	 * {@inheritDoc}
	 */
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(QUERY_FIND_ALL_CONNECTIONS, AppUserSocialConnection.class)
				                                                 .setParameter("userId", userId);
		List<Connection<?>> resultList = appUserSocialConnectionsToConnections(query.getResultList());
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
		}
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			if (connections.get(providerId).size() == 0) {
				connections.put(providerId, new LinkedList<Connection<?>>());
			}
			connections.add(providerId, connection);
		}
		return connections;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Connection<?>> findConnections(String providerId) {
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(QUERY_FIND_CONNECTIONS, AppUserSocialConnection.class)
                                                                 .setParameter("userId", userId)
                                                                 .setParameter("providerId", providerId);
		List<Connection<?>> resultList = appUserSocialConnectionsToConnections(query.getResultList());
		return resultList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	/**
	 * {@inheritDoc}
	 */
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}
		StringBuilder providerUsersCriteriaJpaQl = new StringBuilder(QUERY_SELECT_FROM)
		                                               .append("WHERE ausc.key.appUser.userId = :userId")
		                                               .append(" AND ");
		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<String>> entry = it.next();
			String providerId = entry.getKey();
			providerUsersCriteriaJpaQl.append("ausc.key.providerId = :providerId_").append(providerId)
			                          .append("AND ausc.key.providerUserId IN (:providerUserIds_").append(providerId).append(")");
			if (it.hasNext()) {
				providerUsersCriteriaJpaQl.append(" OR ");
			}
		}
		providerUsersCriteriaJpaQl.append(" ORDER BY ausc.key.providerId, ausc.rank");
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(providerUsersCriteriaJpaQl.toString(), AppUserSocialConnection.class)
				                                                 .setParameter("userId", userId);
		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<String>> entry = it.next();
			String providerId = entry.getKey();
			query.setParameter(("providerId_" + providerId), providerId)
			     .setParameter(("providerUserIds_" + providerId), entry.getValue());
		}
		List<Connection<?>> resultList = appUserSocialConnectionsToConnections(query.getResultList());
		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			List<String> userIds = providerUsers.get(providerId);
			List<Connection<?>> connections = connectionsForUsers.get(providerId);
			if (connections == null) {
				connections = new ArrayList<Connection<?>>(userIds.size());
				for (int i = 0; i < userIds.size(); i++) {
					connections.add(null);
				}
				connectionsForUsers.put(providerId, connections);
			}
			String providerUserId = connection.getKey().getProviderUserId();
			int connectionIndex = userIds.indexOf(providerUserId);
			connections.set(connectionIndex, connection);
		}
		return connectionsForUsers;
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		AppUser appUser = entityManager.find(AppUser.class, userId);
		AppUserSocialConnectionKey primaryKey = new AppUserSocialConnectionKey(appUser, connectionKey.getProviderId(), connectionKey.getProviderUserId());
		AppUserSocialConnection appUserSocialConnection = entityManager.find(AppUserSocialConnection.class, primaryKey);
		return transformer.transform(appUserSocialConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	/**
	 * {@inheritDoc}
	 */
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		Connection<A> connection = findPrimaryConnection(apiType);
		if (null == connection) {
			throw new NotConnectedException(getProviderId(apiType));
		}
		return connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public void addConnection(Connection<?> connection) {
		try {
			ConnectionData cd = connection.createData();
			// Get next rank, default to 1
			TypedQuery<Integer> nextRankQuery = entityManager.createQuery(QUERY_NEXT_RANK, Integer.class)
					                                         .setParameter("userId", userId)
					                                         .setParameter("providerId", cd.getProviderId());
			Integer nextRank = nextRankQuery.getSingleResult();
			// Build & persist new AppUserSocialConnection object
			AppUser appUser = entityManager.find(AppUser.class, userId);
			AppUserSocialConnection ausc = new AppUserSocialConnection();
			ausc.setKey(new AppUserSocialConnectionKey(appUser, cd.getProviderId(), cd.getProviderUserId()));
			ausc.setRank(nextRank.intValue());
			ausc.setDisplayName(cd.getDisplayName());
			ausc.setProfileUrl(cd.getProfileUrl());
			ausc.setImageUrl(cd.getImageUrl());
			ausc.setAccessToken(encrypt(cd.getAccessToken()));
			ausc.setSecret(encrypt(cd.getSecret()));
			ausc.setRefreshToken(encrypt(cd.getRefreshToken()));
			ausc.setExpireTime(cd.getExpireTime());
			entityManager.persist(ausc);
		} catch (EntityExistsException eee) {
			throw new DuplicateConnectionException(connection.getKey());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateConnection(Connection<?> connection) {
		ConnectionData cd = connection.createData();
		AppUserSocialConnectionKey primaryKey = new AppUserSocialConnectionKey(findAppUser(), cd.getProviderId(), cd.getProviderUserId());
		AppUserSocialConnection ausc = entityManager.find(AppUserSocialConnection.class, primaryKey);
		ausc.setDisplayName(cd.getDisplayName());
		ausc.setProfileUrl(cd.getProfileUrl());
		ausc.setImageUrl(cd.getImageUrl());
		ausc.setAccessToken(encrypt(cd.getAccessToken()));
		ausc.setSecret(encrypt(cd.getSecret()));
		ausc.setRefreshToken(encrypt(cd.getRefreshToken()));
		ausc.setExpireTime(cd.getExpireTime());
		entityManager.merge(ausc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConnections(String providerId) {
		Query deleteQuery = entityManager.createQuery(DELETE_CONNECTIONS)
				                         .setParameter("userId", userId)
				                         .setParameter("providerId", providerId);
		deleteQuery.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConnection(ConnectionKey connectionKey) {
		AppUserSocialConnectionKey primaryKey = new AppUserSocialConnectionKey(findAppUser(), connectionKey.getProviderId(), connectionKey.getProviderUserId());
		AppUserSocialConnection ausc = entityManager.find(AppUserSocialConnection.class, primaryKey);
		if (null != ausc) {
			entityManager.remove(ausc);
		}
	}

	private List<Connection<?>> appUserSocialConnectionsToConnections(List<AppUserSocialConnection> appUserSocialConnections) {
		List<Connection<?>> connections = new ArrayList<Connection<?>>(appUserSocialConnections.size());
		for (AppUserSocialConnection appUserSocialConnection : appUserSocialConnections) {
			connections.add(transformer.transform(appUserSocialConnection));
		}
		return connections;
	}

	private AppUser findAppUser() {
		return entityManager.find(AppUser.class, userId);
	}

	private Connection<?> findPrimaryConnection(String providerId) {
		TypedQuery<AppUserSocialConnection> query = entityManager.createQuery(QUERY_FIND_PRIMARY_CONNECTION, AppUserSocialConnection.class)
				                                                 .setParameter("userId", userId)
				                                                 .setParameter("providerId", providerId);
		List<AppUserSocialConnection> appUserSocialConnections = query.getResultList();
		if (0 < appUserSocialConnections.size()) {
			return transformer.transform(appUserSocialConnections.get(0));
		} else {
			return null;
		}
	}

	private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}
	
	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

	private final class ServiceProviderConnectionTransformer {
	
		public Connection<?> transform(AppUserSocialConnection ausc) {
			ConnectionData cd = mapConnectionData(ausc);
			ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(cd.getProviderId());
			return connectionFactory.createConnection(cd);
		}
	
		private ConnectionData mapConnectionData(AppUserSocialConnection ausc) {
			return new ConnectionData(ausc.getProviderId(),
					                  ausc.getProviderUserId(),
					                  ausc.getDisplayName(),
					                  ausc.getProfileUrl(),
					                  ausc.getImageUrl(),
					                  decrypt(ausc.getAccessToken()),
					                  decrypt(ausc.getSecret()),
					                  decrypt(ausc.getRefreshToken()),
					                  ausc.getExpireTime());
		}
	
		private String decrypt(String encryptedText) {
			return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
		}
	}

}
