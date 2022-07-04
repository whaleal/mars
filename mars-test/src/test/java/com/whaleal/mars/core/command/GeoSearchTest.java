package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-06-10 14:13
 */
public class GeoSearchTest {


    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createCollections(){
        String s = "{\"name\": \"Central Park\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.97, 40.77 ] },\"category\": \"Parks\",\"legacy\" : [ -73.9375, 40.8303 ],}\n" +
                "{\"name\": \"Sara D. Roosevelt Park\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.9928, 40.7193 ] },\"category\": \"Parks\",\"legacy\" : [ -73.9375, 40.8303 ],},\n" +
                "{\"name\": \"Polo Grounds\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.9375, 40.8303 ] },\"category\": \"Stadiums\",\"legacy\" : [ -73.9375, 40.8303 ],}";

        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"places");
    }

    @After
    public void dropCollections(){
        mars.dropCollection("places");
    }

    /**
     * db.runCommand({
     *    geoSearch : "places",
     *    near: [ -73.9667, 40.78 ],
     *    maxDistance : 6,
     *    search : { type : "restaurant" },
     *    limit : 30
     * })
     */
    //todo 运行报错  {"ok": 0.0, "errmsg": "no such command: 'geoSearch'", "code": 59, "codeName": "CommandNotFound"}
    @Test
    public void testForGeoSearch(){

        Document document = mars.executeCommand("{\n" +
                "   geoSearch : \"places\",\n" +
                "   near: [ -73.9667, 40.78 ],\n" +
                "   maxDistance : 6,\n" +
                "   search : { type : \"restaurant\" },\n" +
                "   limit : 30\n" +
                "}");
        System.out.println(document);
    }

//    @Test
//    public void testForReadConcern(){
//
//    }
}
