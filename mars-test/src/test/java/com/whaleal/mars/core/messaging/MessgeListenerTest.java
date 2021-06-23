package com.whaleal.mars.core.messaging;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.FullDocument;
import com.whaleal.mars.message.DocumnetMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Corporation;
import com.whaleal.mars.bean.Department;
import com.whaleal.mars.core.Mars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static java.util.Arrays.asList;

/**
 * @author cs
 * @create 2021/3/29
 * @desc
 */
@SpringBootTest
@Slf4j
public class MessgeListenerTest {

    @Autowired
    Mars mars;

    @Autowired
    private MessageListenerContainer messageListenerContainer;


    @Test
    //此方法有bug，无法直接坚定到，正确到操作请看 @link SimpleMessageTest
    public void testServer100() throws InterruptedException {
        Mars mars100 = new Mars(Constant.server100);
        //DocumnetMessageListener listener = new DocumnetMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars100);
        container.start();
        Thread.currentThread().join();
    }


    @Test
    //此方法有bug，无法直接坚定到，正确到操作请看 @link SimpleMessageTest
    public void testMar() throws InterruptedException {
        //System.out.println(mars.getDatabase().getName());
        Mars mars1 = new Mars("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/myrepo");
        //Mars mars = new Mars(Constant.server100);
        DocumnetMessageListener listener = new DocumnetMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars1);
        /*ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();*/
        //container.register(request,Document.class);
        container.start();
        Thread.currentThread().join();
    }

    //@Autowired
    //private MongoTemplate template;
    //这个是可以运行的，使用的基本上就是最原生的
    @Test
    public void testWatch() {
        Mars mars = new Mars("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/myrepo");
        MongoCollection<Document> collection = mars.getDatabase().getCollection("test");
        System.out.println(collection);
        ChangeStreamIterable<Document> documents = collection.watch(asList(Aggregates.match(Filters.in("operationType", asList("insert")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP);
        documents.forEach(doc -> {
            System.out.println(doc);
        });
    }

    @Test
    public void testWatch2() {
        Mars mars = new Mars("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/myrepo");
        MongoCollection<Document> collection = mars.getDatabase().getCollection("test");
        ChangeStreamIterable<Document> documents = mars.getDatabase().watch(asList(Aggregates.match(Filters.in("operationType", asList("insert")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP);
        documents.forEach(doc -> {
            System.out.println(doc);
        });
    }

    @Test
    public void testWatch3() throws InterruptedException {
        System.out.println(mars.getDatabase().getName());
        messageListenerContainer.start();
        Thread.currentThread().join();
    }

    //这是使用Spring整合的,这个测试可以直接启动运行，但是如果在spring容器里面可能会出现一些问题
    /*@Test
    public void testGet() {
        Mars mars = new Mars(MongoClients.create("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/"), "myrepo");
        //Mars mars = new Mars("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/");
        for (; ; ) {
            //创建一个线程
            Executor executor = Executors.newSingleThreadExecutor();
            //创建监听者到容器
            MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mars, executor) {
                @Override
                public boolean isAutoStartup() {
                    return true;
                }
            };
            //创建一个监听者
            MessageListener<ChangeStreamDocument<Document>, Document> listener = new MessageListener<ChangeStreamDocument<Document>, Document>() {
                @Override
                public void onMessage(Message message) {
                    System.out.println("receive message: " + message);
                    log.info("监听到的消息");
                    log.info("Received Message in collection: {},message raw: {}, message body:{}",
                            message.getProperties().getCollectionName(), message.getRaw(), message.getBody());
                }
            };
            MongoDatabase database = mars.getDatabase();
            MongoCollection<Document> collection = database.getCollection("test");
            //System.out.println("Mars的数据库"+database);
            //System.out.println("Mar的集合"+collection);
            ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                    .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                    //TODO
                    //.filter(new AggregationImpl(new DatastoreImpl(database,mars.getMongoClient()),collection))  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                    .filter()
                    .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                    .build();
            messageListenerContainer.register(request, Document.class);
        }
    }*/

    /*@Test
    public void testGet2() {
        for (; ; ) {
            Executor executor = Executors.newSingleThreadExecutor();
            MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mars, executor) {
                @Override
                public boolean isAutoStartup() {
                    return true;
                }
            };
            MessageListener<ChangeStreamDocument<Document>, Document> listener = new MessageListener<ChangeStreamDocument<Document>, Document>() {
                @Override
                public void onMessage(Message message) {
                    System.out.println("receive message: " + message);
                    log.info("监听到的消息");
                    log.info("Received Message in collection: {},message raw: {}, message body:{}",
                            message.getProperties().getCollectionName(), message.getRaw(), message.getBody());
                }
            };
            MongoDatabase database = mars.getDatabase();
            MongoCollection<Document> collection = database.getCollection("test");
            ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                    .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                    .filter(new AggregationImpl(new DatastoreImpl(database, mars.getMongoClient()), collection))  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                    //.filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                    .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                    .build();
            messageListenerContainer.register(request, Document.class);
        }
    }
*/
    @Test
    public void testInsert() {
        //Mars mars = new Mars(MongoClients.create("mongodb://192.168.3.106:37017,192.168.3.106:37018,192.168.3.106:37019/"), "myrepo");
        Corporation corporation = new Corporation();
        corporation.setName("123");
        Department department = new Department();
        department.setName("xx");
        department.setEmployees(Arrays.asList("employee"));
        //corporation.setDepartment(department);
        mars.insert(corporation);
    }
}
