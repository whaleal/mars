package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 17:27
 **/
public class LocksMetrics extends AbstractMonitor{

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected LocksMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Document getGlobalAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getGlobalAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getGlobalTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getGlobalDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    public Document getDatabaseAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getDatabaseAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getDatabaseTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getDatabaseDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    public Document getCollectionAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getCollectionAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getCollectionTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getCollectionDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    public Document getMutexAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getMutexAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getMutexTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getMutexDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    public Document getMetadataAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getMetadataAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getMetadataTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getMetadataDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    public Document getOplogAcquireCount(){
        return getLockData("Global","acquireCount");
    }

    public Document getOplogAcquireWaitCount(){
        return getLockData("Global","acquireWaitCount");
    }

    public Document getOplogTimeAcquiringMicros(){
        return getLockData("Global","timeAcquiringMicros");
    }

    public Document getOplogDeadlockCount(){
        return getLockData("Global","deadlockCount");
    }

    private Document getLockData(String lockLevel,String lockSort){
        Document databaseLocks = serverStatus.get("locks", Document.class)
                .get(lockLevel, Document.class);

        return (Document)databaseLocks.get(lockSort);
    }

}
