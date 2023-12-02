package com.whaleal.mars.core.transactions;

import com.mongodb.TransactionOptions;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.MarsSession;
import com.whaleal.mars.session.MarsSessionImpl;
import com.whaleal.mars.util.StudentGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.mongodb.ClientSessionOptions.builder;
import static com.mongodb.WriteConcern.MAJORITY;
import static org.junit.Assert.*;


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

       Student student = StudentGenerator.getInstance(1001);

         /**
         * 测试内容
         * 插入
         * 删除
         * 无异常
         * 结果无空
         **/

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


        /**
         * 插入
         * 删除
         * 有异常
         * 正常回滚
         * 结果为空
         *
         */
        System.out.println("delete -- Part2");



        mars.withTransaction(( session2 ) -> {
            session2.startTransaction();

            session2.insert(StudentGenerator.getInstance(1001));



            if(true){
                session2.delete(new Query(), Student.class);
                session2.insert(StudentGenerator.getInstance(1001));
                session2.abortTransaction();
                throw  new IllegalArgumentException("do  with Exception");
            }

            try {
                session2.commitTransaction();
            }catch (Exception e){

            }

            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());






        assertNull(mars.find(new Query(), Student.class).tryNext());


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

            session3.startTransaction();

            session3.delete(new Query(), Student.class);
            /**
             *
             * 重复插入会有异常
             * 从而导致 事务内的删除也失败
             *
             */

            try {
                session3.insert(instance2);
                session3.insert(instance2);
            }catch (Exception e){
                session3.abortTransaction();
            }

            try {
                //  按道理讲这块应该不会执行
                session3.deleteMulti(new Query(),Student.class);
                session3.commitTransaction();
            }catch (Exception e){

            }






            return null;
        }, builder()
                .defaultTransactionOptions(TransactionOptions.builder()
                        .writeConcern(MAJORITY)
                        .build())
                .build());


        assertNotNull(mars.find(new Query(), Student.class).tryNext());

        System.out.println("delete -- Part4 ");


    }


    /**
     *   插入重复数据
     *   异常自动退出事务并回滚
     *   查询时无数据
     *
     */
    @Test
    public void insert() {

        Student stu = StudentGenerator.getInstance(1001);
        mars.withTransaction(( session ) -> {
            session.insert(stu);
            session.insert(stu);
            return new Object();
        });

        assertNull(mars.find(new Query(), Student.class).tryNext());
    }


    /**
     * 插入多条文档
     * 查询结果不为空
     *
     */
    @Test
    public void insertList() {
        List< Student > stus = CollUtil.asList(StudentGenerator.getInstance(1001),
                StudentGenerator.getInstance(1002));

        mars.withTransaction(( session ) -> {
            session.insert(stus, Student.class);
            assertEquals(session.find(new Query(), Student.class).toList(), stus);
            return null;
        });

        assertEquals(mars.estimatedCount(Student.class), 2);
    }



    /**
     * 一次性插入多条文档
     * 但是该文档内部重复
     * 触发事务逻辑
     * 查询结果为空
     *
     */
    @Test
    public void insertList2() {
        /**
         * 两条重复的文档
         */
        List< Student > stus = CollUtil.asList(StudentGenerator.getInstance(1001),
                StudentGenerator.getInstance(1001));

        mars.withTransaction(( session ) -> {
            session.insert(stus, Student.class);

            session.commitTransaction();
            return null;
        });

        assertEquals(mars.estimatedCount(Student.class), 0);
    }



    /**
     * 这里调整为 多表 事务
     */
    @Test
    public void manual() {
        try (MarsSession session = mars.startSession()) {


            session.startTransaction();
            session.save(StudentGenerator.getInstance(1001));

            session.save(StudentGenerator.getInstance(1002));

            try {
                // 主动的回滚 需要主动的  startTransaction
                session.abortTransaction();
            }catch (Exception e){

            }

            //session.commitTransaction();
        }


        assertEquals(mars.find(new Query(), Student.class).toList().size(),0);
    }

    @Test
    public void merge() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.save(stu);
        assertEquals(mars.estimatedCount(Student.class), 1);

        mars.withTransaction(( session ) -> {

            assertEquals(session.find(new Query(), Student.class).tryNext(), stu);

            assertEquals(session.find(new Query(), Student.class).tryNext().getClassNo(), stu.getClassNo());

            return null;
        });

        assertEquals(mars.find(new Query(), Student.class).tryNext().getClassNo(), stu.getClassNo());
    }


    /**
     *
     *
     */
    @Test
    public void modify() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.withTransaction(( session ) -> {

            session.save(stu);

            Update update = new Update().inc("stuAge", 13);
            session.updateFirst(new Query(), update, Student.class);

            return null;
        });

        Student student = mars.find(new Query(), Student.class).tryNext();
        assertEquals(stu.getClassNo(), student.getClassNo());
        assertEquals(stu.getStuAge() + 13, student.getStuAge(), 18);

        assertEquals(mars.find(new Query(), Student.class).tryNext().getStuAge(), stu.getStuAge() + 13, 18);
    }

    @Test
    public void remove() {
        Student stu = StudentGenerator.getInstance(1001);
        mars.save(stu);

        mars.withTransaction(( session ) -> {

            assertNotNull(session.find(new Query(), Student.class).tryNext());

            session.find(new Query(), Student.class);

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


            assertNotNull(session.find(new Query(), Student.class).tryNext());

            stu.setStuAge(42);
            session.save(stu);


            assertEquals(session.find(new Query(), Student.class).tryNext().getStuAge(), 42, 0.5);

            return null;
        });

        assertNotNull(mars.find(new Query(), Student.class).tryNext());
    }

    @Test
    public void saveList() {

        MarsSessionImpl marsSession = mars.startSession();


        marsSession.withTransaction(( session ) -> {

            List< Student > stus = CollUtil.asList(StudentGenerator.getInstance(1001),
                    StudentGenerator.getInstance(1002));

            session.save(stus);


            return null;
        });

        assertEquals(mars.estimatedCount(Student.class), 2);
    }


    /**
     * 测试 事务体的隔离 情况
     *
     * 事务体内的操作 应当对其他操作不可见
     *
     */
    @Test
    public void update() {
        Student stu = StudentGenerator.getInstance(1001);

        mars.withTransaction(( session ) -> {

            // 基于事务的操作
            session.save(stu);

            // 在事务体内 此时应该是查不到数据
            Assert.assertNull(new Mars(Constant.connectionStr).find(new Query(), Student.class).tryNext());


            Student stu2 = StudentGenerator.getInstance(1002);



            //  新的 会话插入
            new Mars(Constant.connectionStr).insert(stu2);


            assertEquals(session.find(new Query(), Student.class).toList().size() , 1);

            assertNotNull(mars.find(new Query(), Student.class).tryNext());
            return null;
        });


    }


    public static void main( String[] args ) {


    }


}