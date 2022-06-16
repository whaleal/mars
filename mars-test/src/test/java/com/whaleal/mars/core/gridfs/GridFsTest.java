package com.whaleal.mars.core.gridfs;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.whaleal.icefrog.core.collection.LineIter;
import com.whaleal.icefrog.core.io.IoUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author cs
 * @create 2021/4/7
 * @desc
 */
@SpringBootTest
@Slf4j
public class GridFsTest {
    @Autowired
    Mars mars;

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
        bucket.find().forEach(new Consumer< GridFSFile >() {
            @Override
            public void accept( final GridFSFile gridFSFile ) {
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


    @Test
    public void testGridFSBucket() throws IOException {


        String id = "606d762ce3b6d03a7bf11860";
        //根据id查找文件
        GridFSFile gridFSFile = mars.findOneGridFs(Query.query(Criteria.where("_id").is(id)));
        System.out.println("获取到到gridFsFile");
        System.out.println(gridFSFile);
        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFS);
        //获取流中的数据
        LineIter strings = IoUtil.lineIter(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
        while (strings.hasNext()) {
            System.out.println(strings.nextLine());
        }

    }


}
