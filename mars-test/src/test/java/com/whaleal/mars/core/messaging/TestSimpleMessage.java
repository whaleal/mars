package com.whaleal.mars.core.messaging;

import com.mongodb.client.model.changestream.FullDocument;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.jupiter.api.Test;

/**
 * 不需要容器的changeStream的启动方法
 */
public class TestSimpleMessage {


    @Test
    public void testChangeStream() throws InterruptedException {

        Mars mars = new Mars(Constant.connectionStr);
        DocumnetMessageListener listener = new DocumnetMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars);
        ChangeStreamRequest< Document > request = ChangeStreamRequest.builder(listener)
                .collection("stu")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request, Document.class);
        container.start();
        Thread.currentThread().join();
    }
}
