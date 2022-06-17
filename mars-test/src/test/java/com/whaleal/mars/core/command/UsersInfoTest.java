package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:32
 * FileName: UsersInfoTest
 * Description:
 */
public class UsersInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);
    /**
     * {
     *   usersInfo: <various>,
     *   showCredentials: <Boolean>,
     *   showPrivileges: <Boolean>,
     *   showAuthenticationRestrictions: <Boolean>,
     *   filter: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForSpecificUsersInfo(){
        Document document = mars.executeCommand(" {\n" +
                "     usersInfo:  { user: \"testUser\", db: \"mars\" },\n" +
                "     showPrivileges: true\n" +
                "   }");
        System.out.println(document);
    }

    @Test
    public void testForMultipleUsersInfo(){
        Document document = mars.executeCommand(" {\n" +
                "        usersInfo: [ { user: \"testUser\", db: \"mars\" }, { user: \"root\", db: \"admin\" } ],\n" +
                "        showPrivileges: true\n" +
                "    }");
        System.out.println(document);
    }

    @Test
    public void testForAllUsersInfo(){
        Document document = mars.executeCommand(" { usersInfo: 1 }");
        System.out.println(document);
    }

    @Test
    public void testForSpecificFiltersUsersInfo(){
        Document document = mars.executeCommand(" { usersInfo: 1, filter: { roles: { role: \"root\", db: \"admin\" } } }");
        System.out.println(document);
    }

    @Test
    public void testForUsersInfoWithMechanisms(){
        Document document = mars.executeCommand("{ usersInfo: { forAllDBs: true}, filter: { mechanisms: \"SCRAM-SHA-1\" } } ");
        System.out.println(document);
    }
}
