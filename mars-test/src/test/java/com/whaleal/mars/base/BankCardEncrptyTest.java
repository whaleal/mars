package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.BankCard;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.QueryCursor;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wh
 */
public class BankCardEncrptyTest {

    Mars mars ;

    @Before
    public void init(){
        mars = new Mars(Constant.connectionStr);

    }


    @Test
    public void testInsert(){
        BankCard bankCard = new BankCard();

        bankCard.setCardNum("carnumber124932849329");

        bankCard.setPassword("password");



        mars.insert(bankCard);



    }


    @Test
    public void testRead(){

        QueryCursor< BankCard > all = mars.findAll(BankCard.class);


        while (all.hasNext()){
            System.out.println(all.next());
        }


    }


}
