package com.whaleal.mars.monitor;

import com.mongodb.client.*;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.BsonTimestamp;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lyz
 * @description 执行db.getReplicationInfo()，并获取执行结果
 * @date 2022-06-16 11:03
 **/
public class ReplicationInfoMetrics{

    private final MongoClient mongoClient;

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected ReplicationInfoMetrics(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * @return
     */
    @Nullable
    public Document getReplication() {
        Document document = new Document();
        MongoDatabase db = mongoClient.getDatabase("local");

        List<String> into = db.listCollectionNames().into(new ArrayList<>());

        if (!CollUtil.safeContains(into, "oplog.rs")) {
            throw new RuntimeException("neither master/slave nor replica set replication detected");
        }
        MongoCollection<Document> opLog = db.getCollection("oplog.rs");
        Document ol = db.runCommand(new Document("collStats", "oplog.rs"));

        Long maxSize = ol.getLong("maxSize");
        if (ObjectUtil.isNotEmpty(maxSize) && maxSize > 0) {
            //计算configured oplog size
            Long logSizeMB = maxSize / (1024 * 1024);
            document.put("size", logSizeMB);

            double size = ol.getInteger("size");
            double usedMB = size / (1024 * 1024);
            usedMB = Math.ceil(usedMB * 100) / 100;
            document.put("used", usedMB);

            //获取时间
            Document first = opLog.find().sort(new Document("$natural", 1)).limit(1).first();
            Document last = opLog.find().sort(new Document("$natural", -1)).limit(1).first();

            if (ObjectUtil.isAllNotEmpty(first, last)) {
                BsonTimestamp tfirst = first.get("ts", BsonTimestamp.class);
                BsonTimestamp tlast = last.get("ts", BsonTimestamp.class);
                if (ObjectUtil.isAllNotEmpty(tfirst, tlast)) {
                    long timeDiff = tlast.getTime() - tfirst.getTime();

                    document.put("log length", timeDiff);

                    Date tFirst = new Date(tfirst.getTime());
                    Date tLast = new Date(tlast.getTime());
                    document.put("first", tFirst);
                    document.put("last", tLast);
                    document.put("now", new Date());
                } else {
                    throw new RuntimeException("ts element not found in oplog objects");
                }
            } else {
                throw new RuntimeException("objects not found in local.oplog.$main -- is this a new and empty db instance?");
            }
        } else {
            throw new RuntimeException("Could not get stats for local." + "oplog.rs" + " collection. " +
                    "collstats returned: " + ol.toJson());
        }
        return document;
    }

}
