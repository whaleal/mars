package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:36
 * FileName: DbStatsTest
 * Description:
 */
public class DbStatsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( {
     *    dbStats: 1,
     *    scale: <number>,
     *    freeStorage: 0
     * } )
     */
    @Test
    public void testForDbStats(){
        Document document = mars.executeCommand(Document.parse("{ dbStats: 1 }"));
        String db1 = (String) document.get("db");
        Integer collections1 = (Integer) document.get("collections");
        Integer views = (Integer) document.get("views");
        Integer objects = (Integer) document.get("objects");
        Double avgObjSize = (Double) document.get("avgObjSize");
        Double dataSize = (Double) document.get("dataSize");
        Double storageSize1 = (Double) document.get("storageSize");
        Integer indexes = (Integer) document.get("indexes");
        Double indexSize = (Double) document.get("indexSize");
        Double totalSize = (Double) document.get("totalSize");
        Double scaleFactor = (Double) document.get("scaleFactor");
        Document document3 = new Document().append("db", db1)
                .append("collections", collections1)
                .append("views", views)
                .append("objects", objects)
                .append("avgObjSize", avgObjSize)
                .append("dataSize", dataSize)
                .append("storageSize", storageSize1)
                .append("indexes", indexes)
                .append("indexSize", indexSize)
                .append("totalSize", totalSize)
                .append("scaleFactor", scaleFactor);
        Document result = Document.parse("{\n" +
                "\t\"db\" : \"mars\",\n" +
                "\t\"collections\" : 4,\n" +
                "\t\"views\" : 1,\n" +
                "\t\"objects\" : 5,\n" +
                "\t\"avgObjSize\" : 66.4,\n" +
                "\t\"dataSize\" : 332.0,\n" +
                "\t\"storageSize\" : 77824.0,\n" +
                "\t\"indexes\" : 3,\n" +
                "\t\"indexSize\" : 73728.0,\n" +
                "\t\"totalSize\" : 151552.0,\n" +
                "\t\"scaleFactor\" : 1.0,\n" +
                "}\n");
        Assert.assertEquals(result,document3);
        //要查看集合的空闲空间
        Document document1 = mars.executeCommand(Document.parse("{ dbStats: 1, scale: 1024, freeStorage: 1 }"));
        String db = (String) document1.get("db");
        Integer collections = (Integer) document1.get("collections");
        Double storageSize = (Double) document1.get("storageSize");
        Double freeStorageSize = (Double) document1.get("freeStorageSize");
        Document document2 = new Document().append("db", db)
                .append("collections", collections)
                .append("storageSize", storageSize)
                .append("freeStorageSize", freeStorageSize);
        Document result1 = Document.parse("{\n" +
                "\t\"db\" : \"mars\",\n" +
                "\t\"collections\" : 4,\n" +
                "\t\"storageSize\" : 76.0,\n" +
                "\t\"freeStorageSize\" : 28.0,\n" +
                "}\n");
        Assert.assertEquals(result1,document2);
    }
}
