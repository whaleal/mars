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
    public void testFind() {

    }

}
