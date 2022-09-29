package com.whaleal.mars.core.domain;


import org.bson.BsonWriter;

import java.util.List;

/**
 * @author wh
 */
public interface ISort {

    public boolean isSorted() ;

    public boolean isEmpty() ;

    public boolean isUnsorted() ;


    public ISort ascending( String field, String... additional ) ;


    public ISort descending( String field, String... additional );

    public ISort meta( String field );


    public ISort and( ISort sort );


    public ISort natural( Direction direction );


    public List< SortType > getSorts();

    public void encode( BsonWriter writer );


}
