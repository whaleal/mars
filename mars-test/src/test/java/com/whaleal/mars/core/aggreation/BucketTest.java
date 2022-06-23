package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Bucket;
import com.whaleal.mars.core.aggregation.stages.Facet;

import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.*;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.StringExpressions.concat;


/**
 * @author lyz
 * @desc
 * @date 2022-05-13 21:59
 */
public class BucketTest {

    Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.artists.insertMany([
     *   { "_id" : 1, "last_name" : "Bernard", "first_name" : "Emil", "year_born" : 1868, "year_died" : 1941, "nationality" : "France" },
     *   { "_id" : 2, "last_name" : "Rippl-Ronai", "first_name" : "Joszef", "year_born" : 1861, "year_died" : 1927, "nationality" : "Hungary" },
     *   { "_id" : 3, "last_name" : "Ostroumova", "first_name" : "Anna", "year_born" : 1871, "year_died" : 1955, "nationality" : "Russia" },
     *   { "_id" : 4, "last_name" : "Van Gogh", "first_name" : "Vincent", "year_born" : 1853, "year_died" : 1890, "nationality" : "Holland" },
     *   { "_id" : 5, "last_name" : "Maurer", "first_name" : "Alfred", "year_born" : 1868, "year_died" : 1932, "nationality" : "USA" },
     *   { "_id" : 6, "last_name" : "Munch", "first_name" : "Edvard", "year_born" : 1863, "year_died" : 1944, "nationality" : "Norway" },
     *   { "_id" : 7, "last_name" : "Redon", "first_name" : "Odilon", "year_born" : 1840, "year_died" : 1916, "nationality" : "France" },
     *   { "_id" : 8, "last_name" : "Diriks", "first_name" : "Edvard", "year_born" : 1855, "year_died" : 1930, "nationality" : "Norway" }
     * ])
     */

    /**
     * db.artwork.insertMany([
     *   { "_id" : 1, "title" : "The Pillars of Society", "artist" : "Grosz", "year" : 1926,
     *       "price" : NumberDecimal("199.99") },
     *   { "_id" : 2, "title" : "Melancholy III", "artist" : "Munch", "year" : 1902,
     *       "price" : NumberDecimal("280.00") },
     *   { "_id" : 3, "title" : "Dancer", "artist" : "Miro", "year" : 1925,
     *       "price" : NumberDecimal("76.04") },
     *   { "_id" : 4, "title" : "The Great Wave off Kanagawa", "artist" : "Hokusai",
     *       "price" : NumberDecimal("167.30") },
     *   { "_id" : 5, "title" : "The Persistence of Memory", "artist" : "Dali", "year" : 1931,
     *       "price" : NumberDecimal("483.00") },
     *   { "_id" : 6, "title" : "Composition VII", "artist" : "Kandinsky", "year" : 1913,
     *       "price" : NumberDecimal("385.00") },
     *   { "_id" : 7, "title" : "The Scream", "artist" : "Munch", "year" : 1893},
     *   {"_id":8,"title":"Blue Flower","artist":"O'Keefe","year":1918,"price":NumberDecimal("118.42")}])
     */
    @Before
    public void createData(){
        mars.insert(new Document("id","1").append("last_name","Bernard").append("first_name","Emil").append("year_born","1868").append("year_died","1941").append("nationality","France"),"artists");
        mars.insert(new Document("id","2").append("last_name","Rippl-Ronai").append("first_name","Joszef").append("year_born","1861").append("year_died","1941").append("nationality","Hungary"),"artists");
        mars.insert(new Document("id","3").append("last_name","Ostroumova").append("first_name","Anna").append("year_born","1871").append("year_died","1955").append("nationality","Russia"),"artists");
        mars.insert(new Document("id","4").append("last_name","Van Gogh").append("first_name","Vincent").append("year_born","1853").append("year_died","1890").append("nationality","Holland"),"artists");
        mars.insert(new Document("id","5").append("last_name","Maurer").append("first_name","Alfred").append("year_born","1868").append("year_died","1932").append("nationality","USA"),"artists");
        mars.insert(new Document("id","6").append("last_name","Munch").append("first_name","Edvard").append("year_born","1863").append("year_died","1944").append("nationality","Norway"),"artists");
        mars.insert(new Document("id","7").append("last_name","Redon").append("first_name","Odilon").append("year_born","1840").append("year_died","1916").append("nationality","France"),"artists");
        mars.insert(new Document("id","8").append("last_name","Diriks").append("first_name","Edvard").append("year_born","1855").append("year_died","1930").append("nationality","France"),"artists");

        mars.insert(new Document("_id" , 1).append( "title" , "The Pillars of Society").append( "artist", "Grosz").append( "year" , 1926).append("price",199.99),"artwork");
        mars.insert(new Document("_id" , 2).append( "title" , "Melancholy III").append( "artist" , "Munch").append( "year" , 1902).append("price",280.00),"artwork");
        mars.insert(new Document("_id" , 3).append("title" ,"Dancer").append( "artist" , "Miro").append( "year" , 1925).append("price" ,76.04 ),"artwork");
        mars.insert(new Document("_id" , 4).append("title" , "The Great Wave off Kanagawa").append( "artist" , "Hokusai").append("price" ,167.30),"artwork");
        mars.insert(new Document("_id" , 5).append("title" , "The Persistence of Memory").append( "artist" , "Dali").append("year" ,1931).append("price" ,483.00),"artwork");
        mars.insert(new Document( "_id" , 6).append("title" , "Composition VII").append( "artist", "Kandinsky").append( "year" , 1913).append("price" , 385.00),"artwork");
        mars.insert(new Document("_id" , 7).append("title" , "The Scream").append( "artist" ,"Munch").append( "year" , 1893),"artwork");
        mars.insert(new Document("_id" ,8).append( "title" , "Blue Flower").append("artist" , "O'Keefe").append( "year" , 1918).append("price" ,118.42),"artwork");
    }

