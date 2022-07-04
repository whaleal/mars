package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
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
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"read\", db: \"mars\" }\n" +
                        "    }"
        );
        Document result = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForRoleInfoWithPrivileges(){
        //查询角色信息包含角色继承关系和权限
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: { role: \"read\", db: \"mars\" },\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        Document result = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"privileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSeveralRoles(){
        //查询两个不同库的角色信息
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: [\n" +
                        "         { role: \"read\", db: \"admin\" },\n" +
                        "         { role: \"read\", db: \"mars\" }\n" +
                        "      ]\n" +
                        "    }"
        );
        Document result = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSeveralRolesWithPrivileges(){
        //查询两个不同库的角色信息包含权限信息
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: [\n" +
                        "         { role: \"read\", db: \"admin\" },\n" +
                        "         { role: \"read\", db: \"mars\" }\n" +
                        "      ],\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        Document result = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"privileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"admin\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"privileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"inheritedRoles\" : [ ],\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"isBuiltin\" : true\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForAllRolesWithPrivileges(){
        //查询一个库的所有角色
        Document document = mars.executeCommand(
                "{\n" +
                        "      rolesInfo: 1,\n" +
                        "      showPrivileges: true\n" +
                        "    }"
        );
        Document result = Document.parse("{ \"roles\" : [ ], \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForAllRolesWithBuiltinRoles(){
        //查询一个库的所有角色，包含内嵌角色和权限
        Document document = mars.executeCommand("{rolesInfo: 1,showBuiltinRoles: true}");
        Document result = Document.parse("{\n" +
                "\t\"roles\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"enableSharding\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"dbOwner\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"read\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"userAdmin\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"dbAdmin\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\"isBuiltin\" : true,\n" +
                "\t\t\t\"roles\" : [ ],\n" +
                "\t\t\t\"inheritedRoles\" : [ ]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
