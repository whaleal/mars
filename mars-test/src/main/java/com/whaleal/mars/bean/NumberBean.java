package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.CappedAt;
import com.whaleal.mars.codecs.pojo.annotations.Collation;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.index.annotation.Field;
import com.whaleal.mars.core.index.annotation.Index;
import com.whaleal.mars.core.index.annotation.IndexOptions;
import com.whaleal.mars.core.index.annotation.Indexes;

/**
 * @author lyz
 * @description
 * @date 2022-07-13 16:21
 **/
@Entity
@Collation(locale = "zh")
@CappedAt(count = 1000,value = 1024 * 1024)
@Indexes({@Index(fields = @Field(value = "stocks",type = IndexDirection.ASC)),
        @Index(fields = {@Field(value = "name",type = IndexDirection.ASC),
                @Field(value = "price",type = IndexDirection.DESC)}
                ,options = @IndexOptions(collation = @com.whaleal.mars.core.index.annotation.Collation(locale = "zh",numericOrdering = true)))})
public class NumberBean {

    public int stocks ;

    public String name ;

    public double price ;

}
