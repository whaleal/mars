package com.whaleal.mars.core.command;

import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:32
 * FileName: SetFeatureCompatibilityVersionTest
 * Description:
 */
public class SetFeatureCompatibilityVersionTest {
    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * db.adminCommand( {
     *    setFeatureCompatibilityVersion: <version>,
     *    writeConcern: { wtimeout: <timeout> }
     * } )
     */
    @Test
    public void testForViewFeatureCompatibilityVersion(){
        Document document = mars.executeCommand("{ getParameter: 1, featureCompatibilityVersion: 1 }");
        Document result = Document.parse("{ \"featureCompatibilityVersion\" : { \"version\" : \"5.0\" }, \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSetFeatureCompatibilityVersion(){
        Document document = mars.executeCommand("{ setFeatureCompatibilityVersion: \"5.0\" }");
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSetWriteConcernTimeout(){
        Document document = mars.executeCommand("{\n" +
                "   setFeatureCompatibilityVersion: \"5.0\",\n" +
                "   writeConcern: { wtimeout: 5000 }\n" +
                "}");
        Document result = Document.parse("{ \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }
}
