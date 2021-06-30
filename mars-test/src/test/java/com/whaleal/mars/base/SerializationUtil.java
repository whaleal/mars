package com.whaleal.mars.base;

import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializationUtil {


    private static List<Document> getThousandDocuments() throws IOException, ClassNotFoundException {

        File file = new File("src/test/resources/personDocument");


        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = null;


        List<Document> docs = new ArrayList<>();
        int i = 1000 ;

        while ((doc =(Document)ois.readObject())!=null ){
            docs.add(doc);
            i -- ;

            if(i <=0){
                break;

            }
        }


        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return docs ;

    }




    public static List<Document>   getDocuments()  {
        List<Document> thousandDocuments = null;
        try {
            thousandDocuments = SerializationUtil.getThousandDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return thousandDocuments;

    }
}