package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/13 0013 14:54
 * FileName: UserCommandTest
 * Description:
 */
public class UserCommandTest {

    //连接
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * 创建用户
     * {
     *   createUser: "<name>",
     *   pwd: passwordPrompt(),      // Or  "<cleartext password>"
     *   customData: { <any information> },
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   writeConcern: { <write concern> },
     *   authenticationRestrictions: [
     *      { clientSource: [ "<IP|CIDR range>", ... ], serverAddress: [ "<IP|CIDR range>", ... ] },
     *      ...
     *   ],
     *   mechanisms: [ "<scram-mechanism>", ... ],  //Available starting in MongoDB 4.0
     *   digestPassword: <boolean>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForCreateUser(){
        System.out.println("=========开始创建用户==========");
        //测试创建用户
        Document document = mars.executeCommand(
                "{\n" +
                        "createUser: \"test\",\n" +
                        "pwd: \"test\",\n" +
                        "customData: { employeeId: 12345 },\n" +
                        "roles: [\n" +
                        "                { role: \"read\", db: \"mars\" },\n" +
                        "                { role: \"read\", db: \"mars\" },\n" +
                        "                \"readWrite\"\n" +
                        "              ],\n" +
                        "writeConcern: { w: \"majority\" , wtimeout: 5000 },\n" +
                        "}"
        );
        System.out.println("=========创建结束==========");
    }

    /**
     * 删除用户
     * {
     *   dropUser: "<user>",
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropUser(){
        System.out.println("==============开始删除用户===============");
        Document document2 = mars.executeCommand(
                "{\n" +
                        "   dropUser: \"test\",\n" +
                        "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                        "} "
        );
        System.out.println("=============删除结束==============");
    }

    /**
     * 删除全部用户
     * { dropAllUsersFromDatabase: 1,
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForDropAllUsersFromDatabase(){
        //删除全部用户
        System.out.println("===========开始删除全部用户============");
        Document document = mars.executeCommand(
                " { dropAllUsersFromDatabase: 1, writeConcern: { w: \"majority\" } }"
        );
        Document result = Document.parse("{ \"n\" : 2, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
        System.out.println("===========删除结束============");
    }

    /**
     * 查看用户
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
    public void testForViewSpecificUser(){
        //查看特定用户
        Document document = mars.executeCommand("{\n" +
                "     usersInfo:  { user: \"accountAdmin01\", db: \"mars\" },\n" +
                "     showPrivileges: true\n" +
                "   }");
        System.out.println(document.toJson());
    }

    @Test
    public void testForViewMultipleUsers(){
        //查看多个用户
        Document document = mars.executeCommand("{\n" +
                "   usersInfo: [ { user: \"root\", db: \"admin\" }, { user: \"accountAdmin01\", db: \"mars\" } ],\n" +
                "   showPrivileges: true\n" +
                "} ");
        System.out.println(document.toJson());
    }

    @Test
    public void testForAllUsers(){
        //查看一个库的全部用户,不能跨数据库
        Document document = mars.executeCommand("{\n" +
                "usersInfo: 1\n" + "}");
        System.out.println(document.toJson());
    }

    @Test
    public void testForAllUsersWithSpecifiedFilter(){
        //查看有特殊角色权限的用户，也是在一个数据库里面的用户
        Document document = mars.executeCommand(
                "{ usersInfo: 1, filter: { roles: { role: \"read\", db: \"mars\" } } }");
        System.out.println(document.toJson());
    }

    @Test
    public void testForAllUsersWithCredentials(){
        //根据SCRAM-SHA-1 Credentials查看用户,包含即可
        Document document = mars.executeCommand(
                "{ usersInfo: { forAllDBs: true}, filter: { mechanisms: \"SCRAM-SHA-1\" } }");
        System.out.println(document.toJson());
    }

    /**
     * 授权
     * { grantRolesToUser: "<user>",
     *   roles: [ <roles> ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForGrantRolesToUser(){
        //授予权限
        System.out.println("==============开始授权==============");
        Document document = mars.executeCommand(
            "{ grantRolesToUser: \"test\",\n" +
                    "                 roles: [\n" +
                    "                    { role: \"read\", db: \"stock\"},\n" +
                    "                    \"readWrite\"\n" +
                    "                 ],\n" +
                    "                 writeConcern: { w: \"majority\" , wtimeout: 2000 }\n" +
                    "             }"
        );
        System.out.println("==============授权结束==============");
    }

    /**
     * 收回权限
     * { revokeRolesFromUser: "<user>",
     *   roles: [
     *     { role: "<role>", db: "<database>" } | "<role>",
     *     ...
     *   ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForRevokeRolesFromUser(){
        //收回权限
        System.out.println("==============开始收回权限==============");
        Document document = mars.executeCommand(
                "{ revokeRolesFromUser: \"test\",\n" +
                        "                 roles: [\n" +
                        "                          { role: \"read\", db: \"stock\" },\n" +
                        "                          \"readWrite\"\n" +
                        "                 ],\n" +
                        "                 writeConcern: { w: \"majority\" }\n" +
                        "             }"
        );
        System.out.println("==============收回结束==============");
    }
}
