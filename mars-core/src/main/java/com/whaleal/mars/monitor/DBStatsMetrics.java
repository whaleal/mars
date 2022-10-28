package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.core.Mars;
import org.bson.Document;

/**
 * @author lyz
 * @description 解析db.runCommand( {dbStats: 1,scale: <number>,freeStorage: 0} )
 * @date 2022-06-15 15:29
 **/
public class DBStatsMetrics{

    private static Document dbStats;

    /**
     * 如果不提供数据，则默认为test
     *
     * 指定监控指标的单位1->byte 1024->kb
     * @param mongoClient
     */
    public DBStatsMetrics(MongoClient mongoClient){
        this(mongoClient,"test",1);
    }

    public DBStatsMetrics(MongoClient mongoClient,String dbName){
        this(mongoClient,dbName,1);
    }

    public DBStatsMetrics(MongoClient mongoClient,String dbName,int scale){
        dbStats = mongoClient.getDatabase(dbName).runCommand(new Document("dbStats",1).append("scale",scale).append("freeStorage",0));
    }

    public String getDBName(){
        return dbStats.get("db",String.class);
    }

    public Integer getCollectionCount(){
        return dbStats.get("collections",Integer.class);
    }

    public Integer getViewCount(){
        return dbStats.get("views",Integer.class);
    }

    public Integer getObjectCount(){
        return dbStats.get("objects",Integer.class);
    }

    public Double getAvgObjSize(){
        return dbStats.get("avgObjSize",Double.class);
    }

    public Double getDataSize(){
        return dbStats.get("dataSize",Double.class);
    }

    public Double getStorageSize(){
        return dbStats.get("storageSize",Double.class);
    }

    public Integer getFreeStorageSize(){
        return dbStats.get("freeStorageSize",Integer.class);
    }

    public Integer getIndexs(){
        return dbStats.get("indexs",Integer.class);
    }

    public Double getIndexSize(){
        return dbStats.get("indexSize",Double.class);
    }

    public Integer getIndexFreeStorageSize(){
        return dbStats.get("indexFreeStorageSize",Integer.class);
    }

    public Double getTotalSize(){
        return dbStats.get("totalSize",Double.class);
    }

    public Integer getTotalFreeStorageSize(){
        return dbStats.get("totalFreeStorageSize",Integer.class);
    }

    public Double getScaleFactor(){
        return dbStats.get("scaleFactor",Double.class);
    }

    public Double getFsUsedSize(){
        return dbStats.get("fsUsedSize",Double.class);
    }

}
