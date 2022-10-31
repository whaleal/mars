package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description 解析db.serverStatus()的cache参数
 * @date 2022-06-14 15:22
 **/
public class CacheMetrics extends AbstractMonitor{

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected CacheMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Integer getReadBytesIntoCache(){
        return getCacheData("bytes read into cache");
    }

    public Integer getWrittenBytesFromCache(){
        return getCacheData("bytes written from cache");
    }

    public Integer getTrackedDirtyBytesIntoCache(){
        return getCacheData("tracked dirty bytes in the cache");
    }

    public Integer getBytesCurrentlyInTheCache(){
        return getCacheData("bytes currently in the cache");
    }


    private Integer getCacheData(String key) {
        Document cache = serverStatus.get("wiredTiger",Document.class).get("cache",Document.class);
        // Class c = btree.get(key).getClass();
        return  cache.getInteger(key);
    }


}
