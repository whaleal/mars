package com.whaleal.mars.base;

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

    public static List<Document> createDocument() throws IOException, ClassNotFoundException {

        List<Document> doc = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "src/test/resources/zipCodes"));
            String line = reader.readLine();
            while (line != null) {
                doc.add(Document.parse(line));
//                System.out.println(line);
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
        try {
            List<Document> document = createDocument();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
