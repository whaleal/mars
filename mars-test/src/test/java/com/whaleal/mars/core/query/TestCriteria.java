package com.whaleal.mars.core.query;

import com.whaleal.mars.core.aggregation.stages.filters.Filter;
import com.whaleal.mars.core.aggregation.stages.filters.Filters;
import org.bson.Document;
import org.testng.annotations.Test;


public class TestCriteria {

    @Test

    public void test01() {

        Criteria criteria = Criteria.where("d").is("dad").and("m").is("mum").and("tall").gt("199").lt("220");

        //Criteria criteria = Criteria.where("d").lt("100").gt("200");

        Document criteriaObject = criteria.getCriteriaObject();

        System.out.println(criteriaObject);


    }


    @Test
    public void test02() {
        Filter a = Filters.eq("a", "bb");


        Filter b = Filters.eq("a", "1100");

        Filter filter = Filters.elemMatch("a", Filters.eq("a", 10), Filters.lt("b", 15));


        System.out.println(filter);
    }


}
