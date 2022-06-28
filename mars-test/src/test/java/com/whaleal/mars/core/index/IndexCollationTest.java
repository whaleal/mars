package com.whaleal.mars.core.index;

import com.mongodb.client.model.Collation;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.IndexOptions;
import org.junit.Test;

/**
 * @author lyz
 * @description
 * @date 2022-06-27 16:09
 **/
public class IndexCollationTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void dropCollection(){
        mars.dropCollection("weather");
    }

    @Test
    public void testFor(){
        mars.createCollection(Weather.class);

        mars.ensureIndexes(Weather.class);
    }

    @Test
    public void testForCreateIndex(){
        mars.createIndex(new Index("temp",IndexDirection.ASC,new IndexOptions().collation(Collation.builder().locale("zh").build())),"weather");
    }

    @Test
    public void dropCollectionIndex(){
//        mars.dropInd;
    }
}
