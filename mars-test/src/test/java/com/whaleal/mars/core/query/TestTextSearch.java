package com.whaleal.mars.core.query;

import com.whaleal.icefrog.core.bean.BeanUtil;
import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Articles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @user Lyz
 * @description
 * @date 2022/3/9 15:40
 */
public class TestTextSearch {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Articles.class);
        mars.createIndex(new Index().on("subject", IndexDirection.TEXT),"articles");
        mars.insert(new Articles(1,"coffee","xyz",50));
        mars.insert(new Articles(2,"Coffee Shopping","efg",5));
        mars.insert(new Articles(3,"Baking a cake","abc",90));
        mars.insert(new Articles(4,"baking a coffee","xyz",100));
        mars.insert(new Articles(5,"Café Con Leche","abc",200));
        mars.insert(new Articles(6,"Сырники","jkl",80));
        mars.insert(new Articles(7,"coffee and cream","efg",10));
        mars.insert(new Articles(8,"Cafe con Leche","xyz",10));

    }

    /**
     * {
     *   $text:
     *     {
     *       $search: <string>,
     *       $language: <string>,
     *       $caseSensitive: <boolean>,
     *       $diacriticSensitive: <boolean>
     *     }
     * }
     */
    @Test
    public void testForTextSearchWord() {
//        TextCriteria java_coffee_shop = new TextCriteria().matching("java coffee shop");
//        TextCriteria java_coffee_shop1 = new TextCriteria().matchingAny("java coffee shop");
//        TextCriteria java_coffee_shop2 = new TextCriteria().matchingPhrase("java coffee shop");
//
//
//        Document criteriaObject = java_coffee_shop.getCriteriaObject();
//        Document criteriaObject1 = java_coffee_shop1.getCriteriaObject();
//        Document criteriaObject2 = java_coffee_shop2.getCriteriaObject();
//        System.out.println(criteriaObject);
//        System.out.println(criteriaObject1);
//        System.out.println(criteriaObject2);
//
//
//        //缺少textScore
//        TextCriteria java_coffee_shop3 = new TextCriteria().matching("java coffee shop")

        Query query = new Query(new TextCriteria().matching("coffee"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForTextSearchWords() {
        Query query = new Query(new TextCriteria().matchingAny("coffee","baking"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForTextSearchPhrase() {
        Query query = new Query(new TextCriteria().matchingPhrase("coffee shop"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForExcludeTerm(){
        Query query = new Query(new TextCriteria().matching("cake").notMatching("coffee"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }
    @Test
    public void testForDiffLanguage(){
        TextCriteria criteria = TextCriteria.forLanguage("es");
        criteria.matching("leche");
        Query query = new Query(criteria);
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForCaseInsensitive(){
        Query query = new Query(new TextCriteria().caseSensitive(true).matching("coffee"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForDiacriticSensitive(){
        Query query = new Query(new TextCriteria().diacriticSensitive(true).matching("CAFÉ"));
        QueryCursor<Articles> queryCursor = mars.findAll(query, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForTextSearchScore(){
        //todo textQuery不添加Sort条件,爆空指针
        TextQuery textQuery = new TextQuery(new TextCriteria().matching("cake"));
        textQuery.includeScore("score");
        QueryCursor<Articles> queryCursor = mars.findAll(textQuery, Articles.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForTextSearchScoreWithSort() {
        TextQuery textQuery = new TextQuery(new TextCriteria().matching("coffee"));
        textQuery.includeScore("score");
//        textQuery.withProjection(new Projection().include("score"));
        textQuery.sortByScore();
        QueryCursor<Articles> queryCursor = mars.findAll(textQuery, Articles.class);
        while (queryCursor.hasNext()) {
            System.out.println(queryCursor.next());
        }

    }

    @Test
    public void testForTextSearchWithAnotherQuery(){
        TextQuery textQuery = new TextQuery(new TextCriteria().matching("coffee bake"));
        //todo sort 空指针问题，includeScore 不生效
        textQuery.addCriteria(Criteria.where("author").is("xyz"));
        QueryCursor<Articles> queryCursor = mars.findAll(textQuery, Articles.class);
        while (queryCursor.hasNext()) {
            System.out.println(queryCursor.next());
        }
    }

    @Test
    public void testForTextSearchScoreLimit(){
        //todo 和sort一起使用
    }

    @After
    public void dropCollection(){
        mars.dropCollection("articles");
    }
}
