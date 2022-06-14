package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description
 * @date 2022-06-14 15:17
 **/
public class PageFaultsMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected PageFaultsMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public int getPageFaults(){
        return getOpLatenciesData("page_faults");
    }

    private <T> T getOpLatenciesData(String key) {

        return (T) getServerStatus().get("extra_info",Document.class).get(key,Integer.class);
    }
}
