package com.ewansr.www.koobenapp;

/**
 * Modelo de Producto
 *
 * @author edmsamuel
 */
public class APIProductoModel {
    public int id;
    public int tipoId;
    public String tipoNombre;
    public String nombre;
    public String descripcion;
    public String image_url;

    public APIProductoModel( int id, int tipoId, String tipoNombre, String nombre, String descripcion ) {
        this.id = id;
        this.tipoId = tipoId;
        this.tipoNombre = tipoNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.image_url = KoobenResources.makeUrl( "/supply_" + this.id + ".jpg" );
    }
}
