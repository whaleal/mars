package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.core.Mars;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-15 15:29
 **/
public class DBStatsMetrics extends AbstractMonitor{

    private String dbName;

    private int scale;

    public DBStatsMetrics(MongoClient mongoClient,int scale){
        super(mongoClient);
        this.scale = scale;
        this.dbName = "test";
    }

    public DBStatsMetrics(MongoClient mongoClient,String dbName,int scale){
        super(mongoClient);
        this.dbName = dbName;
        this.scale = scale;
    }

    public String getDBName(){
        return dbName;
    }

    public int getCollectionCount(){
        return getDbBStatsData("collections",Integer.class);
    }

    public int getViewCount(){
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
        Document dbStats = (Document)getDb(dbName).runCommand(new Document("dbStats",1).append("scale",scale).append("freeStorage",0));

        return (T) dbStats.get(key,targetClass);
    }
}
