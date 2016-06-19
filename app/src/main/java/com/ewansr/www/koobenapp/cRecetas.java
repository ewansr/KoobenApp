package com.ewansr.www.koobenapp;

/**
 * Created by EwanS on 08/06/2016.
 */
public class cRecetas {
    int id;
    String name;
    String code;
    String preparation;
    String url_img;

    cRecetas(int id,String name, String code, String url_img, String preparation) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.preparation = preparation;
        this.url_img = url_img;
    }

    public int getId(){
        return this.id;
    }
}
