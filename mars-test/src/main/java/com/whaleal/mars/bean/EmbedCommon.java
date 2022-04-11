package com.whaleal.mars.bean;

import org.bson.types.ObjectId;

/**
 * @author wh
 * @since 2022-03-04
 */
public class EmbedCommon {

    public ObjectId get_id() {
        return _id;
    }

    public void set_id( ObjectId _id ) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge( Integer age ) {
        this.age = age;
    }

    private ObjectId _id ;

    private String name ;

    private Integer age ;

    public Other getOther() {
        return other;
    }

    public void setOther( Other other ) {
        this.other = other;
    }

    private Other other ;



    public static class Other{
        public Other( String left, String right ) {
            this.left = left;
            this.right = right;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft( String left ) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight( String right ) {
            this.right = right;
        }

        private String left ;
        private String right ;
    }

}

