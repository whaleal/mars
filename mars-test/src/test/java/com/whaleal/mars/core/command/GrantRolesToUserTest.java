package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:23
 * FileName: GrantRolesToUserTest
 * Description:
 */
public class GrantRolesToUserTest {
    private Mars mars = new Mars(Constant.connectionStr);


    /**
     * { grantRolesToUser: "<user>",
     *   roles: [ <roles> ],
     *   writeConcern: { <write concern> },
     *   comment: <any>
     * }
     */
    @Test
    public void testForGrantRolesToUser(){
        Document document = mars.executeCommand("{ grantRolesToUser: \"testUser\",\n" +
                "                 roles: [\n" +
                "                    { role: \"readWriteAnyDatabase\", db: \"admin\"},\n" +
                "                    \"readWrite\"\n" +
                "                 ],\n" +
                "                 writeConcern: { w: \"majority\" , wtimeout: 2000 }\n" +
                "             }");
        System.out.println(document);
    }
}
