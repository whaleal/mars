package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
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
        update.push("cc").each("1", 2, "lyz", "lhp");

        Document updateObject = update.getUpdateObject();

        mars.update(new Query(), update, "cc", new UpdateOptions().upsert(true));
    }


    @Test
    public void testAddtoSet() {

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
}
