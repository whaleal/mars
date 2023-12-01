package com.whaleal.mars.util;

import org.bson.Document;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-11 22:45
 */
public class ZipCodesUtil {

    public static List<Document> createDocument(){

        List<Document> doc = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "src/test/resources/zipCodes"));
            String line = reader.readLine();
            while (line != null) {
                doc.add(Document.parse(line));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    @Test
    public void test(){

        List<Document> document = createDocument();

    }
}
