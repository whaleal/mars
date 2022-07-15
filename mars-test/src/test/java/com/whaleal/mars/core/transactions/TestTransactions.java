package com.whaleal.mars.core.transactions;

import com.mongodb.TransactionOptions;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.util.StudentGenerator;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.MarsSession;
import org.junit.After;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.mongodb.ClientSessionOptions.builder;
import static com.mongodb.WriteConcern.MAJORITY;
import static org.testng.Assert.*;


/**
 * @author wh
 */


public class TestTransactions {

    private Mars mars;

    @BeforeMethod
    public void init() {

        mars = new Mars(Constant.connectionStr);

        mars.dropCollection(Student.class);

    }


    @Test
    public void delete() {

        Student student = StudentGenerator.getInstance(1001);
        mars.insert(student);

        mars.withTransaction(( session ) -> {


            assertNotNull(session.findAll(new Query(), Student.class).tryNext());

            session.delete(new Query(), Student.class);


            assertNull(session.findAll(new Query(), Student.class).tryNext());
            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());

        assertNull(mars.findAll(new Query(), Student.class).tryNext());
    }

    @Test
    public void insert() {
        Student stu = StudentGenerator.getInstance(1001);
        MarsSession marsSession = mars.startSession();

        mars.withTransaction(( session ) -> {
            session.insert(stu);

            assertNotNull(marsSession.findAll(new Query(), Student.class).tryNext());
            assertEquals(session.estimatedCount(Student.class), 1);

            return null;
        });

        assertNotNull(mars.findAll(new Query(), Student.class).tryNext());
    }

    @Test
    public void insertList() {
        List< Student > stus = CollUtil.asList(StudentGenerator.getInstance(1001),
                StudentGenerator.getInstance(1002));

        mars.withTransaction(( session ) -> {
            session.insert(stus, Student.class);


            assertEquals(session.findAll(new Query(), Student.class).toList(), stus);

            return null;
        });

        assertEquals(mars.estimatedCount(Student.class), 2);
    }


    /**
     * 这里调整为 多表 事务
     */
    @Test
    public void manual() {
        try (MarsSession session = mars.startSession()) {
            session.startTransaction();

            Student stu = StudentGenerator.getInstance(1001);
            session.save(stu);

            session.save(StudentGenerator.getInstance(1002));


            assertNotNull(session.findAll(new Query(), Student.class).tryNext());

            session.commitTransaction();
        }

        assertNotNull(mars.findAll(new Query(), Student.class).tryNext());
    }

    @Test
    public void merge() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.save(stu);
        assertEquals(mars.estimatedCount(Student.class), 1);

        mars.withTransaction(( session ) -> {


            assertEquals(session.findAll(new Query(), Student.class).tryNext(), stu);


            assertEquals(session.findAll(new Query(), Student.class).tryNext().getClassNo(), stu.getClassNo());

            return null;
        });

        assertEquals(mars.findAll(new Query(), Student.class).tryNext().getClassNo(), stu.getClassNo());
    }

    @Test
    public void modify() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.withTransaction(( session ) -> {
            session.save(stu);


            Update update = new Update().inc("stuAge", 13);
            session.update(new Query(), update, Student.class);

            Student student = mars.findAll(new Query(), Student.class).tryNext();


            assertEquals(stu.getClassNo(), student.getClassNo());
            assertEquals(stu.getStuAge() + 13, student.getStuAge(), 18);

            return null;
        });

        assertEquals(mars.findAll(new Query(), Student.class).tryNext().getStuAge(), stu.getStuAge() + 13, 18);
    }

    @Test
    public void remove() {
        Student stu = StudentGenerator.getInstance(1001);
        mars.save(stu);

        mars.withTransaction(( session ) -> {

            assertNotNull(session.findAll(new Query(), Student.class).tryNext());

            session.findAll(new Query(), Student.class);

            session.delete(new Query(), Student.class);


            assertNull(session.findAll(new Query(), Student.class).tryNext());
            return null;
        });

        assertNull(mars.findAll(new Query(), Student.class).tryNext());
    }

    @Test
    public void save() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.withTransaction(( session ) -> {
            session.save(stu);


            assertNotNull(session.findAll(new Query(), Student.class).tryNext());

            stu.setStuAge(42);
            session.save(stu);


            assertEquals(session.findAll(new Query(), Student.class).tryNext().getStuAge(), 42, 0.5);

            return null;
        });

        assertNotNull(mars.findAll(new Query(), Student.class).tryNext());
    }

    @Test
    public void saveList() {
        List< Student > stus = CollUtil.asList(StudentGenerator.getInstance(1001),
                StudentGenerator.getInstance(1002));

        mars.withTransaction(( session ) -> {
            session.save(stus);


            assertEquals(session.estimatedCount(Student.class), 2);

            return null;
        });

        assertEquals(mars.estimatedCount(Student.class), 2);
    }

    @Test
    public void update() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.withTransaction(( session ) -> {
            session.save(stu);

            assertNotNull(new Mars(Constant.connectionStr).findAll(new Query(), Student.class).tryNext());

            Update update = new Update().inc("stuAge", 13);

            mars.update(new Query(), update, Student.class);

            assertEquals(session.findAll(new Query(), Student.class).tryNext().getStuAge(), stu.getStuAge() + 13, 0.5);

            assertNotNull(mars.findAll(new Query(), Student.class).tryNext());
            return null;
        });

        assertEquals(mars.findAll(new Query(), Student.class).tryNext().getStuAge(), stu.getStuAge() + 13, 0.5);
    }


    @After
    public void destory() {
        mars.dropCollection(Student.class);
    }


}