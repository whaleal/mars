package com.whaleal.mars.core.query;

import com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions;
import com.whaleal.mars.core.aggregation.expressions.MathExpressions;
import com.whaleal.mars.core.aggregation.expressions.impls.SwitchExpression;
import com.whaleal.mars.core.query.updates.UpdateOperator;
import com.whaleal.mars.core.query.updates.UpdateOperators;
import com.whaleal.mars.session.option.UpdateOptions;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

/**
 * @author wh
 */
public class TestUpdatePipeline {

    UpdatePipeline pipeline;
    @Before
    public void init(){
        pipeline = new UpdatePipeline();
    }

    @Test
    public void testBit(){

        pipeline.add(UpdateOperators.or("ceshiOr",19));

        Document updateObject = pipeline.getUpdateObject();

        //Assert.assertEquals(updateObject,Document.parse("{ $bit: { <field>: { <and|or|xor>: <int> } } }"));


        pipeline = new UpdatePipeline();

        pipeline.add(UpdateOperators.and("cc",19));

        System.out.println(pipeline.getUpdateObject());

        pipeline =new UpdatePipeline();

        pipeline.add(UpdateOperators.xor("cc",19));

        System.out.println(pipeline.getUpdateObject());

        pipeline = new UpdatePipeline();
        pipeline.add(UpdateOperators.or("cc",19));

        System.out.println(pipeline.getUpdateObject());

        pipeline = new UpdatePipeline();


        pipeline.add(UpdateOperators.or("cc",19));
        pipeline.add(UpdateOperators.or("cc",19));
        pipeline.add(UpdateOperators.xor("cc",19));

        System.out.println(pipeline.getUpdateObject().get(UpdatePipeline.UpdatePipeline));
    }

    @Test
    public void testAdd(){
        UpdatePipeline updatePipeline = new UpdatePipeline();

        updatePipeline.add(UpdateOperators.set("modified","$$NOW"));

        System.out.println(updatePipeline.getUpdateObject());

        UpdatePipeline pipeline = new UpdatePipeline();

//        pipeline.add(UpdateOperators.set("average",MathExpressions.trunc(AccumulatorExpressions.avg(),0)));
//        pipeline.add(UpdateOperators.set("grade",new SwitchExpression().branch()));

    }
}
