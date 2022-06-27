package com.whaleal.mars.bean;

import com.mongodb.client.model.*;
import com.whaleal.mars.codecs.pojo.annotations.Collation;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;

/**
 * @author lyz
 * @description
 * @date 2022-06-17 10:56
 **/
@Entity("book")
@Collation(locale = "zh",strength = CollationStrength.TERTIARY,caseLevel = true,caseFirst = CollationCaseFirst.UPPER,numericOrdering = true,alternate = CollationAlternate.SHIFTED,
maxVariable = CollationMaxVariable.PUNCT,backwards = true,normalization = true)
public class Book {

    private String name;

    private Double price;

    private int stocks;
}
