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
        return getNetworkData("bytesIn");
    }

    public Long getBytesOut(){
        return getNetworkData("bytesOut");
    }

    public Long getNumRequests(){
        return getNetworkData("numRequests");
    }

    private Long getNetworkData(String key) {
        Document mem = (Document) serverStatus.get("network");
        return mem.getLong(key);
    }
}
