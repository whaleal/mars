package com.whaleal.mars.core.crud;

import com.whaleal.mars.bean.EasyPrimitive;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;

import java.util.Optional;

public class TestPrimitive {

    @Test
    public void test(){

        EasyPrimitive easyPrimitive = new EasyPrimitive();
        Mars mars = new Mars(Constant.server100);
        mars.insert(easyPrimitive);

        Optional<EasyPrimitive> one = mars.findOne(new Query(), EasyPrimitive.class);

        System.out.println(one.get());

    }
}
