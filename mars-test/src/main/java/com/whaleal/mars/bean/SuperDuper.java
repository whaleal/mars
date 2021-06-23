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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class SuperDuper {

    private Set<Person> personSet;
    private List<Person> personList;
    private SortedSet<Person> personSortedSet;
    private Map<String, Person> personMap;
    private Map<String, String> stringMap;

    /**
     * @return the personSet
     */
    public Set<Person> getPersonSet() {
        return personSet;
    }

    /**
     * @param personSet the personSet to set
     */
    public void setPersonSet(Set<Person> personSet) {
        this.personSet = personSet;
    }

    /**
     * @return the personList
     */
    public List<Person> getPersonList() {
        return personList;
    }

    /**
     * @param personList the personList to set
     */
    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    /**
     * @return the personSortedSet
     */
    public SortedSet<Person> getPersonSortedSet() {
        return personSortedSet;
    }

    /**
     * @param personSortedSet the personSortedSet to set
     */
    public void setPersonSortedSet(SortedSet<Person> personSortedSet) {
        this.personSortedSet = personSortedSet;
    }

    /**
     * @return the personMap
     */
    public Map<String, Person> getPersonMap() {
        return personMap;
    }

    /**
     * @param personMap the personMap to set
     */
    public void setPersonMap(Map<String, Person> personMap) {
        this.personMap = personMap;
    }

    /**
     * @return the stringMap
     */
    public Map<String, String> getStringMap() {
        return stringMap;
    }

    /**
     * @param stringMap the stringMap to set
     */
    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((personList == null) ? 0 : personList.hashCode());
        result = prime * result + ((personMap == null) ? 0 : personMap.hashCode());
        result = prime * result + ((personSet == null) ? 0 : personSet.hashCode());
        result = prime * result + ((personSortedSet == null) ? 0 : personSortedSet.hashCode());
        result = prime * result + ((stringMap == null) ? 0 : stringMap.hashCode());
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
        SuperDuper other = (SuperDuper) obj;
        if (personList == null) {
            if (other.personList != null) return false;
        } else if (!personList.equals(other.personList)) return false;
        if (personMap == null) {
            if (other.personMap != null) return false;
        } else if (!personMap.equals(other.personMap)) return false;
        if (personSet == null) {
            if (other.personSet != null) return false;
        } else if (!personSet.equals(other.personSet)) return false;
        if (personSortedSet == null) {
            if (other.personSortedSet != null) return false;
        } else if (!personSortedSet.equals(other.personSortedSet)) return false;
        if (stringMap == null) {
            if (other.stringMap != null) return false;
        } else if (!stringMap.equals(other.stringMap)) return false;
        return true;
    }

}
