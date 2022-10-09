package com.whaleal.mars.core.domain;


import org.bson.BsonWriter;

import java.util.List;

/**
 * @author wh
 */
public interface ISort {

    boolean isSorted() ;

    boolean isEmpty() ;

    boolean isUnsorted() ;


    ISort ascending( String field, String... additional ) ;


    ISort descending( String field, String... additional );

    ISort meta( String field );


    ISort and( ISort sort );


    ISort natural( Direction direction );


    List< SortType > getSorts();

    void encode( BsonWriter writer );


}
