package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EmbedCommon;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author wh
 */
public class EmbedCommonCrudTest {

    Mars mars;

    @BeforeMethod
    public void init() {
        mars = new Mars(Constant.connectionStr);
    }

    @Test
    public void test01() {
        EmbedCommon ec = new EmbedCommon();
        ec.set_id(new ObjectId());
        ec.setAge(19);
        ec.setName("ecname");
        ec.setOther(new EmbedCommon.Other("ll", "rr"));

        InsertOneResult insert = mars.insert(ec);

    }
}
