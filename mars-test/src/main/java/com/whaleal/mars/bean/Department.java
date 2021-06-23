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


import com.whaleal.mars.bson.codecs.pojo.annotations.Entity;

import java.util.List;

/**
 * @author: cx
 * @date: 2021/1/5
 */
@Entity
public class Department implements Comparable<Department> {
    private String name;
    private List<Employee> Employees;//员工列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return Employees;
    }

    public void setEmployees(List employees) {
        Employees = employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", Employees=" + Employees +
                '}';
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.Employees.hashCode();
    }

    @Override
    public int compareTo(Department o) {
        int result = 0;

        if (!this.name.equals(o.name)) {
            return this.name.hashCode() - o.name.hashCode();
        }

        if (!this.Employees.equals(o.Employees)) {
            return this.Employees.hashCode() - o.Employees.hashCode();
        }

        return result;


    }
}
