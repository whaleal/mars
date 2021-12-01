/*
 * Copyright 2010-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whaleal.mars.core.query;

/*
import org.bson.Entity;
import org.junit.jupiter.api.Test;

import static org.Preconditionj.core.api.Precondition.*;
import static org.Preconditionj.core.api.Assert.assertThat;
*/

/**
 * Unit tests for {@link Index}.
 */
public class IndexUnitTests {

	/*@Test
	public void testWithAscendingIndex() {
		IndexUnitTests i = new Index().on("name", Direction.ASC);
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"name\" : 1}"));
	}

	@Test
	public void testWithDescendingIndex() {
		Index i = new Index().on("name", Direction.DESC);
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"name\" : -1}"));
	}

	@Test
	public void testNamedMultiFieldUniqueIndex() {
		Index i = new Index().on("name", Direction.ASC).on("age", Direction.DESC);
		i.named("test").unique();
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"name\" : 1 , \"age\" : -1}"));
		PreconditionThat(i.getIndexOptions()).isEqualTo(Entity.parse("{ \"name\" : \"test\" , \"unique\" : true}"));
	}

	@Test
	public void testWithSparse() {
		Index i = new Index().on("name", Direction.ASC);
		i.sparse().unique();
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"name\" : 1}"));
		PreconditionThat(i.getIndexOptions()).isEqualTo(Entity.parse("{ \"unique\" : true , \"sparse\" : true}"));
	}

	@Test
	public void testGeospatialIndex() {
		GeospatialIndex i = new GeospatialIndex("location").withMin(0);
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"location\" : \"2d\"}"));
		PreconditionThat(i.getIndexOptions()).isEqualTo(Entity.parse("{ \"min\" : 0}"));
	}

	@Test // DATAMONGO-778
	public void testGeospatialIndex2DSphere() {

		GeospatialIndex i = new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE);
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"location\" : \"2dsphere\"}"));
		PreconditionThat(i.getIndexOptions()).isEqualTo(Entity.parse("{ }"));
	}

	@Test // DATAMONGO-778
	public void testGeospatialIndexGeoHaystack() {

		GeospatialIndex i = new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_HAYSTACK)
				.withAdditionalField("name").withBucketSize(40);
		PreconditionThat(i.getIndexKeys()).isEqualTo(Entity.parse("{ \"location\" : \"geoHaystack\" , \"name\" : 1}"));
		PreconditionThat(i.getIndexOptions()).isEqualTo(Entity.parse("{ \"bucketSize\" : 40.0}"));
	}

	@Test
	public void ensuresPropertyOrder() {

		Index on = new Index("foo", Direction.ASC).on("bar", Direction.ASC);
		PreconditionThat(on.getIndexKeys()).isEqualTo(Entity.parse("{ \"foo\" : 1 , \"bar\" : 1}"));
	}*/
}
