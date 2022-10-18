package com.whaleal.mars.core.crud;

import com.mongodb.client.result.UpdateResult;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.UpdateOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.testng.annotations.Test;


/**
 * @author wh
 */
public class UpdateTest {

    private Mars mars = new Mars(Constant.connectionStr);

    public static void main( String[] args ) {


    }

    @Before
    public void createCollection(){
        mars.insert(Document.parse("{ _id: 1, letters: [\"a\", \"b\"] }"),"letters");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("letters");
    }


    @Test
    public void testPush() {

        Mars mars = new Mars(Constant.connectionStr);


        Update update = new Update();
        update.push("cc").each("1", 2, "lyz", "lhp","kkk");


        System.out.println("++++++++++++++++++++++++++++++++++++++");
        Document updateObject = update.getUpdateObject();

        mars.update(new Query(), update, "cc", new UpdateOptions().upsert(false));
    }

    @Test
    public void UpdateOptionByCollectionName() {

        Mars mars = new Mars(Constant.connectionStr);


        Update update = new Update();
        update.push("cc").each("1", 2, "lyz", "lhp","kkk");


        System.out.println("++++++++++++++++++++++++++++++++++++++");
        Document updateObject = update.getUpdateObject();

        mars.update(new Query(), update, "cc", new UpdateOptions().upsert(false));
    }

    @Test
    public void testUpdateOptionByEntityAndCollectionName() {

        Update update = new Update();
        update.addToSet("key").each(667);

        mars.update(new Query(new Criteria("_id").is("3001")),update, Animal.class,new UpdateOptions(),"cc");
    }

    @Test
    public void testUpdateByEntity() {

        Update update = new Update();
        update.set("cc",1);

        UpdateResult id = mars.update(new Query(new Criteria("_id").is("6001")), update, Animal.class);
        System.out.println(id);
    }


    @Test
    public void testUpdateByEntit1y() {

        Update update = new Update();
        update.set("cc",1);

        UpdateResult id = mars.update(new Query(new Criteria("_id").is("6001")), update, Animal.class);
        System.out.println(id);
    }


    @Test
    public void testAddToSet() {

        Update update = new Update();
        update.addToSet("key").each("1", 2, "3");

        System.out.println(update.getUpdateObject());


    }

    @Test
    public void testForAddToSet(){
        Criteria id = Criteria.where("id").is(1);

        Query query = new Query(id);

        QueryCursor<Document> letters = mars.findAll(query, Document.class, "letters");
        while (letters.hasNext()){
            System.out.println(letters.next());
        }
    }




    @Test
    public void updateEntityOptionByCollectionName(){
        Criteria id = Criteria.where("_id").is("3001");

        Query query = new Query(id);

        Animal animal = new Animal();
        animal.setAge(123);
        animal.setBook(new Book());

        mars.updateEntity(query,animal,new UpdateOptions(),"cc");
    }


    @Test
    public void updateEntity(){
        Criteria id = Criteria.where("_id").is("4001");

        Query query = new Query(id);


        Book book = new Book();
        book.setName("book");

        Animal animal = new Animal();
        animal.setAge(123);
        animal.setBook(book);

        mars.updateEntity(new Query(),animal);
    }


    @Test
    public void updateEntityByCollectionName(){
        Book book = new Book();
        book.setName("book");

        Animal animal = new Animal();
        animal.setAge(432);
        animal.setBook(book);

        mars.updateEntity(new Query(),animal,"cc");
    }

    @Test
    public void updateEntityOption(){
        Criteria id = Criteria.where("_id").is("4001");
        Query query = new Query(id);

        Book book = new Book();
        book.setName("book");
        Animal animal = new Animal();
        animal.setAge(123345);
        animal.setBook(book);
        mars.updateEntity(new Query(),animal,new UpdateOptions());
    }

    @Test
    public void updateFirst(){
        Criteria id = Criteria.where("_id").is("4001");
        Query query = new Query(id);

        Book book = new Book();
        book.setName("book");
        Animal animal = new Animal();
        animal.setAge(12332145);
        animal.setBook(book);

//        mars.updateFirst(new Query());
    }


}
