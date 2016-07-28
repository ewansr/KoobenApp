package com.ewansr.www.koobenapp;

/**
 * Item del carro de compra
 *
 * @author edmsamuel
 */
public class APICarritoItemModel {

    public int id;
    public int productoId;
    public String productoNombre;

    public APICarritoItemModel( int id, int productoId, String productoNombre ) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
    }
}
