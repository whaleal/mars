package com.whaleal.mars.core;


import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.FindOneAndUpdateOptions;
import org.bson.Document;
import org.junit.Test;

import java.util.List;


/**
 * Author: cjq
 * Date: 2022/6/28 0028 10:11
 * FileName: TestCollation
 * Description:
 */
public class TestCollation {

    private Mars mars = new Mars(Constant.connectionStr);

    //创建集合
    @Test
    public void testForCreateCollection(){
        mars.createCollection(Book.class);
        //注解索引生效
        mars.ensureIndexes(Book.class);
    }
    //删除集合
    @Test
    public void testForDropCollection(){
        mars.dropCollection(Book.class);
    }

    //插入数据测试
    @Test
    public void testForInsert(){
        String s = "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffac\"), \"name\" : \"张七\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa8\"), \"name\" : \"张三\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa9\"), \"name\" : \"李四\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffaa\"), \"name\" : \"马六\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffab\"), \"name\" : \"马六\" }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"book");
    }


    //查询数据
    @Test
    public void testForFind(){
        //构建collation
        Query query = new Query();
        query.with(Sort.ascending("name"));
        query.collation(Collation.of("zh"));
        QueryCursor<Book> cursor = mars.findAll(query, Book.class);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }

    @Test
    public void testForFindAndModify(){
        //查询所需
        Query query = new Query();
        query.with(Sort.ascending("name"));
        query.collation(Collation.from(new Document().append("locale","zh").append("numericOrdering",true)));

        //更新所需
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("name").is("测试"));
        Update update = new Update();
        update.set("name","七");
        FindOneAndUpdateOptions collation = new
                FindOneAndUpdateOptions().collation(com.mongodb.client.model.Collation.builder().locale("zh").numericOrdering(true).build());
        //更新
        //update没有FindOneAndUpdateOptions会爆空指针异常
        mars.findAndModify(query1, update,collation, Book.class);

        //输出查询结果
        QueryCursor<Book> cursor = mars.findAll(query, Book.class);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }

    //集合值去重，并使用Collation排序
    @Test
    public void testForDistinct(){
        Document document = new Document()
                .append("distinct", "book")
                .append("key", "name")
                .append("collation", new Document()
                        .append("locale", "zh")
                        .append("numericOrdering", false));
        Document document1 = mars.executeCommand(document);
        System.out.println(document1);
    }






}
