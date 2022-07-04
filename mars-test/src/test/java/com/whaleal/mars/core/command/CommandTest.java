package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @date 2022-05-18 13:56
 */
public class CommandTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){

        Document append = new Document("_id", "12769ea0f3dc6ead47c9a1b2")
                .append("author", "abc123")
                .append("title", "zzz")
                .append("tags", new String[]{"programming", "database", "mongodb"});
        mars.insert(append,"articles");


        mars.insert(new Document("id","0").append("name","Pepperoni").append("size","small").append("price","19").append("quantity","20").append("date","ISODate(2021, Calendar.MARCH, 13)"));
        mars.insert(new Document("id","1").append("name","Pepperoni").append("size","small").append("price","19").append("quantity","20").append("date","ISODate(2021, Calendar.MARCH, 13)"));
        mars.insert(new Document("id","2").append("name","Pepperoni").append("size","small").append("price","19").append("quantity","20").append("date","ISODate(2021, Calendar.MARCH, 13)"));
        mars.insert(new Document("id","3").append("name","Cheese").append("size","small").append("price","12").append("quantity","15").append("date","ISODate(2021, Calendar.MARCH, 13)"));
        mars.insert(new Document("id","4").append("name","Cheese").append("size","small").append("price","19").append("quantity","20").append("date","ISODate(2021, Calendar.MARCH, 13)"));
        mars.insert(new Document("id","5").append("name","Pepperoni").append("size","small").append("price","19").append("quantity","20").append("date","ISODate(2021, Calendar.MARCH, 13)"));

        mars.createIndex(new Index("status", IndexDirection.ASC),"orders");

    }

    @After
    public void dropData(){
        mars.dropCollection("articles");
        mars.dropCollection("orders");
    }

//    @Test
//    public void test(){
//        Document parse = Document.parse("");
//        Document document = mars.executeCommand(parse);
//    }

    @Test
    public void testForHello(){
        String s = "{serverStatus: 1}";
//        Document document = mars.executeCommand(s);

//        System.out.println(document);
        String s1 = "{hello : 1}";
        Document dbStats = new Document("dbStats", "1");
        Document document1 = mars.executeCommand(s1);
        System.out.println(document1);

    }

    //todo 结果存在差别
    @Test
    public void testForAggregate(){
        Document parse = Document.parse("{\n" +
                "   aggregate: \"articles\",\n" +
                "   pipeline: [\n" +
                "      { $project: { tags: 1 } },\n" +
                "      { $unwind: \"$tags\" },\n" +
                "      { $group: { _id: \"$tags\", count: { $sum : 1 } } }\n" +
                "   ],\n" +
                "   cursor: { }\n" +
                "} ");
        Document document = mars.executeCommand(parse);
        System.out.println(document);

    }


    @Test
    public void testForCount(){
        Document document = mars.executeCommand("{count : 'articles'}");
        System.out.println(document);
    }


    @Test
    public void testForCountAndQuery(){
        Document parse = Document.parse("   {\n" +
                "     count:'orders',\n" +
                "     query: {\n" +
                "              ord_dt: { $gt: 'ISODate(2021, Calendar.MARCH, 13)' },\n" +
                "              status: \"D\"\n" +
                "            },\n" +
                "     hint: { status: 1 }\n" +
                "   }");

        Document document = mars.executeCommand(parse);
        System.out.println(document);
    }

}
