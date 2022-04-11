package com.whaleal.mars.core;


import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TestParameterizedType {


    private List< Character > list;

    private Map< String, Integer > map;

    private Map< String, Integer > map2;
    private Map map3;

    @Test
    public void testMap() throws NoSuchFieldException {
        Field listField = TestParameterizedType.class.getDeclaredField("list");
        Field mapField = TestParameterizedType.class.getDeclaredField("map");
        //对比 Projection 类的 getType() 和 getGenericType()
        System.out.println(listField.getType());        // interface java.util.List
        System.out.println(listField.getGenericType()); // java.util.List<java.lang.Character>
        System.out.println(mapField.getType());         // interface java.util.Map
        System.out.println(mapField.getGenericType());  // java.util.Map<java.lang.String, java.lang.Integer>

        //获取 list 字段的泛型参数
        ParameterizedType listGenericType = (ParameterizedType) listField.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        for (int i = 0; i < listActualTypeArguments.length; i++) {
            System.out.println(listActualTypeArguments[i]);
        }
        // class java.lang.Character

        //获取 map 字段的泛型参数  会有异常情况   比如他没有泛型时
        ParameterizedType mapGenericType = (ParameterizedType) mapField.getGenericType();
        Type[] mapActualTypeArguments = mapGenericType.getActualTypeArguments();
        for (int i = 0; i < mapActualTypeArguments.length; i++) {
            System.out.println(mapActualTypeArguments[i]);
        }
        // class java.lang.String
        // class java.lang.Integer

        Type genericType = mapField.getGenericType();

        System.out.println(genericType instanceof ParameterizedType);

    }


    @Test
    public void testGenericType() throws NoSuchFieldException {
        Field mapField2 = TestParameterizedType.class.getDeclaredField("map2");


        Type genericType2 = mapField2.getGenericType();


        System.out.println(genericType2);


        Field mapField3 = TestParameterizedType.class.getDeclaredField("map3");


        Type genericType3 = mapField3.getGenericType();

        System.out.println(genericType3);


    }
}
