package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 14:49
 * FileName: ConvertToCappedTest
 * Description:
 */
public class ConvertToCappedTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { convertToCapped: <collection>,
     *   size: <capped size>,
     *   writeConcern: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForConvertToCapped(){
        //转换之后不会有原先集合的索引，如果需要要手动创建
        Document document = mars.executeCommand("{ convertToCapped: 'events', size: 8192 }");
        System.out.println(document);
    }
}
