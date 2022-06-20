package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:32
 * FileName: SetFeatureCompatibilityVersionTest
 * Description:
 */
public class SetFeatureCompatibilityVersionTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand( {
     *    setFeatureCompatibilityVersion: <version>,
     *    writeConcern: { wtimeout: <timeout> }
     * } )
     */
    @Test
    public void testForViewFeatureCompatibilityVersion(){
        Document document = mars.executeCommand("{ getParameter: 1, featureCompatibilityVersion: 1 }");
        System.out.println(document);
    }

    @Test
    public void testForSetFeatureCompatibilityVersion(){
        Document document = mars.executeCommand("{ setFeatureCompatibilityVersion: \"5.0\" }");
        System.out.println(document);
    }

    @Test
    public void testForSetWriteConcernTimeout(){
        Document document = mars.executeCommand("{\n" +
                "   setFeatureCompatibilityVersion: \"5.0\",\n" +
                "   writeConcern: { wtimeout: 5000 }\n" +
                "}");
        System.out.println(document);
    }
}
