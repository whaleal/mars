package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.monitor.CollStatsMetrics;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 13:42
 * FileName: CloneCollectionAsCappedTest
 * Description:
 */
public class CloneCollectionAsCappedTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }

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
        mars.executeCommand("{ cloneCollectionAsCapped: \"book\",\n" +
                "  toCollection: \"BookCapped\",\n" +
                "  size: 10,\n" +
                "  writeConcern: { w: \"majority\"}\n" +
                "  comment: \"test\"\n" +
                "}");
        Boolean capped = new CollStatsMetrics(mars.getMongoClient(), "mars", "BookCapped").isCapped();
        Assert.assertEquals(true,capped);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
        mars.dropCollection("BookCapped");
    }

}
