package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description 解析db.serverStatus()执行结果中network参数
 * @date 2022-06-14 14:36
 **/
public class NetworkMetrics extends AbstractMonitor{

    /**
     * @param mongoClient 不能为空
     */
    protected NetworkMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getBytesIn(){
        return getNetworkData("bytesIn",Long.class);
    }

    public Long getBytesOut(){
        return getNetworkData("bytesOut",Long.class);
    }

    public Long getNumRequests(){
        return getNetworkData("numRequests",Long.class);
    }

    private <T> T getNetworkData(String key, Class<T> targetClass) {
        Document mem = (Document) getServerStatus().get("network");
        return (T) mem.get(key,targetClass);
    }
}
