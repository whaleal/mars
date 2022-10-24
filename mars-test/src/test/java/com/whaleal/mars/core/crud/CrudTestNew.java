package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Articles;
import com.whaleal.mars.bean.Num;
import com.whaleal.mars.bean.Parent;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-24 13:04
 **/
public class CrudTestNew {

    private Mars mars = new Mars(Constant.connectionStr);
    List<Num> nums = new ArrayList<>();


    @Test
    public void createData(){
        for (int i = 0;i < 20; i++){
            Num num = new Num();
            num.setName(i + "hao");
            num.setNum(i);
            nums.add(num);
        }

        mars.insertAll(nums);
    }


    @AfterMethod
    public void drop(){
//        mars.dropCollection(Articles.class);
//        mars.dropCollection("articles1");
//        mars.dropCollection(Parent.class);
    }

    @Test
    public void testForInsertOne(){
        Articles articles = new Articles(100,"ceshi","lyz",5);
        Articles insert = mars.insert(articles);
        System.out.println(insert);

        Articles articles1 = mars.insert(articles, "articles1");
        System.out.println(articles1);

        Articles articles2 = mars.findAll(Articles.class).tryNext();
        Assert.assertEquals(insert,articles2);

        Object articles11 = mars.findAll(null, "articles1").tryNext();
        Assert.assertEquals(articles1,articles11);
    }

    @Test
    public void testForInsertMany(){

        ArrayList<Object> objects = new ArrayList<>();
        for(int i = 0; i < 3 ; i++ ){

            Articles articles = new Articles(i,"ceshi","lyz",5);
            objects.add(articles);
        }
        Collection<Object> insert = mars.insert(objects, Articles.class);
        for (Object o : insert){
            System.out.println(o);
        }
        Assert.assertEquals(insert,objects);

        Collection<Object> articles1 = mars.insert(objects, "articles1");
        for (Object o : articles1) {
            System.out.println(o);
        }
        Assert.assertEquals(articles1,objects);

    }

    @Test
    public void testForInsertAll(){
        ArrayList<Object> objects = new ArrayList<>();
        for(int i = 0; i < 3 ; i++ ){

            Articles articles = new Articles(i,"ceshi","lyz",5);
            objects.add(articles);
        }
        for(int i = 0; i < 3 ; i++ ){
            Parent parent = new Parent();
            parent.setAge(40);
            parent.setName(i + "");
            objects.add(parent);
        }

        Collection<Object> objects1 = mars.insertAll(objects);
        for (Object o : objects1){
            System.out.println(o);
        }
        Assert.assertEquals(objects,objects1);
    }

    @Test
    public void testForFindAll() {
        QueryCursor<Num> all = mars.findAll(Num.class);
        List<Num> nums1 = mars.findAll(Num.class).toList();

        while (all.hasNext()) {
            System.out.println(all.next());
        }

        System.out.println("************");

//        Assert.assertEquals(nums1,nums);

        QueryCursor<Object> num = mars.findAll(null, "num");
        while (num.hasNext()) {
            System.out.println(num.next());
        }

    }

    @Test
    public void testForFindByClass(){
        Criteria num1 = Criteria.where("num").gte(10);
        Query query = Query.query(num1);
        query.with(Sort.descending("num"));

        System.out.println("-------------------");
        QueryCursor<Num> numQueryCursor = mars.find(query, Num.class);
        while (numQueryCursor.hasNext()) {
            System.out.println(numQueryCursor.next());
        }

        Optional<Num> one = mars.findOne(query, Num.class);
        System.out.println(one.get());

    }

    @Test
    public void testForFindByName(){
        Criteria num2 = Criteria.where("num").lt(10);
        QueryCursor<Document> num = mars.find(Query.query(num2), Document.class, "num");

        while (num.hasNext()){
            System.out.println(num.next());
        }

        Optional<Document> one = mars.findOne(Query.query(num2), Document.class,"num");
        System.out.println(one.get());
    }

}


