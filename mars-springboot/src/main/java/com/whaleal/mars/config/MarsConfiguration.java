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

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.core.Mars;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author cx
 * @Date 2020/12/18
 * 配置类，MongoProperties都是在这里获取的
 * 原生的配置了许多Bean，涉及到mars的有
 *
 */

@EnableConfigurationProperties(MongoProperties.class)
public class MarsConfiguration {

    @Bean
    public Mars mars(MongoProperties properties) {
        return new Mars(mongoClient(properties), properties.getMongoClientDatabase());
    }

    private MongoClient mongoClient(MongoProperties properties) {

        MongoClientSettings.Builder builder = MongoClientSettings.builder();

        Customizer customizer = new

                CustomerMongoClientSettings(properties);

        customizer.customize(builder);

        MongoClientSettings build = builder.build();

        MongoClient mongoClient = MongoClients.create(build);

        return mongoClient;
    }

}
