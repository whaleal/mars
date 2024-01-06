package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import com.mongodb.lang.Nullable;
import org.bson.Document;



/**
 * @author lyz
 * @desc
 * @create: 2022-10-31 11:05
 **/
public class TransactionMetrics extends AbstractMonitor{
    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected TransactionMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getCurrentActive(){
        return getTransaction("currentActive");
    }

    public Long getCurrentInactive(){
        return getTransaction("currentInactive");
    }

    public Long getTotalCommitted(){
        return getTransaction("totalCommitted");
    }

    public Long getTotalAborted(){
        return getTransaction("totalAborted");
    }

    @Nullable
    private Long getTransaction(String key){
        Document transactions = serverStatus.get("transactions", Document.class);

        if(transactions ==null || transactions.isEmpty()){
            return null;
        }

        return transactions.getLong(key);
    }

}
