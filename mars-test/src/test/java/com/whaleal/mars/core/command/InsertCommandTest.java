package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.validation.Validator;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-12 21:39
 **/
public class InsertCommandTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @After
    public void dropCollection(){
        mars.dropCollection("users");
    }

    /**
     *db.runCommand(
     *    {
     *       insert: "users",
     *       documents: [
     *          { _id: 2, user: "ijk123", status: "A" },
     *          { _id: 3, user: "xyz123", status: "P" },
     *          { _id: 4, user: "mop123", status: "P" }
     *       ],
     *       ordered: false,
     *       writeConcern: { w: "majority", wtimeout: 5000 }
     *    }
     * )
     */
    @Test
    public void testForBulkInsert(){
        Document document = mars.executeCommand("   {\n" +
                "      insert: \"users\",\n" +
                "      documents: [\n" +
                "         { _id: 2, user: \"ijk123\", status: \"A\" },\n" +
                "         { _id: 3, user: \"xyz123\", status: \"P\" },\n" +
                "         { _id: 4, user: \"mop123\", status: \"P\" }\n" +
                "      ],\n" +
                "      ordered: false,\n" +
                "      writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "   }");

        QueryCursor<Document> users = mars.findAll(new Query(), Document.class, "users");
        while (users.hasNext()){
            System.out.println(users.next());
        }
    }

    //todo createCollection存在问题
    /**
     * db.createCollection("users", {
     *    validator:
     *       {
     *          status: {
     *             $in: [ "Unknown", "Incomplete" ]
     *          }
     *       }
     * })
     *
     * db.runCommand({
     *       insert: "users",
     *       documents: [ {user: "123", status: "Active" } ]
     * })
     *
     * {
     *    n: 0,
     *    writeErrors: [
     *       {
     *          index: 0,
     *          code: 121,
     *          errInfo: {
     *             failingDocumentId: ObjectId('6197a7f2d84e85d1cc90d270'),
     *             details: {
     *                operatorName: '$in',
     *                specifiedAs: { status: { '$in': [Array] } },
     *                reason: 'no matching value found in array',
     *                consideredValue: 'Active'
     *             }
     *          },
     *          errmsg: 'Document failed validation'
     *       }
     *    ],
     *    ok: 1
     * }
     */
    @Test
    public void testForCreateCollection(){
        Criteria in = Criteria.where("status").in("Unknown", "Incomplete");
        mars.createCollection("users", CollectionOptions.empty().validation(CollectionOptions.ValidationOptions.none().validator(Validator.criteria(in))));

        Document document = mars.executeCommand("{\n" +
                "      insert: \"users\",\n" +
                "      documents: [ {user: \"123\", status: \"Active\" } ]\n" +
                "}");

//        Document result = Document.parse("{\n" +
//                "   n: 0,\n" +
//                "   writeErrors: [\n" +
//                "      {\n" +
//                "         index: 0,\n" +
//                "         code: 121,\n" +
//                "         errInfo: {\n" +
//                "            failingDocumentId: ObjectId('6197a7f2d84e85d1cc90d270'),\n" +
//                "            details: {\n" +
//                "               operatorName: '$in',\n" +
//                "               specifiedAs: { status: { '$in': [Array] } },\n" +
//                "               reason: 'no matching value found in array',\n" +
//                "               consideredValue: 'Active'\n" +
//                "            }\n" +
//                "         },\n" +
//                "         errmsg: 'Document failed validation'\n" +
//                "      }\n" +
//                "   ],\n" +
//                "   ok: 1\n" +
//                "}");

//        Assert.assertEquals(document,result);
        System.out.println(document);

    }

}
