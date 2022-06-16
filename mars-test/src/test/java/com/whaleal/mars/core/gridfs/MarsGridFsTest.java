package com.whaleal.mars.core.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.InsertOneOptions;
import com.whaleal.mars.session.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cs
 * @create 2021/4/8
 * @desc
 */
@SpringBootTest
@Slf4j
public class MarsGridFsTest {
    private static final String bucketName = "xx";
    private static final String m1 = "/Users/cs/Documents/xxxx.jpeg";
    private static final String m300 = "/usr/local/tmp/xxx.rar";
    private static final String g2 = "";
    private static final String m300Id = "6080dd5e878eb9205b437f7f";
    @Autowired
    Mars mars;

    @Test
    public void testRegex() {
        String pattern = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher("str123");
        System.out.println(m.matches());
        String str = "1";
        Precondition.state(str == null || Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{0,15}$").matcher(str).matches(), "匹配错误");
    }

    @Test
    public void testCheckBucket() throws IOException {
        System.out.println("mars的bucket");
        //System.out.println(mars.getBucketName());
        File file = new File("/Users/cs/Documents/11.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        //参数 content=fileInputStream  filename="测试用例2"  contentType="", 返回fileID
        ObjectId objectId = mars.storeGridFs(fileInputStream, "change", new Document("delete", true), "xx");
        String fileId = objectId.toString();
        System.out.println(fileId);
        fileInputStream.close();
        //mars.setBucketName("122 mybucket");
        System.out.println("数据库切换了");
        FileInputStream fileInputStream2 = new FileInputStream(file);
        //参数 content=fileInputStream  filename="测试用例2"  contentType="", 返回fileID
        ObjectId objectId2 = mars.storeGridFs(fileInputStream2, "change", new Document("delete", true), "xx");
        String fileId2 = objectId2.toString();
        System.out.println("存储成功了");
        //System.out.println(mars.getBucketName());
        System.out.println(fileId2);
        fileInputStream2.close();

    }

    @Test
    public void testDelByQuery() {
        mars.deleteGridFs(Query.query(Criteria.where("_id").is(new ObjectId("606c2bd63b073106df70cc9e"))));
    }

    @Test
    public void testDelById() {
        mars.deleteGridFs(new ObjectId("608011db84316b43f412d1f8"), "fs");
    }

    @Test
    public void testBucketFind() throws IOException {
        /*System.out.println(bucket.getBucketName());
        GridFSDownloadStream stream = bucket.openDownloadStream("xiaohei");
        int fileLength = (int) stream.getGridFSFile().getLength();
        byte[] bytesToWriteTo = new byte[fileLength];
        stream.read(bytesToWriteTo);
        stream.close();
        System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));*/
        String id = m300Id;
        System.out.println("得到id" + m300Id);
        //根据id查找文件
        GridFSFile gridFSFile = mars.findOneGridFs(Query.query(Criteria.where("_id").is(new ObjectId(id))), "xx");
        //打开下载流对象
        GridFsResource resource = mars.getResource(gridFSFile, bucketName);
        //创建gridFsSource，用于获取流对象
        //获取流中的数据
        InputStream input = resource.getInputStream();
        //IOUtil.Inputstr2Str_Reader(resource.getInputStream(), "UTF-8");
        String destination = "/usr/local/tmp/xx.rar";
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
        System.out.println("完成了" + LocalTime.now());
    }

    @Test
    public void testNormalInsert() {
        String str = "ceshi1";
        mars.insert(str);
    }

    @Test
    public void testInsert() {
        String str = "测试插入";
        InsertOneResult result = mars.insert(str, new InsertOneOptions(), "fs.files");
        System.out.println("插入结果");
        System.out.println(result);
    }

    @Test
    public void updateGridFs() {
        String filedId = "606ea28f0e9b070ef23e9d98";
        UpdateResult result = mars.getDatabase().getCollection("fs.files").updateOne(Filters.eq("_id", new ObjectId(filedId)), Updates.set("metadata.delete", true));
        System.out.println("更新的结果");
        System.out.println(result);
    }

    @Test
    public void testFindNormal() {
        FindIterable< Document > documents = mars.getDatabase().getCollection("fs.files").find();
        documents.forEach(document -> {
            System.out.println(document);
        });
    }

    @Test
    public void testUpdateMetadata() {
        String fieldId = "606ed612bbb89b03e366dc7d";
        MongoDatabase database = mars.getDatabase();
        System.out.println("获取到到数据库" + database);
        System.out.println(database.getName());
        Optional< BasicDBObject > document = mars.findOne(Query.query(Criteria.where("_id").is(new ObjectId(fieldId))), BasicDBObject.class, "fs.files");
        System.out.println(document);
        //System.out.println(document.get());

    }

    @Test
    public void testFindByMetadata() {
        Document document = new Document("delete", false);
        //GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("metadata").is(document)));
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("metadata.delete").is(true)));
        System.out.println("找到的数据");
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testFindByName() {
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("filename").is("xx")), bucketName);
        System.out.println("找到的数据");
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    public void testFindById() {
        String fieldId = "60d17fb23465890d95361ad6";
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("_id").is(new ObjectId(fieldId))), bucketName);
        System.out.println("找到的数据");
        System.out.println(iterable);
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }

    //60d17f6bc6cbba23b99aca1c   60d17fb23465890d95361ad6
    @Test
    public void testInsertGridFs() throws FileNotFoundException {
        File file = new File(m1);
        FileInputStream fileInputStream = new FileInputStream(file);
        //参数 content=fileInputStream  filename="测试用例2"  contentType="", 返回fileID
        System.out.println(mars);
        String name = mars.getDatabase().getName();
        System.out.println(name);
        ObjectId objectId = mars.storeGridFs(fileInputStream, "xx", new Document("delete", true), bucketName);
        String fileId = objectId.toString();

        System.out.println(fileId);
    }

}
