package com.whaleal.mars.bean;

import com.whaleal.icefrog.core.annotation.pojo.Id;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.PropEncrypt;
import lombok.Data;
import lombok.ToString;

/**
 * @author wh
 */
@Entity
@Data
@ToString
public class BankCard {

    @Id
    String _id ;


    String  cardNum ;

    @PropEncrypt(enableDecrypt = false)
    String password ;

}
