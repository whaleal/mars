package com.whaleal.mars.core.transactions;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wh
 */
public class TestTransactionApply {

    @Test
    public void test01() {
        Student student = new Mars(Constant.connectionStr).findAll(new Query(), Student.class).tryNext();

        Assert.assertNull(student);
    }
}
