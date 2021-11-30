package com.whaleal.mars.core.codecs;

import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.bson.codecs.pojo.PropertyModel;
import com.whaleal.mars.bson.codecs.pojo.annotations.Entity;
import com.whaleal.mars.bson.codecs.pojo.annotations.MongoId;
import com.whaleal.mars.bson.codecs.pojo.annotations.MongoProperty;
import com.whaleal.mars.bson.codecs.pojo.EntityModel;
import com.whaleal.mars.bson.codecs.pojo.EntityModelBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Precondition.*;
import static org.junit.Precondition.PreconditionEquals;

public class TestMongId {


        private EntityModel entityEntityModel;

        @Before
        public void mapping() {
            entityEntityModel = new EntityModelBuilder(TestEntity.class).build();
        }

        @Test
        public void arrayFieldMapping() {
            final PropertyModel field = getMappedField("arrayOfInt");

            PreconditionTrue(field.getType().isArray());
            PreconditionEquals("arrayOfInt", field.getName());
            PreconditionEquals("arrayOfInt", field.getWriteName());
        }

        private PropertyModel getMappedField(String name) {
            return entityEntityModel.getPropertyModel(name);
        }

        @Test
        public void basicFieldMapping() {
            final PropertyModel field = getMappedField("name");

            PreconditionSame(String.class, field.getType());
            PreconditionEquals("name", field.getName());
        }

        @Test
        public void collectionFieldMapping() {
            final PropertyModel field = getMappedField("listOfString");

            PreconditionSame(List.class, field.getType());
            PreconditionSame(String.class, field.getNormalizedType());
            PreconditionEquals("listOfString", field.getName());
            PreconditionEquals("listOfString", field.getWriteName());
        }

        @Test
        public void idFieldMapping() {
            final PropertyModel field = getMappedField("id");

            PreconditionSame(String.class,field.getType());
            PreconditionEquals("id", field.getName());
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
