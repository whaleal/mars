package com.whaleal.mars.codecs.pojo;

import com.whaleal.mars.util.AESUtil;

/**
 * @author wh
 *
 * 针对字符串等格式 进行先关加密及解密的 实现类
 *
 * @see  com.whaleal.mars.codecs.pojo.annotations.PropEncrypt ;
 *
 */
class PropertyModelEncrptySerializationImpl implements PropertySerialization<String> {


    private String sKey ;
    private  boolean enableDecrypt  ;

    PropertyModelEncrptySerializationImpl( String sKey  ,boolean enableDecrypt) {
        super();
        this.sKey = sKey ;
        this.enableDecrypt = enableDecrypt ;

    }

    @Override
    public boolean shouldSerialize(final String value) {
        return value != null;
    }

    @Override
    public String serialize( String value ) {
        try {
            return AESUtil.encrypt(value , sKey);
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }

    }

    @Override
    public String deserialize( String value ) {
        if(enableDecrypt){
            try {
                return AESUtil.decrypt(value,sKey);
            }catch (Exception e){
                return null ;
            }
        }else {
            return value ;
        }


    }
}