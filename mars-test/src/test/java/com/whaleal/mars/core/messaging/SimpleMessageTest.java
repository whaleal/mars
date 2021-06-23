package com.whaleal.mars.core.messaging;

import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.message.DocumnetMessageListener;

/**
 * 不需要容器的changeStream的启动方法
 */
public class SimpleMessageTest {

    @Test
    public void testServer100() throws InterruptedException {
        Mars mars100 = new Mars(Constant.server100);
        //DocumnetMessageListener listener = new DocumnetMessageListener();
        DocumnetMessageListener listener = new DocumnetMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars100);
        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request,Document.class);
        container.start();
        Thread.currentThread().join();
    }

    @Test
    public void testMar() throws InterruptedException {
        //System.out.println(mars.getDatabase().getName());
        Mars mars1 = new Mars("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/myrepo");
        //Mars mars = new Mars(Constant.server100);
        DocumnetMessageListener listener = new DocumnetMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars1);
        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request,Document.class);
        container.start();
        Thread.currentThread().join();
    }
}
