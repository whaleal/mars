package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.Redact;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.whaleal.mars.core.aggregation.expressions.ArrayExpressions.array;
import static com.whaleal.mars.core.aggregation.expressions.ArrayExpressions.size;
import static com.whaleal.mars.core.aggregation.expressions.ComparisonExpressions.gt;
import static com.whaleal.mars.core.aggregation.expressions.ConditionalExpressions.condition;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.SetExpressions.setIntersection;


/**
 * @author lyz
 * @desc
 * @date 2022-05-17 16:57
 */
public class RedactTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{\n" +
                "  _id: 1,\n" +
                "  title: \"123 Department Report\",\n" +
                "  tags: [ \"G\", \"STLW\" ],\n" +
                "  year: 2014,\n" +
                "  subsections: [\n" +
                "    {\n" +
                "      subtitle: \"Section 1: Overview\",\n" +
                "      tags: [ \"SI\", \"G\" ],\n" +
                "      content:  \"Section 1: This is the content of section 1.\"\n" +
                "    },\n" +
                "    {\n" +
                "      subtitle: \"Section 2: Analysis\",\n" +
                "      tags: [ \"STLW\" ],\n" +
                "      content: \"Section 2: This is the content of section 2.\"\n" +
                "    },\n" +
                "    {\n" +
                "      subtitle: \"Section 3: Budgeting\",\n" +
                "      tags: [ \"TK\" ],\n" +
                "      content: {\n" +
                "        text: \"Section 3: This is the content of section3.\",\n" +
                "        tags: [ \"HCS\" ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Document parse = Document.parse(s);
        mars.insert(parse,"forecasts");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("forecasts");
    }


    /**
     * var userAccess = [ "STLW", "G" ];
     * db.forecasts.aggregate(
     *    [
     *      { $match: { year: 2014 } },
     *      { $redact: {
     *         $cond: {
     *            if: { $gt: [ { $size: { $setIntersection: [ "$tags", userAccess ] } }, 0 ] },
     *            then: "$$DESCEND",
     *            else: "$$PRUNE"
     *          }
     *        }
     *      }
     *    ]
     * );
     */
    @Test
    public void testForEvaluatePermissions(){
        pipeline.match(Filters.eq("year",2014));
        Expression expression = setIntersection(field("tags"), array(value("STLW"),value("G")));
        pipeline.redact(Redact.on(condition(gt(size(expression),value(0)), value("$$DESCEND"),value("$$PRUNE"))));

        Object forecasts = mars.aggregate(pipeline, "forecasts").tryNext();

        Assert.assertNotNull(forecasts);
    }

}
