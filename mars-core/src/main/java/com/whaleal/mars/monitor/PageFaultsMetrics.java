package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * @author lyz
 * @description 解析db.serverStatus()的extra_info.page_faults参数
 * @date 2022-06-14 15:17
 **/
public class PageFaultsMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected PageFaultsMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public long getPageFaults(){
        return getOpLatenciesData("page_faults");
    }

    private Long getOpLatenciesData(String key) {

        return getServerStatus().get("extra_info",Document.class).get(key,Long.class);
    }
}
