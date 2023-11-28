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
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.mongodb.ClientSessionOptions.builder;
import static com.mongodb.WriteConcern.MAJORITY;
import static org.testng.Assert.*;


/**
 * @author wh
 */



public class TestTransactions {

    private Mars mars;

    @Before
    public void init() {

        mars = new Mars(Constant.connectionStr);

        try {
            mars.deleteMulti(new Query(),Student.class);
        }catch (Exception e){

        }





    }


    @Test
    public void delete() {

      /*  Student student = StudentGenerator.getInstance(1001);

        *//**
         * 测试内容
         * 插入
         * 删除
         * 无异常
         * 结果无空
         *//*

        mars.withTransaction(( session ) -> {
            session.insert(student);

            assertNotNull(session.find(new Query(), Student.class).tryNext());
            session.delete(new Query(), Student.class);

            assertNull(session.find(new Query(), Student.class).tryNext());

            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());


        assertNull(mars.find(new Query(), Student.class).tryNext());

        //  redo  delete  with  Exception  this will not work for delete.
        //  插入非事务 。删除为事务操作, 插入已经执行完毕,但是删除无法执行。 因此 文档还在


        *//**
         * 插入
         * 删除
         * 有异常
         * 正常回滚
         * 结果为空
         *
         *//*
        System.out.println("delete -- Part2");



        mars.withTransaction(( session2 ) -> {

            session2.insert(StudentGenerator.getInstance(1001));

            session2.delete(new Query(), Student.class);

            if(true){
                throw  new IllegalArgumentException("do  with Exception");
            }

            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());


        assertNull(mars.find(new Query(), Student.class).tryNext());

*/

        System.out.println("delete -- Part3 ");

        /**
         *
         * 插入 在事务体外
         *
         * 事务体内执行  删除-插入-插入 过程中有异常
         *
         * 结果应当为有数据
         *
         */


        Student instance = StudentGenerator.getInstance(1001);

        Student instance2 = StudentGenerator.getInstance(1002);

        mars.insert(instance);

        mars.withTransaction(( session3 ) -> {

            session3.delete(new Query(), Student.class);
            /**
             *
             * 重复插入会有异常
             * 从而导致 事务内的删除也失败
             *
             */

            session3.insert(instance2);
            session3.insert(instance2);
            //  按道理讲这块应该不会执行
            session3.deleteMulti(new Query(),Student.class);




            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());


        assertNotNull(mars.find(new Query(), Student.class).tryNext());

        System.out.println("delete -- Part4 ");


    }

    @Test
    public void insert() {


        /**
         *   插入重复数据
         *   异常自动退出事务
         *
         */
        Student stu = StudentGenerator.getInstance(1001);
        mars.withTransaction(( session ) -> {
            session.insert(stu);
            session.insert(stu);
            return new Object();
        });

        assertNull(mars.find(new Query(), Student.class).tryNext());
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




}