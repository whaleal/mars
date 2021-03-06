package com.whaleal.mars.issues;

import com.mongodb.client.*;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.DateExpressions;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;

/**
 * @author wh
 */
public class DateToPartsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createData(){
        Document parse = Document.parse("{\n" +
                "  \"_id\" : 2,\n" +
                "  \"item\" : \"abc\",\n" +
                "  \"price\" : 10,\n" +
                "  \"quantity\" : 2,\n" +
                "  \"date\" : ISODate(\"2017-01-01T01:29:09.123Z\")\n" +
                "}");
        mars.insert(parse,"sales");

        Document parse1 = Document.parse("{\n" +
                "   \"_id\" : 1,\n" +
                "   \"item\" : \"abc\",\n" +
                "   \"price\" : 20,\n" +
                "   \"quantity\" : 5,\n" +
                "   \"date\" : ISODate(\"2017-05-20T10:24:51.303Z\")\n" +
                "}");
        mars.insert(parse1,"sales");

    }

    @Test
    public void testFor(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

//        pipeline.project(Projection.of().include("date", DateExpressions.dateToParts(field("date")))
//                .include("date_iso",DateExpressions.dateToParts(new DocumentExpression().field("date",field("date")).field("iso8601",value("true"))))
//                .include("date_timezone",DateExpressions.dateToParts(new DocumentExpression().field("date",field("date")).field("timezone",value("America/New_York")))));

        pipeline.project(Projection.of().include("date", DateExpressions.dateToParts(field("date")))
                .include("date_iso",DateExpressions.dateToParts(new DocumentExpression().field("date",field("date")).field("iso8601",value("true"))))
                .include("date_timezone",DateExpressions.dateToParts(new DocumentExpression().field("date",field("date")).field("timezone",value("America/New_York")))));


        QueryCursor<Document> sales = mars.aggregate(pipeline, "sales");
        while (sales.hasNext()){
            System.out.println(sales.next());
        }

    }

    @Test
    public void testBy(){
        // Replace the uri string with your MongoDB deployment's connection string
        try (MongoClient mongoClient = MongoClients.create(Constant.connectionStr)) {
            MongoDatabase database = mongoClient.getDatabase("mars");

            MongoCollection<Document> sales = database.getCollection("sales");

            List<Document> documentList = new ArrayList<>();
            documentList.add(Document.parse(" {\n" +
                    "    $project: {\n" +
                    "       date: {\n" +
                    "          $dateToParts: { date: \"$date\" }\n" +
                    "       },\n" +
                    "       date_iso: {\n" +
                    "          $dateToParts: { date: \"$date\", iso8601: true }\n" +
                    "       },\n" +
                    "       date_timezone: {\n" +
                    "          $dateToParts: { date: \"$date\", timezone: \"America/New_York\" }\n" +
                    "       }\n" +
                    "    }\n" +
                    "}"));
            MongoCursor<Document> iterator = sales.aggregate(documentList).iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next());
            }


        }
    }

    @Test
    public void testForWrite(){
        DocumentWriter documentWriter = new DocumentWriter();

        documentWriter.writeName("sales");
        documentWriter.writeBoolean(true);
    }


}
