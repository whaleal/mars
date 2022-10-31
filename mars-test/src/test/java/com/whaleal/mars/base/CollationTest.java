package com.whaleal.mars.base;

import com.mongodb.client.model.Collation;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.NumberBean;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.CollectionOptions;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author lyz
 * @description
 * @date 2022-07-13 15:27
 **/
public class CollationTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @After
    public void dropCollection(){
        mars.dropCollection("student");
        mars.dropCollection("student1");
        mars.dropCollection("numberBean");
//        mars.dropCollection("weather");
//        mars.dropCollection("number");
    }


    /**
     * 创建集合时手动指定字符集排序规则
     */
    @Test
    public void testForWhenCreate(){
        mars.createCollection("student", CollectionOptions.just(Collation.builder().locale("zh").build()));
        List<Document> list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list,"student");

        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.sort(com.whaleal.mars.core.aggregation.stages.Sort.sort().ascending("name"));
        pipeline.project(Projection.project().exclude("_id"));

        List<Document> list1 = mars.aggregate(pipeline, "student").toList();

        List<Document> list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");

        Assert.assertEquals(list1,list2);

    }

    /**
     * 创建普通集合，查询时才指定排序规则
     */
    @Test
    public void testForWhenQuery(){
        mars.createCollection("student1");
        List<Document> list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list,"student1");

//        List<Document> list1 = new ArrayList();
//        QueryCursor<Document> all = mars.findAll(new Query().with(Sort.ascending("name")).collation(com.whaleal.mars.core.query.Collation.of("zh")), Document.class, "student1");
//        while (all.hasNext()){
//            String name = all.next().getString("name");
//            list1.add(new Document("name",name));
//        }
//        List<Document> list1 = mars.findAll(new Query().with(Sort.ascending("name")).collation(com.whaleal.mars.core.query.Collation.of("zh")), Document.class, "student1").toList();
        List<Document> list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");

//        Assert.assertEquals(list1,list2);
    }

    @Test
    public void testForWithAnno(){
        mars.createCollection(NumberBean.class);
        List<Document> list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list,"numberBean");

        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.sort(com.whaleal.mars.core.aggregation.stages.Sort.sort().ascending("name"));
        pipeline.project(Projection.project().exclude("_id"));

        List<Document> list1 = mars.aggregate(pipeline, "numberBean").toList();

        List<Document> list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");

        Assert.assertEquals(list1,list2);

    }
    }
