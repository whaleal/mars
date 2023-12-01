package com.whaleal.mars.base;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CreateCollectionOptions;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.NumberBean;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.option.CollectionOptions;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @author wh
 * @description
 * @date 2022-07-13 15:27
 **/
public class CollationTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @After
    public void dropCollection() {
        mars.getDatabase().drop();
    }


    /**
     * 创建集合时手动指定字符集排序规则
     */
    @Test
    public void CollationForCreation() {

        mars.createCollection("student", CollectionOptions.just(Collation.builder().locale("zh").build()));
        List< Document > list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list, "student");


        AggregationPipeline< Document > pipeline = AggregationPipeline.create();
        pipeline.sort(com.whaleal.mars.core.aggregation.stages.Sort.sort().ascending("name"));
        pipeline.project(Projection.project().exclude("_id"));

        //  查询结果应当是有序的 正确的
        List< Document > list1 = mars.aggregate(pipeline, "student").toList();

        List< Document > list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");


        System.out.println(StrUtil.toString(list1));
        System.out.println(StrUtil.toString(list2));

        Assert.assertEquals(list1, list2);




        MongoIterable< Document > collectionInfos = mars.getDatabase().listCollections();

        // Iterate through collection information
        for (Document collectionInfo : collectionInfos) {

            Document document = collectionInfo.get("options", Document.class);

            Document collation = document.get("collation",Document.class);

            System.out.println(collation);

            Assert.assertEquals("zh",collation.get("locale"));

        }
    }

    /**
     * 创建普通集合，查询时才指定排序规则
     */
    @Test
    public void CollationForQuery() {
        
        String  tableName  = "student";

        mars.createCollection(tableName);
        List< Document > list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list, tableName);


        List< Document > list1 = mars.find(new Query().withProjection(new com.whaleal.mars.core.query.Projection().exclude("_id")).with(Sort.ascending("name")).collation(Collation.builder().locale("zh").build()), Document.class, tableName).toList();
        List< Document > list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");
        System.out.println(StrUtil.toString(list1));
        System.out.println(StrUtil.toString(list2));
        Assert.assertEquals(list2, list1);
    }

    @Test
    public void CollationForEntityAnno() {

        mars.createCollection(NumberBean.class);
        mars.ensureIndexes(NumberBean.class);
        List< Document > list = CreateDataUtil.parseString("{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }\n" +
                "{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"马六\" }");
        mars.insert(list, "numberBean");

        AggregationPipeline< Document > pipeline = AggregationPipeline.create();
        pipeline.sort(com.whaleal.mars.core.aggregation.stages.Sort.sort().ascending("name"));
        pipeline.project(Projection.project().exclude("_id"));

        List< Document > list1 = mars.aggregate(pipeline, "numberBean").toList();

        List< Document > list2 = CreateDataUtil.parseString("{\"name\" : \"李四\" }\n" +
                "{\"name\" : \"马六\" }\n" +
                "{\"name\" : \"王五\" }\n" +
                "{\"name\" : \"张七\" }\n" +
                "{\"name\" : \"张三\" }");

        System.out.println(StrUtil.toString(list));
        System.out.println(StrUtil.toString(list1));
        System.out.println(StrUtil.toString(list2));
        Assert.assertEquals(list2, list1);


        List< Index > numberBean = mars.getIndexes("numberBean");
        for(Index index :numberBean){
            String locale = index.getIndexOptions().getCollation().getLocale();

            System.out.println(index.getIndexKeys());
            System.out.println(locale);
            Assert.assertEquals("zh",locale);
        }


    }




}
