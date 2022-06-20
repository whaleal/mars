package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-20 18:14
 **/
public class SortTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createCollection(){
        List<Document> documents = CreateDataUtil.parseString("{ \"item\": \"journal\", \"qty\": 25, \"size\": { \"h\": 14, \"w\": 21, \"uom\": \"cm\" }, \"status\": \"A\" },\n" +
                "    { \"item\": \"notebook\", \"qty\": 50, \"size\": { \"h\": 8.5, \"w\": 11, \"uom\": \"in\" }, \"status\": \"A\" },\n" +
                "    { \"item\": \"paper\", \"qty\": 100, \"size\": { \"h\": 8.5, \"w\": 11, \"uom\": \"in\" }, \"status\": \"D\" },\n" +
                "    { \"item\": \"planner\", \"qty\": 75, \"size\": { \"h\": 22.85, \"w\": 30, \"uom\": \"cm\" }, \"status\": \"D\" },\n" +
                "    { \"item\": \"postcard\", \"qty\": 45, \"size\": { \"h\": 10, \"w\": 15.25, \"uom\": \"cm\" }, \"status\": \"A\" }\n");

        mars.insert(documents,"inventory");

    }

    @After
    public void dropCollection(){
        mars.dropCollection("inventory");
    }

    @Test
    public void testForSort(){
        Criteria criteria = Criteria.where("status").is("A");

        Query query = new Query(criteria);

        query.with(Sort.on().descending("qty"));

        QueryCursor<Document> inventory = mars.findAll(query, Document.class, "inventory");
        while (inventory.hasNext()){
            System.out.println(inventory.next());
        }
    }

}
