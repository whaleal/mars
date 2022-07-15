package com.whaleal.mars.core.command;

import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.bean.*;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Author: cjq
 * Date: 2022/6/29 0029 10:31
 * FileName: CollModTest
 * Description:
 */
public class CollModTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/mars?authSource=admin");


    @Before
    public void createData(){
        mars.createCollection(Weather01.class);
        mars.createCollection(Contacts.class);
        mars.insert(new Book(new ObjectId(),"test",2.0,2));
        Index index = new Index("name", IndexDirection.ASC,new IndexOptions().expireAfter(40L, TimeUnit.SECONDS));
        mars.createIndex(index,"book");
        mars.insert(new Weather01(1,new Date(),"test","temp","test"));
    }

    /**
     * db.runCommand( {
     *    collMod: <collection>,
     *    index: {
     *       keyPattern: <index_spec> || name: <index_name>,
     *       expireAfterSeconds: <number>,  // If changing the TTL expiration threshold
     *       hidden: <boolean>              // If changing the visibility of the index from the query planner
     *    }
     * } )
     */
    @Test
    public void testForModIndexOptionsToCollection(){
        //修改集合索引Options
        Document document = new Document()
                .append("collMod", "book")
                .append("index", new Document()
                        .append("name", "name_1")
                        .append("expireAfterSeconds", 20));
        Document document1 = mars.executeCommand(document);
        Document result = Document.parse("{expireAfterSeconds_old:40, expireAfterSeconds_new:20, ok:1.0}");
        //todo 对比结果看上去一致却爆出错误
        Assert.assertEquals(document1,result);
    }

    //todo ModifyView

    /**
     * db.runCommand({
     *    collMod: <collection>,
     *    expireAfterSeconds: <Number> || "off"
     * })
     */
    @Test
    public void testForModTimeSeriesCollection(){
        //修改时序集合的数据过期时间
        Document document = new Document();
        document.append("collMod","weather01")
                .append("expireAfterSeconds",10);
        Document document1 = mars.executeCommand(document);
        System.out.println(document1);
        Document result = Document.parse("{ok:1.0}");
        Assert.assertEquals(document1,result);
    }

    /**
     * db.runCommand( { collMod: "contacts",
     *    validator: { $jsonSchema: {
     *       bsonType: "object",
     *       required: [ "phone" ],
     *       properties: {
     *          phone: {
     *             bsonType: "string",
     *             description: "must be a string and is required"
     *          },
     *          email: {
     *             bsonType : "string",
     *             pattern : "@mongodb\.com$",
     *             description: "must be a string and match the regular expression pattern"
     *          },
     *          status: {
     *             enum: [ "Unknown", "Incomplete" ],
     *             description: "can only be one of the enum values"
     *          }
     *       }
     *    } },
     *    validationLevel: "moderate",
     *    validationAction: "warn"
     * } )
     */
    @Test
    public void testForAddDocumentValidation(){
        //添加文档校验规则
        Document document = new Document();
        document.append("collMod","contacts")
                .append("validator",new Document()
                        .append("$jsonSchema",new Document()
                                .append("bsonType","object")
                                .append("required", ListUtil.of("phone"))
                                .append("properties",new Document()
                                        .append("phone",new Document()
                                                .append("bsonType","string")
                                                .append("description","must be a string and is required"))
                                        .append("email",new Document()
                                                .append("bsonType","string")
                                                .append("pattern","@mongodb.\\com$")
                                                .append("description","must be a string and match the regular expression pattern"))
                                        .append("status",new Document()
                                                .append("enum",ListUtil.of("Unknown","Incomplete"))
                                                .append("description","can only be one of the enum values")))))
                .append("validationLevel","moderate")
                .append("validationAction","warn");
        Document document1 = mars.executeCommand(document);
        mars.insert(new Contacts(1,null,"111@qq.com", Status.Unknown));
        System.out.println(document1);
        //todo 比对日志信息 是否警告
    }


    @After
    public void dropCollection(){
        mars.dropCollection("book");
        mars.dropCollection("weather01");
        mars.dropCollection("contacts");
    }
}
