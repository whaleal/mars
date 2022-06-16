package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/14 0014 10:59
 * FileName: RoleInfoTest
 * Description:
 */
public class RoleInfoTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * 查看角色信息
     * {
     *   rolesInfo: { role: <name>, db: <db> },
     *   showPrivileges: <Boolean>,
     *   showBuiltinRoles: <Boolean>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForRoleInfo(){
        //查询admin库中单个角色信息,包含角色继承关系
        System.out.println("============查询单个指定角色============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"service\", db: \"mars\" }\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }

    @Test
    public void testForRoleInfoWithPrivileges(){
        //查询角色信息包含角色继承关系和权限
        System.out.println("============查询单个指定角色包括权限============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"service\", db: \"mars\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }

    @Test
    public void testForSeveralRoles(){
        //查询两个不同库的角色信息
        System.out.println("============查询不同库多个角色============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: [\n" +
                        "         { role: \"serivce\", db: \"mars\" },\n" +
                        "         { role: \"root\", db: \"admin\" }\n" +
                        "      ]\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }

    @Test
    public void testForSeveralRolesWithPrivileges(){
        //查询两个不同库的角色信息包含权限信息
        System.out.println("============查询不同库多个角色包括权限============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: [\n" +
                        "         { role: \"root\", db: \"admin\" },\n" +
                        "         { role: \"service\", db: \"mars\" }\n" +
                        "      ],\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }

    @Test
    public void testForAllRolesWithPrivileges(){
        //查询一个库的所有角色
        System.out.println("============查询单个库所有角色包括权限============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }

    @Test
    public void testForAllRolesWithBuiltinRoles(){
        //查询一个库的所有角色，包含内嵌角色和权限
        System.out.println("============查询单个库所有角色包括内嵌角色============");
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showBuiltinRoles: true\n" +
                        "    }"
        );
        System.out.println("================查询角色结束==================");
        System.out.println(document);
    }
}
