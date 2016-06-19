package com.ewansr.www.koobenapp;

/**
 * Created by Saulo Euan on 31/05/2016.
 */
public class cTiposReceta {
    int id;
    String codigo;
    String nombre;

    public cTiposReceta(int id, String codigo, String nombre){
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getId(){
        return this.id;
    }

    public String getCode(){
        return this.codigo;
    }

    public String getName(){
        return this.nombre;
    }

    @Override
    public String toString(){
        return this.nombre;
    }
}
