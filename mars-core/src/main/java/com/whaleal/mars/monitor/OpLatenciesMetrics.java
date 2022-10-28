package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

import javax.print.Doc;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 14:57
 **/
public class OpLatenciesMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected OpLatenciesMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getReadsLatency(){
        return getOpLatenciesData("reads");
    }

    public Long getWritesLatency(){
        return getOpLatenciesData("writes");
    }

    public Long getCommandsLatency(){
        return getOpLatenciesData("commands");
    }

    public Long getTransactionsLatency(){
        return getOpLatenciesData("transactions");
    }

    private Long getOpLatenciesData(String key) {
        Document mem = (Document) serverStatus.get("opLatencies",Document.class).get(key);

        return mem.get("latency",Long.class);
    }
}
