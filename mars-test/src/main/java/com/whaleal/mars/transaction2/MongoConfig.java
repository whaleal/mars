/*
package com.whaleal.mars.transaction2;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.Constant;
import com.whaleal.mars.config.transaction.MongoTransactionManager;
import com.whaleal.mars.core.Mars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

*/
/**
 * @author wh
 *//*

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {



    @Override
    protected String getDatabaseName() {
        return "wh";
    }

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(Constant.connectionStr);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
*/
