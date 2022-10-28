package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

import javax.print.Doc;


/**
 * @author lyz
 * @description  The information on the number of concurrent of read and write transactions allowed into the WiredTiger storage engine.
 * @date 2022-06-14 16:18
 **/
public class TicketsMetrics extends AbstractMonitor{

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected TicketsMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Integer getWriteOut(){
        return getWriteTicketsData("out");
    }

    public Integer getWriteAvailable(){
        return getWriteTicketsData("available");
    }

    public Integer getWriteTotalTickets(){
        return getWriteTicketsData("totalTickets");
    }

    public Integer getReadOut(){
        return getReadTicketsData("out");
    }

    public Integer getReadAvailable(){
        return getReadTicketsData("available");
    }

    public Integer getReadTotalTickets(){
        return getReadTicketsData("totalTickets");
    }

    private Integer getWriteTicketsData(String key) {
        Document writeTicketsDocument = (Document) serverStatus.get("wiredTiger",Document.class)
                .get("concurrentTransactions", Document.class)
                .get("write");
        return writeTicketsDocument.getInteger(key);
    }

    private Integer getReadTicketsData(String key) {
        Document readTicketsDocument = (Document) serverStatus.get("wiredTiger",Document.class)
                .get("concurrentTransactions", Document.class)
                .get("read");
        return readTicketsDocument.getInteger(key);
    }
}
