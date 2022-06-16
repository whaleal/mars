package com.whaleal.mars.base;

import com.mongodb.client.MongoCursor;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationUtil {


    private static List< Document > getThousandDocuments() throws IOException, ClassNotFoundException {

        File file = new File("src/test/resources/person");


        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }


        List< Document > docs = new ArrayList<>();
        int i = 1000;
        Document doc = null;
        while ((doc = (Document) ois.readObject()) != null) {


            docs.add(doc);
            i--;

            if (i <= 0) {
                break;

            }
        }


        try {
            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return docs;

    }


    public static List< Document > getDocuments() {
        List< Document > thousandDocuments = null;
        try {
            thousandDocuments = SerializationUtil.getThousandDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return thousandDocuments;

    }

    public static void saveObjToFile() {
        try {
            //写对象流的对象
            File file = new File("src/test/resources/person");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

            Mars mars = new Mars(Constant.connectionStr);

            MongoCursor< Document > person = mars.getCollection(Document.class, "person").find(new Document()).iterator();

            while (person.hasNext()) {

                oos.writeObject(person.next());

            }

            //将Person对象p写入到oos中

            oos.close();                        //关闭文件流
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        try {
            SerializationUtil.getThousandDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}