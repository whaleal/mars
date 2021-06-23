/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
 package com.whaleal.mars.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.whaleal.mars.util.Assert;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author cx
 * @Date 2020/12/21
 */
@Configuration
public class CustomerMongoClientSettings implements Customizer {

    private final MongoProperties properties;


    public CustomerMongoClientSettings(MongoProperties properties) {

        this.properties = properties;
    }

    @Override
    public void customize(MongoClientSettings.Builder settingsBuilder) {
        validateConfiguration();
        applyUuidRepresentation(settingsBuilder);
        applyHostAndPort(settingsBuilder);
        applyCredentials(settingsBuilder);
        applyReplicaSet(settingsBuilder);
    }

    private void validateConfiguration() {
        if (hasCustomAddress() || hasCustomCredentials() || hasReplicaSet()) {
            Assert.state(this.properties.getUri() == null,
                    "Invalid mongo configuration, either uri or host/port/credentials/replicaSet must be specified");
        }
    }

    private void applyUuidRepresentation(MongoClientSettings.Builder settingsBuilder) {
        settingsBuilder.uuidRepresentation(this.properties.getUuidRepresentation());
    }

    private void applyHostAndPort(MongoClientSettings.Builder settings) {


        if (hasCustomAddress()) {
            String host = getOrDefault(this.properties.getHost(), "localhost");
            int port = getOrDefault(this.properties.getPort(), MongoProperties.DEFAULT_PORT);
            ServerAddress serverAddress = new ServerAddress(host, port);
            settings.applyToClusterSettings((cluster) -> cluster.hosts(Collections.singletonList(serverAddress)));
            return;
        }

        settings.applyConnectionString(new ConnectionString(this.properties.determineUri()));
    }

    private void applyCredentials(MongoClientSettings.Builder builder) {
        if (hasCustomCredentials()) {
            String database = (this.properties.getAuthenticationDatabase() != null)
                    ? this.properties.getAuthenticationDatabase() : this.properties.getMongoClientDatabase();
            builder.credential((MongoCredential.createCredential(this.properties.getUsername(), database,
                    this.properties.getPassword())));
        }
    }

    private void applyReplicaSet(MongoClientSettings.Builder builder) {
        if (hasReplicaSet()) {
            builder.applyToClusterSettings(
                    (cluster) -> cluster.requiredReplicaSetName(this.properties.getReplicaSetName()));
        }
    }

    private <V> V getOrDefault(V value, V defaultValue) {
        return (value != null) ? value : defaultValue;
    }


    private boolean hasCustomCredentials() {
        return this.properties.getUsername() != null && this.properties.getPassword() != null;
    }

    private boolean hasCustomAddress() {
        return this.properties.getHost() != null || this.properties.getPort() != null;
    }

    private boolean hasReplicaSet() {
        return this.properties.getReplicaSetName() != null;
    }


}
