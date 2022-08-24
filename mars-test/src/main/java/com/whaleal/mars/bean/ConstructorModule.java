package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Constructor;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Property;

/**
 * @author wh
 */
public class ConstructorModule {
    @Override
    public String toString() {
        return "ConstructorModule{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }



    @Constructor
    public ConstructorModule( @Property("name") String name, @Property("age") Integer age ) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
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

    @Id
    private String id ;

    private String name ;

    private Integer age ;

}
