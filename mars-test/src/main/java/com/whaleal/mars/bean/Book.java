package com.whaleal.mars.bean;


import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
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
//@Indexes(@Index(fields = @Field(value = "name",type = IndexDirection.ASC),options = @IndexOptions(collation = @Collation(locale = "zh"))))
public class Book {

    @Id
    private ObjectId objectId;

    private String name;

    private Double price;

    private int stocks;
}
