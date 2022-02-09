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


import com.whaleal.mars.codecs.pojo.annotations.Property;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Representation;
import org.bson.BsonType;

import java.time.LocalDate;



public class Person
        implements Comparable<Person> {
    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = _id;
    }


    @Representation(BsonType.OBJECT_ID)
    private String id;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    private LocalDate birthDate;

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    private Car[] cars = new Car[]{new Car(),new Car()};


    @Property("fName")
    private String firstName;
    private String lastName;
    private Address address;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Representation(BsonType.INT32)
    private String age = "18";


    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Property(value = "tall")
    @Representation(BsonType.STRING)
    private Double  height = 1.8D ;



    public Person() {
    }



    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        if (address == null) {
            if (other.address != null) return false;
        } else if (!address.equals(other.address)) return false;
        if (firstName == null) {
            if (other.firstName != null) return false;
        } else if (!firstName.equals(other.firstName)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        return true;
    }

    public int compareTo(Person o) {
        return this.firstName.compareTo(o.firstName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", birthDate=" + birthDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                '}';
    }
}
