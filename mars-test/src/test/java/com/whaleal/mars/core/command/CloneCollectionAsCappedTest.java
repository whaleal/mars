package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 13:42
 * FileName: CloneCollectionAsCappedTest
 * Description:
 */
public class CloneCollectionAsCappedTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { cloneCollectionAsCapped: <existing collection>,
     *   toCollection: <capped collection>,
     *   size: <capped size>,
     *   writeConcern: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForCloneCollectionAsCapped(){
        mars.executeCommand("{ cloneCollectionAsCapped: \"person\",\n" +
                "  toCollection: \"PersonCapped\",\n" +
                "  size: 10,\n" +
                "  writeConcern: { w: \"majority\"}\n" +
                "  comment: \"test\"\n" +
                "}");
        Query query = new Query();
        query.with(Sort.on().ascending("name"));

        QueryCursor<Document> person = mars.findAll(query, Document.class, "PersonCapped");
        while (person.hasNext()){
            System.out.println(person.next());
        }

        Collation person1 = Collation.parse("PersonCapped");
        System.out.println(person1.toDocument());
    }

    @After
    public void dropCollection(){
        mars.dropCollection("PersonCapped");
    }

}
