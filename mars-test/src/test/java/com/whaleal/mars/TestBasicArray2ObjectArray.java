package com.whaleal.mars;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import org.junit.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestBasicArray2ObjectArray {


    @Test
    public void  testConvert(){

        Object arr  = new int[]{1,3,5,7,9};

        System.out.println(arr.getClass());

        Assert.assertTrue(arr instanceof int[]);

        Assert.assertFalse(arr instanceof long[]);


        BasicDBList  ret = new BasicDBList();


        int [] data = (int[])  arr ;
        List t = Arrays.asList((int[])  arr ) ;
        ret.addAll(Arrays.asList(data));


        System.out.println(ret.get(0).toString());



        Integer[] Intr   = new Integer[]{1,3,5,7,9};
        Object[]  ret1 = (Object[]) Intr;

        System.out.println(ret1);
    }




    public void test02(){
       // Filters.geoWithinPolygon();
    }




}
