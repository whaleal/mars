package com.whaleal.mars.gridfs;

import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import com.whaleal.mars.core.Mars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author cs
 * @create 2021/4/7
 * @desc
 */
@SpringBootTest
@Slf4j
public class GridFsTemplateTest {
    @Autowired
    Mars mars;
    //@Autowired
    //private GridFsTemplate gridFsTemplate;
    //@Autowired
    //private MongoDatabaseFactory factory;
    @Autowired  // 这个是之前在config里配置的
    private GridFSBucket gridFSBucket;

    @Test
    public void testBucketFind() {
        GridFSDownloadStream stream = gridFSBucket.openDownloadStream(new ObjectId("606e967aa64fc91968c4cca4"));
        int fileLength = (int) stream.getGridFSFile().getLength();
        byte[] bytesToWriteTo = new byte[fileLength];
        stream.read(bytesToWriteTo);
        stream.close();

        System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));
    }

    @Test
    public void testBucket() {
        byte[] data = "Data to upload into GridFS".getBytes(StandardCharsets.UTF_8);
        GridFSUploadStream uploadStream = gridFSBucket.openUploadStream("simpleData");
        uploadStream.write(data);
        uploadStream.close();
        System.out.println("The fileId of the uploaded file is: " + uploadStream.getObjectId().toHexString());
        System.out.println(gridFSBucket);
        System.out.println(gridFSBucket.getBucketName());
        System.out.println(gridFSBucket.getChunkSizeBytes());
    }

    @Test
    public void testFindAll() {
        GridFSBucket bucket = GridFSBuckets.create(mars.getDatabase());
        bucket.find().forEach(new Consumer<GridFSFile>() {
            @Override
            public void accept(final GridFSFile gridFSFile) {
                System.out.println(gridFSFile.getFilename());
            }
        });
    }

    @Test
    public void testMongoGridFs() {
        GridFSBucket bucket = GridFSBuckets.create(mars.getDatabase());
        GridFSDownloadStream downloadStream = bucket.openDownloadStream("back");
        int fileLength = (int) downloadStream.getGridFSFile().getLength();
        byte[] bytesToWriteTo = new byte[fileLength];
        downloadStream.read(bytesToWriteTo);
        downloadStream.close();
        System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));
    }

    /*@Test
    public void testGridFSBucket() throws IOException {

        GridFsTemplate template = new GridFsTemplate(mars);
        String id = "606d762ce3b6d03a7bf11860";
        //根据id查找文件
        GridFSFile gridFSFile = template.findOneGridFs(Query.query(Criteria.where("_id").is(id)));
        System.out.println("获取到到gridFsFile");
        System.out.println(gridFSFile);
        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFS);
        //获取流中的数据
        String string = IOUtil.Inputstr2Str_Reader(gridFsResource.getInputStream(),"UTF-8");
        System.out.println(string);
    }*/

    /*@Test
    public void testMarsGrid(){
        GridFsTemplate template = new GridFsTemplate(mars);
        System.out.println("两个template的相等性");
        System.out.println(template == mars.getGridFsTemplate());
        String fieldId = "606d762ce3b6d03a7bf11860";
        GridFSFindIterable iterable = mars.getGridFsTemplate().find(Query.query(Criteria.where("_id").is(fieldId)));
        System.out.println("找到的数据");
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }*/

    /*@Test
    public void testMars() {
        MongoDatabase database = mars.getDatabase();
        System.out.println("取得的数据库名字");
        System.out.println(database.getName());
        GridFsTemplate template = mars.getGridFsTemplate();
        System.out.println(template);
    }*/

    /*@Test
    public void testFactory() {
        MongoDatabase database = factory.getMongoDatabase();
        System.out.println("取得的数据库名字");
        System.out.println(database.getName());
    }*/

    @Test
    public void testFind() {

    }


    /*@Test
    public void testFindOne() {
        //GridFsTemplate gridFsTemplate = new GridFsTemplate(mars);
        *//*String field = "606d710644887a378486e962";
        GridFSFindIterable iterable = gridFsTemplate.find(query(Criteria.where("_id").is(field)));
        iterable.forEach(item-> {
            System.out.println(item);
        });*//*
        //String fieldId = "606d6f2f35f9b56f331c80cf";
        String fieldId = "606d762ce3b6d03a7bf11860";
        GridFSFindIterable iterable = gridFsTemplate.find(Query.query(Criteria.where("_id").is(fieldId)));
        System.out.println("找到的数据");
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }*/

    /*@Test
    public void testInsert() throws FileNotFoundException {
        File file = new File("/Users/cs/Documents/back.jpeg");
        FileInputStream fileInputStream = new FileInputStream(file);
        //参数 content=fileInputStream  filename="测试用例2"  contentType="", 返回fileID
        System.out.println(gridFsTemplate);
        ObjectId objectId = gridFsTemplate.storeGridFs(fileInputStream, "xiaohei", "");
        String fileId = objectId.toString();
        System.out.println(fileId);
    }*/

}
