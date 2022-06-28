package com.whaleal.mars.core.index;

import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lyz
 * @description
 * @date 2022-06-24 14:05
 **/
@SpringBootTest
public class AutoCreateIndexTest {

//    private Mars mars = new Mars(Constant.connectionStr);
    @Autowired
    private Mars mars;

    @Test
    public void testFor(){
        mars.createCollection(Animal.class);
    }
}
