package com.whaleal.mars.core.codecs;

import com.whaleal.mars.bean.Depart;
import com.whaleal.mars.core.Mars;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

// @RunWith(SpringRunner.class)
// @SpringBootTest
public class TypeConverterTest {
    //@Autowired
    Mars mars;

    @Before
    public void initMars() {
        mars = new Mars(Constant.cs);
    }

    @Test
    public void insertTest() {
        Depart depart = new Depart();
        depart.setAtomicIntegerCodecAge(new AtomicInteger(12));
        depart.setDecimal128Age(new BigDecimal(12.1));
        depart.setDoubleAge(12.0d);
        depart.setFloatAge(12.0f);
        depart.setIntegerAge(12);
        depart.setLongAge(12l);
        depart.setShortAge(new Short("12"));
        depart.setStringAge("12");
        mars.insert(depart);
    }

    @After
    public void close() {
        mars = null;
    }
}
