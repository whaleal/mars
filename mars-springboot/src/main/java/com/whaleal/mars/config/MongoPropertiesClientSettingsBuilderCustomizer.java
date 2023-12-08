/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.config;

import java.util.Collections;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * A {@link MongoClientSettingsBuilderCustomizer} that applies properties from a
 * {@link MongoProperties} to a {@link MongoClientSettings}.
 *
 *  @author wh
 */
public class MongoPropertiesClientSettingsBuilderCustomizer implements MongoClientSettingsBuilderCustomizer , Ordered {

	private final MongoProperties properties;

	private final Environment environment;

	private int order = 0;

	public MongoPropertiesClientSettingsBuilderCustomizer( MongoProperties properties, Environment environment) {
		this.properties = properties;
		this.environment = environment;
	}

	@Override
	public void customize(MongoClientSettings.Builder settingsBuilder) {
		applyUuidRepresentation(settingsBuilder);
		applyHostAndPort(settingsBuilder);
		applyCredentials(settingsBuilder);
		applyReplicaSet(settingsBuilder);
	}

	private void applyUuidRepresentation(MongoClientSettings.Builder settingsBuilder) {
		settingsBuilder.uuidRepresentation(this.properties.getUuidRepresentation());
	}

	private void applyHostAndPort(MongoClientSettings.Builder settings) {
		if (getEmbeddedPort() != null) {
			settings.applyConnectionString(new ConnectionString("mongodb://localhost:" + getEmbeddedPort()));
			return;
		}
		if (this.properties.getUri() != null) {
			settings.applyConnectionString(new ConnectionString(this.properties.getUri()));
			return;
		}
		if (this.properties.getHost() != null || this.properties.getPort() != null) {
			String host = getOrDefault(this.properties.getHost(), "localhost");
			int port = getOrDefault(this.properties.getPort(), MongoProperties.DEFAULT_PORT);
			ServerAddress serverAddress = new ServerAddress(host, port);
			settings.applyToClusterSettings((cluster) -> cluster.hosts(Collections.singletonList(serverAddress)));
			return;
		}
		settings.applyConnectionString(new ConnectionString(MongoProperties.DEFAULT_URI));
	}

	private void applyCredentials(MongoClientSettings.Builder builder) {
		if (this.properties.getUri() == null && this.properties.getUsername() != null
				&& this.properties.getPassword() != null) {
			String database = (this.properties.getAuthenticationDatabase() != null)
					? this.properties.getAuthenticationDatabase() : this.properties.getMongoClientDatabase();
			builder.credential((MongoCredential.createCredential(this.properties.getUsername(), database,
					this.properties.getPassword())));
		}
	}

	private void applyReplicaSet(MongoClientSettings.Builder builder) {
		if (this.properties.getReplicaSetName() != null) {
			builder.applyToClusterSettings(
					(cluster) -> cluster.requiredReplicaSetName(this.properties.getReplicaSetName()));
		}
	}

	private <V> V getOrDefault(V value, V defaultValue) {
		return (value != null) ? value : defaultValue;
	}

	private Integer getEmbeddedPort() {
		if (this.environment != null) {
			String localPort = this.environment.getProperty("local.mongo.port");
			if (localPort != null) {
				return Integer.valueOf(localPort);
			}
		}
		return null;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	/**
	 * Set the order value of this object.
	 * @param order the new order value
	 * @see #getOrder()
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
