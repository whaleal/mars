package com.whaleal.mars.bean;


import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.index.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


/**
 * @author lyz
 * @description
 * @date 2022-06-17 10:56
 **/
@Entity("book")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Collation(locale = "zh",strength = CollationStrength.TERTIARY,caseLevel = true,caseFirst = CollationCaseFirst.UPPER,numericOrdering = true,alternate = CollationAlternate.SHIFTED,
//maxVariable = CollationMaxVariable.PUNCT,backwards = true,normalization = true)
@Indexes({@Index(fields = @Field(value = "stocks",type = IndexDirection.ASC)),
          @Index(fields = {@Field(value = "name",type = IndexDirection.ASC),
                  @Field(value = "price",type = IndexDirection.DESC)}
        ,options = @IndexOptions(collation = @Collation(locale = "zh",numericOrdering = true)))})
public class Book {


    @Id
    private ObjectId id;

    private String name;

    private Double price;

    private int stocks;
}
