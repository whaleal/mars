package other;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wh
 * @Date 2021-01-15
 */
public class BsonDocumentAndDocumentPerfermanceTest {

    MongoClient client;
    MongoDatabase pocdb;


    @Before
    public void init() {
        client = MongoClients.create("mongodb://admin:root123@192.168.3.35:37019,192.168.3.35:37018,192.168.3.35:37017/admin");
        pocdb = client.getDatabase("test");
    }


    /**
     * 用于测试直接获取Document的一个性能
     */
    @Test
    public void testDocument() {
        MongoCursor<Document> iterator = pocdb.getCollection("test2").find().iterator();

        while (iterator.hasNext()) {
            //System.out.println(iterator.next());
            iterator.next();
        }

    }

    /**
     * 用于测试直接获取BsonDocument的一个性能
     */
    @Test
    public void testBsonDocument() {
        MongoCursor<BsonDocument> iterator = pocdb.getCollection("test2", BsonDocument.class).find().iterator();

        while (iterator.hasNext()) {
            //System.out.println(iterator.next());
            iterator.next();
        }


    }
}
