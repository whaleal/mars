package com.whaleal.mars.core.query;

import org.bson.Document;
import org.testng.annotations.Test;


/**
 * @user Lyz
 * @description
 * @date 2022/3/9 15:40
 */
public class TestTextSearch {


    @Test
    public void testForTextSearch() {
        TextCriteria java_coffee_shop = new TextCriteria().matching("java coffee shop");
        TextCriteria java_coffee_shop1 = new TextCriteria().matchingAny("java coffee shop");
        TextCriteria java_coffee_shop2 = new TextCriteria().matchingPhrase("java coffee shop");


        Document criteriaObject = java_coffee_shop.getCriteriaObject();
        Document criteriaObject1 = java_coffee_shop1.getCriteriaObject();
        Document criteriaObject2 = java_coffee_shop2.getCriteriaObject();
        System.out.println(criteriaObject);
        System.out.println(criteriaObject1);
        System.out.println(criteriaObject2);


        //缺少textScore
        TextCriteria java_coffee_shop3 = new TextCriteria().matching("java coffee shop");
    }
}
