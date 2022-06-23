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

    private MongoClient mongoClient;

    private String dbName;

    //指定监控指标的单位1->byte 1024->kb
    private int scale;

    /**
     * 如果不提供数据，则默认为test
     * @param mongoClient
     */
    public DBStatsMetrics(MongoClient mongoClient){
        this.mongoClient = mongoClient;
        this.scale = 1;
        this.dbName = "test";
    }

    public DBStatsMetrics(MongoClient mongoClient,String dbName){
        this.mongoClient = mongoClient;
        this.scale = 1;
        this.dbName = dbName;
    }

    public DBStatsMetrics(MongoClient mongoClient,String dbName,int scale){
        this.mongoClient = mongoClient;
        this.dbName = dbName;
        this.scale = scale;
    }

    public String getDBName(){
        return dbName;
    }

    public Integer getCollectionCount(){
        return getDbBStatsData("collections",Integer.class);
    }

    public Integer getViewCount(){
        return getDbBStatsData("views",Integer.class);
    }

    public Integer getObjectCount(){
        return getDbBStatsData("objects",Integer.class);
    }

    public Double getAvgObjSize(){
        return getDbBStatsData("avgObjSize",Double.class);
    }

    public Double getDataSize(){
        return getDbBStatsData("dataSize",Double.class);
    }

    public Double getStorageSize(){
        return getDbBStatsData("storageSize",Double.class);
    }

    public Integer getFreeStorageSize(){
        return getDbBStatsData("freeStorageSize",Integer.class);
    }

    public Integer getIndexs(){
        return getDbBStatsData("indexs",Integer.class);
    }

    public Double getIndexSize(){
        return getDbBStatsData("indexSize",Double.class);
    }

    public Integer getIndexFreeStorageSize(){
        return getDbBStatsData("indexFreeStorageSize",Integer.class);
    }

    public Double getTotalSize(){
        return getDbBStatsData("totalSize",Double.class);
    }

    public Integer getTotalFreeStorageSize(){
        return getDbBStatsData("totalFreeStorageSize",Integer.class);
    }

    public Double getScaleFactor(){
        return getDbBStatsData("scaleFactor",Double.class);
    }

    public Double getFsUsedSize(){
        return getDbBStatsData("fsUsedSize",Double.class);
    }

    private <T> T getDbBStatsData(String key,Class<T> targetClass){
        Document dbStats = mongoClient.getDatabase(dbName).runCommand(new Document("dbStats",1).append("scale",scale).append("freeStorage",0));

        return (T) dbStats.get(key,targetClass);
    }
}
