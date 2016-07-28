package com.ewansr.www.koobenapp;
import android.util.Log;
import org.json.JSONObject;


/**
 * Modelado de la respuesta para presentaciones
 *
 * @author edmsamuel
 */
public class APIPresentacionModel {

    public int id;
    public int productoId;
    public String nombre;
    public String imagen_url;

    public APIPresentacionModel( int id, int productoId, String nombre ) {
        this.id = id;
        this.productoId = productoId;
        this.nombre = nombre;
        this.imagen_url = KoobenResources.makeUrl( "/presentation_" + id + ".jpg" );
    }

    public APIPresentacionModel( JSONObject presentacion ) {
        try {
            this.id = presentacion.getInt( "id" );
            this.productoId = presentacion.getInt( "productoId" );
            this.nombre = presentacion.getString( "presentacionNombre" );
            this.imagen_url = KoobenResources.makeUrl( "/presentation_" + id + ".jpg" );
        } catch (  Exception error ) {
            Log.e( this.getClass().toString(), error.getMessage() );
        }
    }

}