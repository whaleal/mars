> 作者：[@恒曳](https://github.com/HbnKing)   [@拽拽](https://github.com/zzbka)  



# 快速入门
## 背景
为什么会有这个项目，现阶段spring-data-mongo 封装较重，性能损耗较大，升级改动也较大，开发成本高。  
现阶段亟需一款稳定的成熟的轻量的ORM框架针对MongoDB基本操作进行封装，同时具备高性能，高容错性的特点。

## 简介
`Mars` 是一款 `MongoDB` 的驱动层框架，采用 `Java` 语言开发，在原生的驱动基础功能上做了进一步的封装，让操作变得更加方便快捷。
> **目标**
> 我们希望 `Mars` 框架将来可以整合多个数据库，对不同的数据平台提供统一的驱动操作。让用户能够达到只用Mars就可以对多种不同类型数据库进行操作

##  版本信息 
目前已发布至 0.4.0
请修改相关pom  文件 


### 特点

- 快速：Mars框架对于底层的数据映射操作做了一系列的优化，对于大数据量的部分操作速度对比Spring会有部分提高
- 便捷：可以很轻松的在项目中引入Mars框架并使用Mars框架的功能，而不需要关注底层的实现
- 适配：可以在多种环境下使用Mars框架，一个普通的Java项目可以使用，一个SpringBoot项目也可以使用
### 代码托管
> [GitHub](https://github.com/whaleal/mars.git)
>
> Mars已经发布至maven中央仓库，所以可以通过maven中央仓库或者阿里云的镜像仓库来下载我们的项目
>
> 用户也可以通过本项目github地址来下载使用

### 项目的整体架构



![整体架构图](./images/img.png)

## 性能对比

> 由于本框架底层采用Bson数据流进行传输，所以对于数据的批量插入和删除在性能上较Spring 相比会有一定的提升，数据单位为毫秒，具体的数据如下  

<img src="./images/性能对比.png" alt="性能对比" style="zoom:200%;" />

> 性能对比折线图
>
> 注： 柱状图横坐标 (蓝色)为Mars性能测试，(橙色)为spring-data-mongo性能测试
>
> 纵坐标单位时间为毫秒

<img src="./images/img_1.png" alt="性能对比图" />

<img src="./images/img_2.png" alt="性能对比图2" />

<img src="./images/img_3.png" alt="性能对比图3" />

<img src="./images/img_4.png" alt="性能对比图3" />



## 在项目中引入Mars

接下来将通过一个简易的 Demo 来阐述 `Mars` 的强大功能，在此之前，我们假设您已经：

- 有一定的 Java 基础
- 熟悉 Spring Boot
- 熟悉 Maven

一个简单的java项目想要使用 `Mars` ,那么只需要引入依赖

```xml
<dependency>
    <groupId>com.whaleal.mars</groupId>
    <artifactId>mars-core</artifactId>
    <version>0.1.2</version>
</dependency>
```

然后可以通过传递一个连接字符串来创建一个`Mars`对象

```java
//mongodb://用户名：密码@mongoDB服务器1的IP：端口，mongoDB服务器1的IP：端口/数据库?附带拼接字符串
Mars mars = new Mars("mongodb://root:123456@server100:37017,server100:37018,server100:37019/myrepo?authSource=admin");
```

`SpringBoot`项目中引入最新版本的`Mars`：

```xml
<dependency>
    <groupId>com.whaleal.mars</groupId>
    <artifactId>mars-springboot</artifactId>
    <version>0.4.0</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.whaleal.mars</groupId>
    <artifactId>mars-core</artifactId>
    <version>0.4.0</version>
</dependency>
```

## 配置Entity
在 `application.properties` 配置文件中添加Mars框架的配置信息：
```shell
mars.data.mongodb.uri= #你的连接字符串，集群和单机模式均可，如果没有配置，那么默认就是localhost的27017端口 
mars.data.mongodb.database=myrepo #数据库的名字，

mars.data.mongodb.uri=mongodb://root:123456@server100:37017,server100:37018,server100:37019/mars?authSource=admin
mars.data.mongodb.database=test
```
创建 Spring Boot 启动类：
```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);
    }
}
```


## 编码
编写实体类 `Student.java`（此处使用了 [Lombok](https://www.projectlombok.org/) 简化代码）
```java
@Entity("student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @MongoId(value = StorageType.STRING)
    private String stuNo;

    private String classNo;

    private String stuName;

    private Integer stuAge;

    @MongoProperty(value = "height", storageType = StorageType.STRING)
    private Double stuHeight;

    @MongoProperty(value = "sex")
    private Sex stuSex;

    @MongoProperty(value = "cscore")
    private Double chineseScore;

    @MongoProperty(value = "mscore")
    private Double mathScore;

    @MongoProperty(value = "escore")
    private Double englishScore;

}

public enum Sex {

    F("男"),
    M("女");

    String sex ;

    Sex(String s) {
        sex = s;
    }

}
```


## 注解
### @MongoId

- 描述：文档主键注解

| 属性  | 类型    | 必须指定 | 默认值              | 描述         |
| ----- | ------- | -------- | ------------------- | ------------ |
| value | StorageType | 否       | StorageType._OBJECT_ID_ | 主键数据类型 |

### @MongoProperty

- 描述：文档字段注解

| 属性    | 类型    | 必须指定 | 默认值                               | 描述                   |
| ------- | ------- | -------- | ------------------------------------ | ---------------------- |
| value   | String  | 否       | ""如果不声明，会以字段名字存入数据库 | 存入数据库中字段的名字 |
| storageType | StorageType | 否       | StorageType.*IMPLICIT*                   | 字段的数据类型         |

# 核心操作

## 单个CRUD操作
```java
@Slf4j
@SpringBootTest
public class MarsCRUDTest {
    @Autowired
    private Mars mars;
    
    //插入一个对象记录到数据库
	@Test
    public void testInsertOne() {
        //这个只是我自定义的一个创建对象的静态方法
        Student student = StudentGenerator.getInstance(defStuNo);
        InsertOneResult insert = mars.insert(student);
    }
	//根据条件查找一个对象并返回
	@Test
    public void testFind() {
        String key = "1";
        Optional<Student> student = mars.findOne(Query.query(Criteria.where("_id").is(key)), Student.class);
    }
	//更新student表中_id为key的记录，修改它的stuName字段为yy
	@Test
    public void testUpdate() {
        String key = "1";
        mars.update(Query.query(Criteria.where("_id").is(key)), Update.update("stuName", "yy"),Student.class,new UpdateOptions().multi(false),"student");
    }
	//删除student集合中_id为key的记录
	@Test
    public void testDeleteOne(){
        String key = "1";
        mars.delete(Query.query(Criteria.where("_id").is(key)),Student.class);
    }

}
```
## 批量CRUD操作
```java
@Slf4j
@SpringBootTest
public class MarsCRUDTest {
    @Autowired
    private Mars mars;
    
    private static List<Student> stuList = new LinkedList<>();
    private static Integer totalCount = 1000000;

    public void init() {
        mars.getCollection(Student.class).drop();
        for (int i = 0; i < totalCount; i++) {
            Student student = StudentGenerator.getInstance(i);
            stuList.add(student);
        }
    }
    //插入多个对象记录到数据库
	@Test
    public void testInsertMany() {
        init();
        mars.insert(stuList);
    }
    
    //根据条件查找多个对象，并返回其中的前一部分数据
    @Test
    public void testFindAll(){
        Integer queryCount = 3;
        QueryCursor<Student> cursor = mars.findAll(new Query().limit(queryCount), Student.class);
    }
    
	
	//更新student表中_id为key的记录，修改它的stuName字段为yy
	@Test
    public void testUpdate() {
        String key = "1";
        mars.update(Query.query(Criteria.where("_id").is(key)), Update.update("stuName", "yy"),Student.class,new UpdateOptions().multi(false),"student");
    }
	//删除student集合中_id为key的记录
	@Test
    public void testDeleteOne(){
        String key = "1";
        mars.delete(Query.query(Criteria.where("_id").is(key)),Student.class);
    }

}
```



# 扩展功能

## 文件存储GridFs

- GridFS 用于存储和恢复那些超过16M（BSON文件限制）的文件(如：图片、音频、视频等)
- GridFS 也是文件存储的一种方式，但是它是存储在MonoDB的集合中
- GridFS 可以更好的存储大于16M的文件
- GridFS 会将大文件对象分割成多个小的chunk(文件片段),一般为256k/个,每个chunk将作为MongoDB的一个文档(document)被存储在chunks集合中
- GridFS 用两个集合来存储一个文件：fs.files与fs.chunks
- 每个文件的实际内容被存在chunks(二进制数据)中,和文件有关的meta数据(filename,content_type,还有用户自定义的属性)将会被存在files集合中
> `Mars`框架同样支持GridFs 的功能，相对于原生的Java驱动，在操作方面有了许多简化，

```java
@SpringBootTest
@Slf4j
public class MarsGridFsTest {
    @Autowired
    Mars mars;

    private static String bucketName = "xx";
    private static String m1 = "/Users/cs/Documents/back.jpeg";

    private static String m1Id = "6080dd5e878eb9205b437f7f";


    //向MongoDB中存入一个文件，插入成功会返回该文件的主键
    @Test
    public void testInsertGridFs() throws FileNotFoundException {
        File file = new File(m1);
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId objectId = mars.storeGridFs(fileInputStream, "xx", new Document("delete", true), bucketName);
    }
    
    //根据主键Id去查找文件的信息，返回的是一个GridFSFindIterable 类型的对象，这个对象包含了文件的基本信息，但是不包含文件流
    @Test
    public void testFindById() {
        String fieldId = "6080dc7d30d2012b25d79af5";
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("_id").is(new ObjectId(fieldId))), bucketName);
        System.out.println("找到的数据");
        System.out.println(iterable);
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }
    
    //根据文件的filename去进行查询，由于filename可重复，所以可能会查询出多个结果
    @Test
    public void testFindByName() {
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("filename").is("back")));
        System.out.println("找到的数据");
        iterable.forEach(item -> {
            System.out.println(item);
        });
    }
    
    //根据文件的元信息进行查询，也会返回多个结果
    @Test
    public void testFindByMetadata() {
        Document document = new Document("delete", false);
        GridFSFindIterable iterable = mars.findGridFs(Query.query(Criteria.where("metadata.delete").is(true)));
    }
    
    //更改文件的元信息，实际上是使用Mar对文档的操作进行修改的，并没有使用到GridFs的操作
    @Test
    public void updateGridFs() {
        UpdateResult result = mars.getDatabase().getCollection("fs.files").updateOne(Filters.eq("_id", new ObjectId(m1Id)), Updates.set("metadata.delete", true));
        System.out.println("更新的结果");
        System.out.println(result);
    }
    
    //将在MongoDB中存储的文件下载到指定目录
    @Test
    public void testBucketFind() throws IOException {
        GridFSFile gridFSFile = mars.findOneGridFs(Query.query(Criteria.where("_id").is(new ObjectId(m1Id))), bucketName);
        //打开下载流对象
        GridFsResource resource = mars.getResource(gridFSFile,bucketName);
        //创建gridFsSource，用于获取流对象
        //获取流中的数据
        InputStream input = resource.getInputStream();
        //目的地地址
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
    }
    
    //根据查询条件删除文件
    @Test
    public void testDelByQuery() {
        mars.deleteGridFs(Query.query(Criteria.where("_id").is(new ObjectId("606c2bd63b073106df70cc9e"))));
    }

    //根据id删除文件
    @Test
    public void testDelById() {
        mars.deleteGridFs(new ObjectId("608011db84316b43f412d1f8"), "fs");
    }

}
```
## 数据迁移ChangeStream
> changeStream的原理就是使用 `消费者-生产者` 的模式，让一个线程去读取MongoDB中 opLog 的变更，然后将消息分发给其他的消费者



### 直接使用

- 首先需要定义收到消息之后需要进行的处理，实现接口，并重写接口中的 onMessage 方法

  
```java
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.mars.core.messaging.Message;
import org.mars.core.messaging.MessageListener;

@Slf4j
public class DocumentMessageListener implements MessageListener<ChangeStreamDocument<Document>, Document> {
    @Override
    public void onMessage(Message<ChangeStreamDocument<Document>, Document> message) {
        log.info("Received Message in collection: {},message raw: {}, message body:{}",
                message.getProperties().getCollectionName(), message.getRaw(), message.getBody());
    }
}

```

- 接下来可以通过创建对象的方式进行调用

  
```java
import com.cs.mars.config.DocumentMessageListener;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mars.core.Mars;
import org.mars.core.messaging.ChangeStreamRequest;
import org.mars.core.messaging.DefaultMessageListenerContainer;
import org.mars.core.messaging.MessageListenerContainer;

public class SimpleListenerTest {

    //监听指定连接数据库中的test表格中的所有操作，如果数据有变更，就会执行上面定义的onMessage的操作
    @Test
    public void testListener() throws InterruptedException {
        Mars mars = new Mars("连接uri");
        DocumentMessageListener listener = new DocumentMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars);
        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                .collection("test")//需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件 ，本功能暂时不能支持
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request,Document.class);
        container.start();
        Thread.currentThread().join();
    }
    	
    //如果需要监听数据库中的多个集合，那么需要往MessageListener多注册一套规则，这个规则还是会包括，对消息的处理，监听的集合
    @Test
    public void tesMultiListener() throws InterruptedException {
        Mars mars = new Mars("连接uri");
        DocumentMessageListener listener = new DocumentMessageListener();
        MessageListenerContainer container = new DefaultMessageListenerContainer(mars);
        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(listener)
                .collection("test")//需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件 ，本功能暂时不能支持
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request,Document.class);
        
        ChangeStreamRequest<Document> request1 = ChangeStreamRequest.builder(listener)
                .collection("testtt")//需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件 ，本功能暂时不能支持
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        container.register(request1,Document.class);
        container.start();
        Thread.currentThread().join();
    }
}
```

- 如果你只是想要简单的使用，并且对性能方面也没有太高的要求的话，那么建议你使用 `Mars` 支持的原生changeStream操作

  
```java
    @Test
    public void testWatch() {
        Mars mars = new Mars("连接uri");
        MongoCollection<Document> collection = mars.getDatabase().getCollection("test");
        System.out.println(collection);
        //对本collection进行监听，并且过滤操作的规则，只监听insert类型的操作
        ChangeStreamIterable<Document> documents = collection.watch(asList(Aggregates.match(Filters.in("operationType", asList("insert")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP);
        documents.forEach(doc -> {
            System.out.println(doc);
        });
    }
```
### 在Spring容器内部使用



- 首先还是需要定义接收到消息之后的处理策略

  
```java
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.mars.core.messaging.Message;
import org.mars.core.messaging.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentMessageListener implements MessageListener<ChangeStreamDocument<Document>, Document> {
    @Override
    public void onMessage(Message<ChangeStreamDocument<Document>, Document> message) {
        log.info("Received Message in collection: {},message raw: {}, message body:{}",
                message.getProperties().getCollectionName(), message.getRaw(), message.getBody());
    }
}
```

- 接下来是使用配置类来配置MessageListenerContainer，将定义的策略和需要监听的集合配置进去

  
```java
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.mars.core.Mars;
import org.mars.core.messaging.ChangeStreamRequest;
import org.mars.core.messaging.DefaultMessageListenerContainer;
import org.mars.core.messaging.MessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class MongoConfig {
    //这里是声明将MessageListenerContainer放入到Spring容器中可以进行使用，
    @Bean
    MessageListenerContainer messageListenerContainer(Mars mars, DocumentMessageListener documnetMessageListener) {
        Executor executor = Executors.newSingleThreadExecutor();
        MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mars, executor) {
            //在Spring项目启动到使用，就会自动启动
            @Override
            public boolean isAutoStartup() {
                return true;
            }
        };
        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(documnetMessageListener)
                .collection("dept")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件 功能暂不支持
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();
        messageListenerContainer.register(request, Document.class);

        return messageListenerContainer;
    }
}

```

- 如果想要监听多个集合，只需要在写配置类的同时，往里面多注入一个即可

  
```java
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.mars.core.Mars;
import org.mars.core.messaging.ChangeStreamRequest;
import org.mars.core.messaging.DefaultMessageListenerContainer;
import org.mars.core.messaging.MessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class MongoConfig {
    @Bean
    MessageListenerContainer messageListenerContainer(Mars mars, DocumnetMessageListener documnetMessageListener) {
        Executor executor = Executors.newSingleThreadExecutor();
        MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mars, executor) {
            @Override
            public boolean isAutoStartup() {
                return true;
            }
        };

        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(documnetMessageListener)
                .collection("test")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();

        messageListenerContainer.register(request, Document.class);


        return messageListenerContainer;
    }

    @Bean
    MessageListenerContainer messageListenerContainer2(Mars mars, DocumnetMessageListener documnetMessageListener) {
        Executor executor = Executors.newSingleThreadExecutor();
        MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mars, executor) {
            @Override
            public boolean isAutoStartup() {
                return true;
            }
        };

        ChangeStreamRequest<Document> request = ChangeStreamRequest.builder(documnetMessageListener)
                .collection("person")  //需要监听的集合名，不指定默认监听数据库的
                .filter()  //过滤需要监听的操作类型，可以根据需求指定过滤条件
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)  //不设置时，文档更新时，只会发送变更字段的信息，设置UPDATE_LOOKUP会返回文档的全部信息
                .build();

        messageListenerContainer.register(request, Document.class);
        return messageListenerContainer;
    }

}
```


