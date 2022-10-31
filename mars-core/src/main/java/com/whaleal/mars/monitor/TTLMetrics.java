package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.Document;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 11:12
 **/
public class TTLMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected TTLMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getDeletedByTTL(){
        return getTTL("deletedDocuments", Long.class);
    }

    public Long getDeleted(){
        return getTTL("deleted", Long.class);
    }

    public Long getPass(){
        return getTTL("passes", Long.class);
    }

    private <T> T getTTL(String key,Class<T> clazz){
        Document metrics = serverStatus.get("metrics", Document.class);
        if(ObjectUtil.isEmpty(metrics)){
            return null;
        }

        Document ttl = metrics.get("ttl", Document.class);
        if(ObjectUtil.isEmpty(ttl)){
            return null;
        }

        return ttl.get(key,clazz);
    }
}
