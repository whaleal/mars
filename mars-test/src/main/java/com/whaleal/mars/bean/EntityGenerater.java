/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.bean;

import java.time.LocalDate;
import java.util.*;


public class EntityGenerater {
    public static Random  random = new Random();
    public static final Integer max = 99999;



    public static Person getPerson(){
            Person p = new Person();
            p.setAddress(getAddress());
            p.setFirstName("f"+random.nextInt(max));
            p.setLastName("L"+random.nextInt(max));
            p.setBirthDate(LocalDate.now());

            return p ;
    }

    public static Address getAddress(){
        Address address =new Address();
        address.setStreetName("street"+random.nextInt(max));
        address.setCity(getCity());
        return address ;
    }




    public static City getCity() {
        String[] zipCodes = {
                random.nextInt(9999) + "",
                random.nextInt(9999) + "",
                random.nextInt(9999) + "",
                random.nextInt(9999) + "",
                random.nextInt(9999) + ""
        };
        City city = new City();
        city.setId("102101021");
        city.setName("上海");
        city.setLat(102F);
        city.setLon(302F);
        city.setZipCodes(zipCodes);

        return city ;
    }






    public static Map<String,String> getCommunication() {
        /*List phone = new ArrayList();
        phone.add("13142075822");
        phone.add("13316677219");
        phone.add("13142075833");*/

        List weChat = new ArrayList();
        weChat.add("chenyou");
        weChat.add("chenyou2");
        weChat.add("chenyouiii");

        Map<String,String> communication = new HashMap();
        //communication.put("phone", phone);
        //communication.put("weChat", weChat);
        communication.put("email", "chenyouiii@163.com");
        communication.put("QQ", "1354329911");

        return communication;
    }





}
