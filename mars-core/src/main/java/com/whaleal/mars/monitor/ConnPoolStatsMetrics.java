package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.icefrog.core.util.ObjectUtil;
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

    public Integer getClientConnections(){
        return getConnStatsData("numClientConnections",Integer.class);
    }

    public Integer getAScopedConnections(){
        return getConnStatsData("numAScopedConnections",Integer.class);
    }

    public Integer getTotalInUse(){
        return  getConnStatsData("totalInUse",Integer.class);
    }

    public Integer getTotalAvailable(){
        return  getConnStatsData("totalAvailable",Integer.class);
    }

    public Integer getTotalCreated(){
        return getConnStatsData("totalCreated",Integer.class);
    }

    public Integer getTotalRefreshing(){
        return getConnStatsData("totalRefreshing",Integer.class);
    }

    public String getStrategy(){
        return  getConnStatsData("replicaSetMatchingStrategy",String.class);
    }

    private <T> T getConnStatsData(String key,Class<T> targetClass){
        return connPoolStats.get(key,targetClass);
    }

    public Document getPoolsGlobal(){
        Document pools = connPoolStats.get("pools", Document.class);
        if(ObjectUtil.isEmpty(pools)){
            return null;
        }
        return pools.get("global",Document.class);
    }

//    private <T> T getPoolStatsData(String key,Class<T> targetClass){
//        Document pools = connPoolStats.get("pools", Document.class);
//
//        return (T) pools.get(key,targetClass);
//    }

    public  Document getReplicaSetData(){
        return connPoolStats.get("replicaSets", Document.class);
    }

}
