package com.whaleal.mars.core.index;

import com.mongodb.client.model.Collation;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Address;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-27 16:09
 **/
public class IndexCollationTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createIndex(){
        mars.createCollection(Book.class);

        mars.ensureIndexes(Book.class);

    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }


    @Test
    public void testForQueryIndex(){

        List<Index> indexList = mars.getIndexes("book");

        IndexOptions zh = new IndexOptions().collation(Collation.builder().locale("zh").numericOrdering(true).build()).name("name_1_price_-1");
        Index index1 = new Index().on("stocks",IndexDirection.ASC).setOptions(new IndexOptions().name("stocks_1"));
        Index index2 = new Index().on("name", IndexDirection.ASC).on("price", IndexDirection.DESC).setOptions(zh);

        Assert.assertEquals(indexList.get(1).toString(),index1.toString());
        Assert.assertEquals(indexList.get(2),index2);

    }

    @Test
    public void testForDeleteIndex(){
        List<Index> book = mars.getIndexes("book");
        Assert.assertEquals(book.size(),2);

        mars.dropIndex(new Index("name",IndexDirection.ASC),"book");
        List<Index> book1 = mars.getIndexes("book");
        Assert.assertEquals(book1.size(),1);

    }

    @Test
    public void testForCopy(){
        //获取集合索引
        List<Index> bookIndex = mars.getIndexes("book");

        Assert.assertEquals(bookIndex.get(2).getIndexKeys().toJson(),"{\"name\": 1, \"price\": -1}");
        Assert.assertEquals(bookIndex.size(),3);

        Index index = bookIndex.get(2);

        //删除索引
        mars.dropIndex(index,"book");
        Assert.assertEquals(mars.getIndexes("book").size(),2);

        //复用索引
        mars.createIndex(index,"book");

        List<Index> book = mars.getIndexes("book");
        Assert.assertEquals(book.get(2).getIndexKeys().toJson(),"{\"name\": 1, \"price\": -1}");
        Assert.assertEquals(book.size(),3);
        Assert.assertEquals(index.toString(),book.get(2).toString());


    }

    @Test
    public void testForCreateIndex(){
        mars.createIndex(new Index("temp",IndexDirection.ASC,new IndexOptions().collation(Collation.builder().locale("zh").build())),"weather");
    }

}
