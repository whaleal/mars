package com.whaleal.mars.core.index;


import com.whaleal.mars.bean.Student;
import org.bson.Document;
import org.junit.Precondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.IndexOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {


    @Autowired
    Mars mars;

    String coll = "coll" ;



    @Test
    public void testConnection(){

        Precondition.PreconditionNotNull(mars);
    }




    @Test
    public void testCreateIndex(){

        mars.dropIndexes(coll);
        Index index = new Index();
        index.on("c", IndexDirection.ASC);
        index.setOptions(new IndexOptions().background(true).expireAfter(3600l, TimeUnit.SECONDS));
        mars.createIndex(index,coll);

        List<Index> cool = mars.getIndexes(coll);
        Precondition.PreconditionEquals(cool.size() ,2);

        Precondition.PreconditionEquals(cool.get(1).getIndexKeys(),new Document("c",1));

        Precondition.PreconditionEquals(cool.get(1).getIndexOptions().getExpireAfter(TimeUnit.SECONDS),Long.valueOf(3600));

        mars.dropIndexes(coll);

    }


    @Test
    public void testDropIndex(){
        Index  index = new Index();
        index.on("a",IndexDirection.ASC).on("b",IndexDirection.DESC);
        index.setOptions(new IndexOptions().background(true));

        mars.dropIndexes(coll);

        mars.createIndex(index ,coll);
        List<Index> indexes = mars.getIndexes(coll);
        Precondition.PreconditionEquals(indexes.size() ,2);

        Precondition.PreconditionEquals(indexes.get(1).getIndexKeys(),new Document("a",1).append("b",-1));
        mars.dropIndex(index,coll);

    }

    @Test
    public void testEnsurseIndexes(){

        mars.dropIndexes(coll);
        mars.ensureIndexes(Student.class ,coll);
        List<Index> indexes = mars.getIndexes(coll);
        Precondition.PreconditionEquals(indexes.size() ,4);


        Precondition.PreconditionEquals(indexes.get(1).getIndexKeys(),new Document("salary",1).append("name",-1));

        Precondition.PreconditionEquals(indexes.get(2).getIndexKeys(),new Document("idcc","hashed"));


        Precondition.PreconditionEquals(indexes.get(3).getIndexKeys(),new Document("idcc",1));


        Long expireAfter = indexes.get(3).getIndexOptions().getExpireAfter(TimeUnit.SECONDS);

        Precondition.PreconditionEquals(expireAfter,Long.valueOf(10));

        mars.dropIndexes(coll);


    }
}


