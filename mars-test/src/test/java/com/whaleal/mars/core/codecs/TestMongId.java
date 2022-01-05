package com.whaleal.mars.core.codecs;

import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Property;
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

        private static class TestEntity {
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

            public List< String > getListOfString() {
                return listOfString;
            }

            public void setListOfString( List< String > listOfString ) {
                this.listOfString = listOfString;
            }

            public List< List< String > > getListOfListOfString() {
                return listOfListOfString;
            }

            public void setListOfListOfString( List< List< String > > listOfListOfString ) {
                this.listOfListOfString = listOfListOfString;
            }

            public int[] getArrayOfInt() {
                return arrayOfInt;
            }

            public void setArrayOfInt( int[] arrayOfInt ) {
                this.arrayOfInt = arrayOfInt;
            }

            public Map< String, Integer > getMapOfInts() {
                return mapOfInts;
            }

            public void setMapOfInts( Map< String, Integer > mapOfInts ) {
                this.mapOfInts = mapOfInts;
            }

            public List< Embed > getListOfEmbeds() {
                return listOfEmbeds;
            }

            public void setListOfEmbeds( List< Embed > listOfEmbeds ) {
                this.listOfEmbeds = listOfEmbeds;
            }

            public Embed getEmbed() {
                return embed;
            }

            public void setEmbed( Embed embed ) {
                this.embed = embed;
            }

            @Id
            private String id;
            @Property("n")
            private String name;
            private List<String> listOfString;
            private List<List<String>> listOfListOfString;
            private int[] arrayOfInt;
            private Map<String, Integer> mapOfInts;
            private List<Embed> listOfEmbeds;
            private Embed  embed ;
        }

        private static class Embed {
            public String getEmbedName() {
                return embedName;
            }

            public void setEmbedName( String embedName ) {
                this.embedName = embedName;
            }

            public List< Embed > getEmbeddeds() {
                return embeddeds;
            }

            public void setEmbeddeds( List< Embed > embeddeds ) {
                this.embeddeds = embeddeds;
            }

            private String embedName;
            private List<Embed> embeddeds;
        }



}
