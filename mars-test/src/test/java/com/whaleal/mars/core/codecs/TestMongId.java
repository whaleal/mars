package com.whaleal.mars.core.codecs;

import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.MongoId;
import com.whaleal.mars.codecs.pojo.annotations.MongoProperty;
import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.codecs.pojo.EntityModelBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestMongId {


        private EntityModel entityEntityModel;

        @Before
        public void mapping() {
            entityEntityModel = new EntityModelBuilder(TestEntity.class).build();
        }

        @Test
        public void arrayFieldMapping() {
            final PropertyModel field = getMappedField("arrayOfInt");

            assertTrue(field.getType().isArray());
            assertEquals("arrayOfInt", field.getName());
            assertEquals("arrayOfInt", field.getWriteName());
        }

        private PropertyModel getMappedField(String name) {
            return entityEntityModel.getPropertyModel(name);
        }

        @Test
        public void basicFieldMapping() {
            final PropertyModel field = getMappedField("name");

            assertSame(String.class, field.getType());
            assertEquals("name", field.getName());
        }

        @Test
        public void collectionFieldMapping() {
            final PropertyModel field = getMappedField("listOfString");

            assertSame(List.class, field.getType());
            assertSame(String.class, field.getNormalizedType());
            assertEquals("listOfString", field.getName());
            assertEquals("listOfString", field.getWriteName());
        }

        @Test
        public void idFieldMapping() {
            final PropertyModel field = getMappedField("id");

            assertSame(String.class,field.getType());
            assertEquals("id", field.getName());
        }


        private List<String> dbList(String... values) {
            return new ArrayList<>(Arrays.asList(values));
        }

        @Entity
        private static class TestEntity {
            @MongoId
            private String id;
            @MongoProperty("n")
            private String name;
            private List<String> listOfString;
            private List<List<String>> listOfListOfString;
            private int[] arrayOfInt;
            private Map<String, Integer> mapOfInts;
            private List<Embed> listOfEmbeds;
            private Embed  embed ;
        }

        @Entity
        private static class Embed {
            private String embedName;
            private List<Embed> embeddeds;
        }



}
