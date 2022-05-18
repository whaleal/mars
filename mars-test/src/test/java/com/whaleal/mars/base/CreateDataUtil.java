package com.whaleal.mars.base;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 14:06
 */
public class CreateDataUtil {

    public static List<Document> parseString(String s){
        String[] split = s.split("\n");

        ArrayList<Document> documents = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            documents.add(Document.parse(split[i]));
        }
        return documents;
    }
}
