package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;
import org.junit.Test;

import java.util.List;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 16:47
 * FileName: CreateTest
 * Description:
 */
public class CreateTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   create: <collection or view name>,
     *   capped: <true|false>,
     *   timeseries: {
     *      timeField: <string>,
     *      metaField: <string>,
     *      granularity: <string>
     *   },
     *   expireAfterSeconds: <number>,
     *   autoIndexId: <true|false>,
     *   size: <max_size>,
     *   max: <max_documents>,
     *   storageEngine: <document>,
     *   validator: <document>,
     *   validationLevel: <string>,
     *   validationAction: <string>,
     *   indexOptionDefaults: <document>,
     *   viewOn: <source>,
     *   pipeline: <pipeline>,
     *   collation: <document>,
     *   writeConcern: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForCreateCappedCollection(){
        Document document = mars.executeCommand("{ create: \"testCollection\", capped: true, size: 65535 }");
        System.out.println(document);
    }

    //todo Create a Time Series Collection no such command

    @Test
    public void testForCreateView(){
//        mars.createCollection("survey", CollectionOptions.just(Collation.of("zh")));

        String s = "{ _id: 1, empNumber: \"abc123\", feedback: { management: 3, environment: 3 }, department: \"A\" }\n" +
                "{ _id: 2, empNumber: \"xyz987\", feedback: { management: 2, environment: 3 }, department: \"B\" }\n" +
                "{ _id: 3, empNumber: \"ijk555\", feedback: { management: 3, environment: 4 }, department: \"A\" }";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"survey ");
        Document document = mars.executeCommand(" {\n" +
                "   create: \"managementFeedback\",\n" +
                "   viewOn: \"survey\",\n" +
                "   pipeline: [ { $project: { \"management\": \"$feedback.management\", department: 1 } } ]\n" +
                "} ");
        System.out.println(document);
    }

    //todo 创建视图Specify Collation未测

    //todo Specify Storage Engine Options未测

}
