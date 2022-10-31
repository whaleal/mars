package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.Document;


/**
 * @author lyz
 * @description
 * @date 2022-06-14 16:43
 **/
public class QueryExecutorMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected QueryExecutorMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getScannedKey(){
        return getQueryExecutorData("scanned",Long.class);
    }

    public Long getScannedObjects(){
        return getQueryExecutorData("scannedObjects",Long.class);
    }

    public Long getCollectionScanNonTailable(){
        return getQueryExecutorData("collectionScans", Document.class).get("nonTailable",Long.class);
    }

    public Long getCollectionScanTotal(){
        return getQueryExecutorData("collectionScans", Document.class).get("total",Long.class);
    }

    public Long getScanAndOrder(){
        if(ObjectUtil.isEmpty(getQueryExecutorData("operation",Document.class))){
            return 0L;
        }
        return getQueryExecutorData("operation",Document.class).get("scanAndOrder",Long.class);
    }

    private <T> T getQueryExecutorData(String key,Class<T> targetClass) {
        Document mem = serverStatus.get("metrics",Document.class)
                .get("queryExecutor",Document.class);

        return (T) mem.get(key,targetClass);
    }
}