    @After
    public void dropData(){
        mars.dropCollection("artists");
        mars.dropCollection("artwork");

    }

    /**
     * db.artists.aggregate( [
     *   // First Stage
     *   {
     *     $bucket: {
     *       groupBy: "$year_born",                        // Field to group by
     *       boundaries: [ 1840, 1850, 1860, 1870, 1880 ], // Boundaries for the buckets
     *       default: "Other",                             // Bucket id for documents which do not fall into a bucket
     *       output: {                                     // Output for each bucket
     *         "count": { $sum: 1 },
     *         "artists" :
     *           {
     *             $push: {
     *               "name": { $concat: [ "$first_name", " ", "$last_name"] },
     *               "year_born": "$year_born"
     *             }
     *           }
     *       }
     *     }
     *   },
     *   // Second Stage
     *   {
     *     $match: { count: {$gt: 3} }
     *   }
     * ] )
     */
    @Test
    public void testForBucket(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.bucket(Bucket.bucket().groupBy(field("year_born"))
                .boundaries(value("1840"),value("1850"),value("1860"), value("1870"), value("1880"))
                .defaultValue(value("other"))
                .outputField("count",sum(value(1)))
                .outputField("artists",push().field("name",concat(field("first_name"),field("last_name"))).field("year_born",field("year_born")))
                );

        pipeline.match(Filters.gt("count",3));

        QueryCursor<Document> artists = mars.aggregate(pipeline, "artists");

        while (artists.hasNext()){
            System.out.println(artists.next());
        }
    }

    /**
     * db.artwork.aggregate( [
     *   {
     *     $facet: {                               // Top-level $facet stage
     *       "price": [                            // Output field 1
     *         {
     *           $bucket: {
     *               groupBy: "$price",            // Field to group by
     *               boundaries: [ 0, 200, 400 ],  // Boundaries for the buckets
     *               default: "Other",             // Bucket id for documents which do not fall into a bucket
     *               output: {                     // Output for each bucket
     *                 "count": { $sum: 1 },
     *                 "artwork" : { $push: { "title": "$title", "price": "$price" } },
     *                 "averagePrice": { $avg: "$price" }
     *               }
     *           }
     *         }
     *       ],
     *       "year": [                                      // Output field 2
     *         {
     *           $bucket: {
     *             groupBy: "$year",                        // Field to group by
     *             boundaries: [ 1890, 1910, 1920, 1940 ],  // Boundaries for the buckets
     *             default: "Unknown",                      // Bucket id for documents which do not fall into a bucket
     *             output: {                                // Output for each bucket
     *               "count": { $sum: 1 },
     *               "artwork": { $push: { "title": "$title", "year": "$year" } }
     *             }
     *           }
     *         }
     *       ]
     *     }
     *   }
     * ] )
     */
    @Test
    public void testForMulti(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.facet(Facet.facet()
                .field("price",Bucket.bucket().groupBy(field("price"))
                .boundaries(value(0), value(200), value(400))
                .defaultValue("Other")
                .outputField("count",sum(value(1)))
                .outputField("artwork",push().field("title",field("title")).field("price",field("price")))
                .outputField("averagePrice",avg(field("price"))))

                .field("year",Bucket.bucket().groupBy(field("year"))
                .boundaries(value( 1890), value(1910), value(1920), value(1940 ))
                .defaultValue("unknown")
                .outputField("count",sum(value(1)))
                .outputField("artwork",push().field("title",field("title")).field("year",field("year"))
                )));


        QueryCursor<Document> artwork = mars.aggregate(pipeline, "artwork");
        while (artwork.hasNext()){
            System.out.println(artwork.next());
        }
    }


}
