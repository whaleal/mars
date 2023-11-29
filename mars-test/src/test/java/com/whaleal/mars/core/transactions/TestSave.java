package com.whaleal.mars.core.transactions;

import com.mongodb.client.MongoDatabase;
import com.sun.tools.javac.util.List;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.MarsSessionImpl;
import com.whaleal.mars.util.StudentGenerator;

/**
 * @author wh
 */
public class TestSave {

    public static void main( String[] args ) {
        Mars  mars =new Mars(Constant.connectionStr);

        MarsSessionImpl marsSession = mars.startSession();

        Student instance = StudentGenerator.getInstance(1004);
        Student  instance2 = StudentGenerator.getInstance(1005);
        List< Student > of = List.of(instance, instance2);
        marsSession.save(of);


        mars.getDatabase().drop();
    }
}
