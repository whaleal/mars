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
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.core.Mars;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.autoconfigure.mongo.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.stream.Collectors;


/**
 * @author cx
 * @author wh
 * @Date 2020/12/18
 * 配置类，MongoProperties都是在这里获取的
 * 原生的配置了许多Bean，涉及到mars的有
 * 当
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class, Mars.class})
@EnableConfigurationProperties(MongoProperties.class)
@AutoConfigureBefore(MongoAutoConfiguration.class)
@ConditionalOnMissingBean(Mars.class)
public class MarsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({Mars.class})
    public Mars mars( MongoClient client, MongoProperties properties, ApplicationContext applicationContext ) {

        if (Boolean.TRUE.equals(properties.isAutoIndexCreation())) {
            String databaseName = properties.getMongoClientDatabase();
            PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
            MongoMappingContext context = new MongoMappingContext(client.getDatabase(databaseName));
            mapper.from(properties.isAutoIndexCreation()).to(context::setAutoIndexCreation);
            try {
                context.setInitialEntitySet(new EntityScanner(applicationContext).scan(Entity.class));
            } catch (Exception e) {
                context.setAutoIndexCreation(false);
            }
            return new Mars(client, context);
        } else {
            return new Mars(client, properties.getMongoClientDatabase());
        }

    }


    @Bean
    @ConditionalOnMissingBean({MongoClient.class})
    public MongoClient mongo( ObjectProvider< MongoClientSettingsBuilderCustomizer > builderCustomizers, MongoClientSettings settings ) {
        return (MongoClient) (new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))).createMongoClient(settings);
    }

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnMissingBean({MongoClientSettings.class})
    static class MongoClientSettingsConfiguration {
        MongoClientSettingsConfiguration() {
        }

        @Bean
        MongoClientSettings mongoClientSettings() {
            return MongoClientSettings.builder().build();
        }

        @Bean
        MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer( org.springframework.boot.autoconfigure.mongo.MongoProperties properties, Environment environment ) {
            return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
        }
    }

}
