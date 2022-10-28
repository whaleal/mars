package com.whaleal.mars.monitor;

import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2022-09-08 13:42
 **/
public class BuildInfoMetrics {

    private static Document document ;

    public BuildInfoMetrics(MongoClient mongoClient){
        document = mongoClient.getDatabase("admin").runCommand(new Document("buildInfo", 1));
    }

    public String getVersion(){
        return document.get("version",String.class);
    }

    public String getGitVersion(){
        return document.getString("gitVersion");
    }

    public String getSysInfo(){
        return document.getString("sysInfo");
    }

    public String getLoaderFlags(){
        return document.getString("loaderFlags");
    }

    public String getCompilerFlags(){
        return document.getString("compilerFlags");
    }

    public String getAllocator(){
        return document.getString("allocator");
    }

    public List getVersionArray(){
        return document.get("versionArray", List.class);
    }

    public Document getOpenssl(){
        return document.get("openssl",Document.class);
    }

    public String getJavascriptEngine(){
        return document.getString("javascriptEngine");
    }

    public int getBits(){
        return document.getInteger("bits");
    }

    public boolean getDebug(){
        return document.getBoolean("debug");
    }

    public int getMaxBsonObjectSize(){
        return document.getInteger("maxBsonObjectSize");
    }

    public List getStorageEngines(){
        return document.get("storageEngines", List.class);
    }

    public double getOK(){
        return document.getDouble("ok");
    }

}
