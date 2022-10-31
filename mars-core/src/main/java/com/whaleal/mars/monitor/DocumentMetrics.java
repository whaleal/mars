package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.Document;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 10:49
 **/
public class DocumentMetrics extends AbstractMonitor{

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected DocumentMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getInserted(){
        return getDocument("inserted", Long.class);
    }

    public Long getUpdated(){
        return getDocument("updated", Long.class);
    }

    public Long getReturned(){
        return getDocument("returned", Long.class);
    }


    private <T> T getDocument(String key,Class<T> clazz){
        Document metrics = serverStatus.get("metrics", Document.class);
        if(ObjectUtil.isEmpty(metrics)){
            return null;
        }

        Document document = metrics.get("document", Document.class);
        if(ObjectUtil.isEmpty(document)){
            return null;
        }

        return document.get(key,clazz);

    }
}
