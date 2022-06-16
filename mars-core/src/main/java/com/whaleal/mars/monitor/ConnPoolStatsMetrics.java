package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description 解析db.runCommand( { "connPoolStats" : 1 } )得到的ConnPoolStats基本信息
 * @date 2022-06-14 20:27
 **/
public class ConnPoolStatsMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected ConnPoolStatsMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public int getClientConnections(){
        return (int)getConnStatsData("numClientConnections");
    }

    public int getAScopedConnections(){
        return (int) getConnStatsData("numAScopedConnections");
    }

    public int getTotalInUse(){
        return (int) getConnStatsData("totalInUse");
    }

    public int getTotalAvailable(){
        return (int) getConnStatsData("totalAvailable");
    }

    public int getTotalCreated(){
        return (int) getConnStatsData("totalCreated");
    }

    public int getTotalRefreshing(){
        return (int) getConnStatsData("totalRefreshing");
    }

    public String getStrategy(){
        return (String) getConnStatsData("replicaSetMatchingStrategy");
    }

    private <T> T getConnStatsData(String key){
        return (T)getConnPoolStats().get(key);
    }

    public Document getPoolsGlobal(){
        return getPoolStatsData("global",Document.class);
    }

    private <T> T getPoolStatsData(String key,Class<T> targetClass){
        Document pools = getConnPoolStats().get("pools", Document.class);

        return (T) pools.get(key,targetClass);
    }

    public  Document getReplicaSetData(){
        return getConnPoolStats().get("replicaSets", Document.class);
    }

}
