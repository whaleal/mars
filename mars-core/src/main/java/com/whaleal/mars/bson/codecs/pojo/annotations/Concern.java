package com.whaleal.mars.bson.codecs.pojo.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface  Concern {

    String writeConcern() default "";

    String readConcern() default "";

    String readPreference() default "" ;

}
