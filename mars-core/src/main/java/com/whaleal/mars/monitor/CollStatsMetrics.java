package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.Document;

import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-15 11:22
 **/
public class CollStatsMetrics extends AbstractMonitor{

    private static Document collStats;

    /**
     * @param
     */

    public CollStatsMetrics(MongoClient mongoClient,MongoDatabase db,String collectionName) {
        super(mongoClient);
        collStats = db.runCommand(new Document("collStats",collectionName));
    }

    public String getNS(){
        return getCollStats("ns",String.class);
    }

    public Integer getSize(){
        return getCollStats("size",Integer.class);
    }

    public Integer getCount(){
        return getCollStats("count",Integer.class);
    }

    public Integer getAvgObjSize(){
        return getCollStats("avgObjSize",Integer.class);
    }

    public Integer getStorageSize(){
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

    public Integer getScaleFactor(){
        return getCollStats("scaleFactor",Integer.class);
    }


    public Integer getNIndexes(){
        return getCollStats("nindexes",Integer.class);
    }

    public List<String> getIndexBuilds(){
        return (List<String>) getCollStats("indexBuilds",List.class);
    }

    public Document getIndexStats(){
        return getCollStats("indexDetails",Document.class);
    }

    public Integer getTotalIndexSize(){
        return getCollStats("totalIndexSize",Integer.class);
    }

    public Integer getTotalSize(){
        return getCollStats("totalSize",Integer.class);
    }

    public Document getIndexSize(){
        return getCollStats("indexSizes",Document.class);
    }

    protected <T> T getCollStats(String key,Class<T> targetClass){
        if(ObjectUtil.isEmpty(collStats)){
            return null;
        }
        return (T) collStats.get(key,targetClass);
    }

}
