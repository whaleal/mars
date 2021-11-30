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


import com.whaleal.mars.codecs.pojo.annotations.MongoId;
import com.whaleal.mars.codecs.pojo.annotations.MongoProperty;

import java.util.ArrayList;
import java.util.List;


public class Car {
    @MongoId
    public String id;
    @MongoProperty()
    public Long carNum;
    public String model;
    public String size;

    @MongoProperty(value = "weig")
    public Double weight;
    public String out;
    public Address address;


    public Car(){}



    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }



    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @MongoProperty("cName")
    private  String  cc ;




    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }




  /* public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    //  数组元素 现在还不支持
    String[] stringArray = new String[]{"1","3","5"};*/


    public List<String> getListofString() {
        return listofString;
    }

    public void setListofString(List<String> listofString) {
        this.listofString = listofString;
    }

    List<String> listofString = new ArrayList<>();




}
