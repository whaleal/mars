package com.whaleal.mars.core;/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.whaleal.mars.bean.Address;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Precondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.whaleal.mars.core.index.Index;

import java.util.List;


@SpringBootTest("team.mars.mongodb.Application")
@RunWith(SpringRunner.class)
public class MarsApplicationTests {



    @Autowired
    Mars mars;



    @Test
    public void test() {

        Precondition.PreconditionNotNull(mars);


    }


    @Test
    public void testDBAndCollection(){
        MongoDatabase database = mars.getDatabase();
        MongoCollection<Document> tables = database.getCollection("tables");

        tables.insertOne(new Document());

    }

    @Test
    public void testCollectionName() {

        String tableName = "marsTables";



        //Precondition.PreconditionEquals(mars.determineCollectionName(Address.class, tableName), tableName);


        //Precondition.PreconditionEquals(mars.determineCollectionName(Address.class,null),"data");

        //Precondition.PreconditionEquals(mars.determineCollectionName(Address.class,null),"data");

        //System.out.println(mars.determineCollectionName(Address.class, null));

    }

    @Test
    public void testEnsureIndexes() {

        mars.ensureIndexes(Address.class, "addr");

    }

    @Test
    public void testEnsureIndexesWithBigData() {

        System.out.println(System.currentTimeMillis());
        mars.ensureIndexes(Address.class, "POCCOLL");
        System.out.println(System.currentTimeMillis());

    }

    @Test
    public void testGetIndexes() {

        List<Index> addr = mars.getIndexes("cc");

        System.out.println(addr);
    }


}