package com.whaleal.mars.base;

import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.CollectionOptions;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;

public class EntityTest {
    Mars mars;

    @Before
    public void init() {
        mars = new Mars(Constant.server100_test);
    }

    @Test
    public void testDropCollection() {
        mars.dropCollection(Student.class);
    }

    @Test
    public void testCreateCollection() {
        CollectionOptions options = CollectionOptions.empty().capped().size(2000L).maxDocuments(2000L);
        //mars.createCollection("cs", options);
        mars.createCollection(Student.class);
    }

}
