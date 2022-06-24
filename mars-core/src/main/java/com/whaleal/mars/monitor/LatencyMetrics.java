package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 17:48
 **/
public class LatencyMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected LatencyMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Integer getFileReadLatencyBucketOne(){
        return getLatencyData("file system read latency histogram (bucket 1) - 10-49ms");
    }
    public Integer getFileReadLatencyBucketTwo(){
        return getLatencyData("file system read latency histogram (bucket 2) - 50-99ms");
    }
    public Integer getFileReadLatencyBucketThree(){
        return getLatencyData("file system read latency histogram (bucket 3) - 100-249ms");
    }
    public Integer getFileReadLatencyBucketFour(){
        return getLatencyData("file system read latency histogram (bucket 4) - 250-499ms");
    }
    public Integer getFileReadLatencyBucketFive(){
        return getLatencyData("file system read latency histogram (bucket 5) - 500-999ms");
    }
    public Integer getFileReadLatencyBucketSix(){
        return getLatencyData("file system read latency histogram (bucket 6) - 1000ms+");
    }
    public Integer getFileWriteLatencyBucketOne(){
        return getLatencyData("file system write latency histogram (bucket 1) - 10-49ms");
    }
    public Integer getFileWriteLatencyBucketTwo(){
        return getLatencyData("file system write latency histogram (bucket 2) - 50-99ms");
    }
    public Integer getFileWriteLatencyBucketThree(){
        return getLatencyData("file system write latency histogram (bucket 3) - 100-249ms");
    }
    public Integer getFileWriteLatencyBucketFour(){
        return getLatencyData("file system write latency histogram (bucket 4) - 250-499ms");
    }
    public Integer getFileWriteLatencyBucketFive(){
        return getLatencyData("file system write latency histogram (bucket 5) - 500-999ms");
    }
    public Integer getFileWriteLatencyBucketSix(){
        return getLatencyData("file system write latency histogram (bucket 6) - 1000ms+");
    }

    public Integer getOperationReadLatencyBucketOne(){
        return getLatencyData("operation read latency histogram (bucket 1) - 100-249us");
    }
    public Integer getOperationReadLatencyBucketTwo(){
        return getLatencyData("operation read latency histogram (bucket 2) - 250-499us");
    }
    public Integer getOperationReadLatencyBucketThree(){
        return getLatencyData("operation read latency histogram (bucket 3) - 500-999us");
    }
    public Integer getOperationReadLatencyBucketFour(){
        return getLatencyData("operation read latency histogram (bucket 4) - 1000-9999us");
    }
    public Integer getOperationReadLatencyBucketFive(){
        return getLatencyData("operation read latency histogram (bucket 5) - 10000us+");
    }

    public Integer getOperationWriteLatencyBucketOne(){
        return getLatencyData("operation write latency histogram (bucket 1) - 100-249us");
    }
    public Integer getOperationWriteLatencyBucketTwo(){
        return getLatencyData("operation write latency histogram (bucket 2) - 250-499us");
    }
    public Integer getOperationWriteLatencyBucketThree(){
        return getLatencyData("operation write latency histogram (bucket 3) - 500-999us");
    }
    public Integer getOperationWriteLatencyBucketFour(){
        return getLatencyData("operation write latency histogram (bucket 4) - 1000-9999us");
    }
    public Integer getOperationWriteLatencyBucketFive(){
        return getLatencyData("operation write latency histogram (bucket 5) - 10000us+");
    }

    private Integer getLatencyData(String key){
        Document databaseLocks = getServerStatus().get("wiredTiger", Document.class)
                .get("perf", Document.class);

        return databaseLocks.getInteger(key);
    }
}
