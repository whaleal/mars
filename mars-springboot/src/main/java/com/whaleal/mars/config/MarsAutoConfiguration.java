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
import com.mongodb.TransactionOptions;
import com.mongodb.client.MongoClient;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.config.transaction.MongoTransactionManager;
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

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author cx
 * @author wh
 * @Date 2020/12/18
 * 配置类，MongoProperties都是在这里获取的
 * 原生的配置了许多Bean，涉及到mars的有
 * @see MongoProperties
 * @see MongoClient
 * @see MongoMappingContext
 *
 *
 * @Configuration(proxyBeanMethods = false) 表明此类是配置类,被Spring管理，proxyBeanMethods设为false，此配置类不会被拦截进⾏CGLIB代理,可以提升性能
 * @ConditionalOnClass({MongoClient.class, Mars.class}) 只有当MongoClient类和Mars类都位于类路径上,才会实例化@Bean生成的对象
 * @EnableConfigurationProperties(MongoProperties.class) 让MongoProperties.class类上使用的 @ConfigurationProperties注解的生效,并且将该类注入到 IOC 容器中,交由 IOC 容器进行管理
 * @AutoConfigureBefore(MongoAutoConfiguration.class) 优先加载当前的配置类，在加载MongoAutoConfiguration.class之前加载
 * @ConditionalOnMissingBean(Mars.class) 仅当当前上下文中没有Mars对象时,才会实例化@Bean生成的对象
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class, Mars.class})
@EnableConfigurationProperties(MongoProperties.class)
@AutoConfigureBefore(MongoAutoConfiguration.class)
@ConditionalOnMissingBean(Mars.class)
public class MarsAutoConfiguration {

    //生成mars对象,交给spring管理
    @Bean
    @ConditionalOnMissingBean({Mars.class})
    public Mars mars( MongoClient client, MongoProperties properties, ApplicationContext applicationContext ) throws ClassNotFoundException {

        //PropertyMapper作用：读取配置文件中的属性注入到Spring上下文中
        PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        String databaseName = properties.getMongoClientDatabase();
        MongoMappingContext context = new MongoMappingContext(client.getDatabase(databaseName));
        //读取配置文件中是否自动创建索引的选项，注入到MongoMappingContext中
        mapper.from(properties.isAutoIndexCreation()).to(context::setAutoIndexCreation);
        //借助spring扫描所有加了@Entity注解的类
        context.setInitialEntitySet(new EntityScanner(applicationContext).scan(Entity.class));
        //从配置文件中获取命名策略，注入到MongoMappingContext中
        Class<?> strategyClass = properties.getFieldNamingStrategy();
        if (strategyClass != null) {
            context.setNamingStrategy(strategyClass);
        }
        return new Mars(client, context);
    }


    @Bean
    @ConditionalOnMissingBean({MongoClient.class})
    public MongoClient mongo(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers, MongoClientSettings settings) {
        return (MongoClient)(new MongoClientFactory((List)builderCustomizers.orderedStream().collect(Collectors.toList()))).createMongoClient(settings);
    }

    @Bean
    @ConditionalOnMissingBean({MongoTransactionManager.class})
    public MongoTransactionManager mongoTransactionManager(MongoMappingContext context, TransactionOptions transactionOptions, MongoClient mongoClient) {
        return (MongoTransactionManager)(new MongoTransactionManager(context,transactionOptions,mongoClient));
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
        MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(MongoProperties properties, Environment environment) {
            return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
        }
    }
}
