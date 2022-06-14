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

    public int getFileReadLatencyBucketOne(){
        return getLatencyData("file system read latency histogram (bucket 1) - 10-49ms");
    }
    public int getFileReadLatencyBucketTwo(){
        return getLatencyData("file system read latency histogram (bucket 2) - 50-99ms");
    }
    public int getFileReadLatencyBucketThree(){
        return getLatencyData("file system read latency histogram (bucket 3) - 100-249ms");
    }
    public int getFileReadLatencyBucketFour(){
        return getLatencyData("file system read latency histogram (bucket 4) - 250-499ms");
    }
    public int getFileReadLatencyBucketFive(){
        return getLatencyData("file system read latency histogram (bucket 5) - 500-999ms");
    }
    public int getFileReadLatencyBucketSix(){
        return getLatencyData("file system read latency histogram (bucket 6) - 1000ms+");
    }
    public int getFileWriteLatencyBucketOne(){
        return getLatencyData("file system write latency histogram (bucket 1) - 10-49ms");
    }
    public int getFileWriteLatencyBucketTwo(){
        return getLatencyData("file system write latency histogram (bucket 2) - 50-99ms");
    }
    public int getFileWriteLatencyBucketThree(){
        return getLatencyData("file system write latency histogram (bucket 3) - 100-249ms");
    }
    public int getFileWriteLatencyBucketFour(){
        return getLatencyData("file system write latency histogram (bucket 4) - 250-499ms");
    }
    public int getFileWriteLatencyBucketFive(){
        return getLatencyData("file system write latency histogram (bucket 5) - 500-999ms");
    }
    public int getFileWriteLatencyBucketSix(){
        return getLatencyData("file system write latency histogram (bucket 6) - 1000ms+");
    }

    public int getOperationReadLatencyBucketOne(){
        return getLatencyData("operation read latency histogram (bucket 1) - 100-249us");
    }
    public int getOperationReadLatencyBucketTwo(){
        return getLatencyData("operation read latency histogram (bucket 2) - 250-499us");
    }
    public int getOperationReadLatencyBucketThree(){
        return getLatencyData("operation read latency histogram (bucket 3) - 500-999us");
    }
    public int getOperationReadLatencyBucketFour(){
        return getLatencyData("operation read latency histogram (bucket 4) - 1000-9999us");
    }
    public int getOperationReadLatencyBucketFive(){
        return getLatencyData("operation read latency histogram (bucket 5) - 10000us+");
    }

    public int getOperationWriteLatencyBucketOne(){
        return getLatencyData("operation write latency histogram (bucket 1) - 100-249us");
    }
    public int getOperationWriteLatencyBucketTwo(){
        return getLatencyData("operation write latency histogram (bucket 2) - 250-499us");
    }
    public int getOperationWriteLatencyBucketThree(){
        return getLatencyData("operation write latency histogram (bucket 3) - 500-999us");
    }
    public int getOperationWriteLatencyBucketFour(){
        return getLatencyData("operation write latency histogram (bucket 4) - 1000-9999us");
    }
    public int getOperationWriteLatencyBucketFive(){
        return getLatencyData("operation write latency histogram (bucket 5) - 10000us+");
    }


    private int getLatencyData(String key){
        Document databaseLocks = getServerStatus().get("wiredTiger", Document.class)
                .get("perf", Document.class);

        return (int)databaseLocks.get(key);
    }
}
