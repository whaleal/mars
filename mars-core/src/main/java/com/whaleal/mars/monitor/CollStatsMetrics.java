package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.Document;

import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-15 11:22
 **/
public class CollStatsMetrics extends AbstractMonitor{

    private final String dbName;

    private final String collectionName;

    /**
     * @param
     */

    public CollStatsMetrics(MongoClient mongoClient,String dbName,String collectionName) {
        super(mongoClient);
        this.dbName = dbName;
        this.collectionName = collectionName;
    }

    public String getNS(){
        return getCollStats("ns",String.class);
    }

    public int getSize(){
        return getCollStats("size",Integer.class);
    }

    public int getCount(){
        return getCollStats("count",Integer.class);
    }

    public int getAvgObjSize(){
        return getCollStats("avgObjSize",Integer.class);
    }

    public int getStorageSize(){
        return getCollStats("freeStorageSize",Integer.class);
    }

    public Boolean isCapped(){
        return getCollStats("capped",Boolean.class);
    }

    public Integer getMax(){
        return getCollStats("max", Integer.class);
    }

    public Integer getMaxSize(){
        return getCollStats("maxSize", Integer.class);
    }

    public int getScaleFactor(){
        return getCollStats("scaleFactor",Integer.class);
    }


    public int getNIndexes(){
        return getCollStats("nindexes",Integer.class);
    }

    public List<String> getIndexBuilds(){
        return (List<String>) getCollStats("indexBuilds",List.class);
    }

    public Document getIndexStats(){
        return getCollStats("indexDetails",Document.class);
    }

    public int getTotalIndexSize(){
        return getCollStats("totalIndexSize",Integer.class);
    }

    public int getTotalSize(){
        return getCollStats("totalSize",Integer.class);
    }

    public Document getIndexSize(){
        return getCollStats("indexSizes",Document.class);
    }

    protected <T> T getCollStats(String key,Class<T> targetClass){
        Document document = getDb(dbName).runCommand(new Document("collStats",collectionName));
        if(ObjectUtil.isEmpty(document)){
            return null;
        }
        return (T) document.get(key,targetClass);
    }


    protected  <T> T getTimeSeriesData(String key,Class<T> targetClass){
        Document timeSeries = getCollStats("timeseries",Document.class);
        if(ObjectUtil.isEmpty(timeSeries)){
            return null;
        }

        return (T)timeSeries.get(key,targetClass);
    }
}
