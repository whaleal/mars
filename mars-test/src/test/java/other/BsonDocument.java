package other;

import com.mongodb.MongoClientSettings;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Test;

public class BsonDocument {


    @Test
    public void testBsonDocuemnt2Document() {
        Document document = new Document();

        document.put("a", "1");

        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();


        org.bson.BsonDocument bsonDocument = document.toBsonDocument(BsonDocument.class, defaultCodecRegistry);

    }
}
