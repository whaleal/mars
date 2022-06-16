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

    public int getWriteOut(){
        return getWriteTicketsData("out");
    }

    public int getWriteAvailable(){
        return getWriteTicketsData("available");
    }

    public int getWriteTotalTickets(){
        return getWriteTicketsData("totalTickets");
    }

    public int getReadOut(){
        return getReadTicketsData("out");
    }

    public int getReadAvailable(){
        return getReadTicketsData("available");
    }

    public int getReadTotalTickets(){
        return getReadTicketsData("totalTickets");
    }

    private int getWriteTicketsData(String key) {
        Document writeTicketsDocument = (Document) getServerStatus().get("wiredTiger",Document.class)
                .get("concurrentTransactions", Document.class)
                .get("write");
        return (int)writeTicketsDocument.get(key);
    }

    private int getReadTicketsData(String key) {
        Document readTicketsDocument = (Document) getServerStatus().get("wiredTiger",Document.class)
                .get("concurrentTransactions", Document.class)
                .get("read");
        return (int)readTicketsDocument.get(key);
    }
}
