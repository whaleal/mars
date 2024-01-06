package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.whaleal.mars.util.ObjectUtil;
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
        Document operation = getQueryExecutorData("operation", Document.class);
        if(operation ==null ||  operation.isEmpty()){
            return 0L;
        }

        if(operation.containsKey("scanAndOrder")){
            return operation.get("scanAndOrder",Long.class);
        }

        return 0L;
    }

    private <T> T getQueryExecutorData(String key,Class<T> targetClass) {
        Document mem = serverStatus.get("metrics",Document.class)
                .get("queryExecutor",Document.class);

        return (T) mem.get(key,targetClass);
    }
}
