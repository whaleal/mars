package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.option.UpdateOptions;
import org.bson.Document;
import org.testng.annotations.Test;


/**
 * @author wh
 */
public class UpdateTest {

    public static void main( String[] args ) {


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
}
