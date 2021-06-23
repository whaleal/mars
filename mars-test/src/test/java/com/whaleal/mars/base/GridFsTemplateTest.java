package com.whaleal.mars.base;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.whaleal.mars.core.Mars;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
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

    @Test
    public void testFind() {

    }

}
