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
import org.bson.UuidRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;

/**
 * @author cx
 * @Date 2020/12/18
 */
@ConfigurationProperties(prefix = MongoProperties.MYBATIS_PREFIX)
public class MongoProperties {

    public static final String MYBATIS_PREFIX = "mars.data.mongodb";

    /**
     * Default port used when the configured port is {@code null}.
     */
    public static final int DEFAULT_PORT = 27017;

    /**
     * Default URI used when the configured URI is {@code null}.
     */
    public static final String DEFAULT_URI = "mongodb://localhost/test";

    /**
     * Mongo server host. Cannot be set with URI.
     */
    private String host;

    /**
     * Mongo server port. Cannot be set with URI.
     */
    private Integer port = null;

    /**
     * Mongo database URI. Cannot be set with host, port, credentials and replica set
     * name.
     */
    private String uri;

    /**
     * Database name.
     */
    private String database;

    /**
     * Authentication database name.
     */
    private String authenticationDatabase;

    /**
     * GridFS database name.
     */
    private String bucket;

    /**
     * Login user of the mongo server. Cannot be set with URI.
     */
    private String username;

    /**
     * Login password of the mongo server. Cannot be set with URI.
     */
    private char[] password;

    /**
     * Required replica set name for the cluster. Cannot be set with URI.
     */
    private String replicaSetName;

    /**
     * Fully qualified name of the FieldNamingStrategy to use.
     */
    private Class<?> fieldNamingStrategy;

    /**
     * Representation to use when converting a UUID to a BSON binary value.
     */
    private UuidRepresentation uuidRepresentation = UuidRepresentation.JAVA_LEGACY;

    /**
     * Whether to enable auto-index creation.
     */
    private Boolean autoIndexCreation;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAuthenticationDatabase() {
        return this.authenticationDatabase;
    }

    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getReplicaSetName() {
        return this.replicaSetName;
    }

    public void setReplicaSetName(String replicaSetName) {
        this.replicaSetName = replicaSetName;
    }

    public Class<?> getFieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public void setFieldNamingStrategy(Class<?> fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
    }

    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }

    public void setUuidRepresentation(UuidRepresentation uuidRepresentation) {
        this.uuidRepresentation = uuidRepresentation;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String determineUri() {
        return (this.uri != null) ? this.uri : DEFAULT_URI;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getMongoClientDatabase() {
        if (this.database != null) {
            return this.database;
        }
        return new ConnectionString(determineUri()).getDatabase();
    }

    public Boolean isAutoIndexCreation() {
        return this.autoIndexCreation;
    }

    public void setAutoIndexCreation(Boolean autoIndexCreation) {
        this.autoIndexCreation = autoIndexCreation;
    }

    @Override
    public String toString() {
        return "MongoProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", uri='" + uri + '\'' +
                ", database='" + database + '\'' +
                ", authenticationDatabase='" + authenticationDatabase + '\'' +
                ", bucket='" + bucket + '\'' +
                ", username='" + username + '\'' +
                ", password=" + Arrays.toString(password) +
                ", replicaSetName='" + replicaSetName + '\'' +
                ", fieldNamingStrategy=" + fieldNamingStrategy +
                ", autoIndexCreation=" + autoIndexCreation +
                '}';
    }
}
