package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:26
 * FileName: RevokeRolesFromUserTest
 * Description:
 */
public class RevokeRolesFromUserTest {

    private Mars mars = new Mars(Constant.connectionStr);


    /**
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
        Document document = mars.executeCommand("{ revokeRolesFromUser: \"testUser\",\n" +
                "                 roles: [\n" +
                "                          { role: \"readAnyDatabase\", db: \"admin\" },\n" +
                "                          \"readWrite\"\n" +
                "                 ],\n" +
                "                 writeConcern: { w: \"majority\" }\n" +
                "             }");
        System.out.println(document);
    }
}
