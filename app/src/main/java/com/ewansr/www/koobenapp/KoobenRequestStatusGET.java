package com.ewansr.www.koobenapp;

import android.util.Log;

import org.json.JSONObject;

/**
 * Clase para estatus de tipo GET
 *
 * @author edmsamuel
 */
public class KoobenRequestStatusGET {

    public Boolean found;
    public int count;


    /**
     * Metodo constructor
     *
     * @param found Boolean Encontrado(s)
     * @param count int Cantidad de elementos
     * @author edmsamuel
     */
    public KoobenRequestStatusGET( Boolean found, int count ) {
        this.found = found;
        this.count = count;
    }


    /**
     * Metodo constructor a partir de un objeto de JSON
     *
     * @param status Objeto de JSON.
     * @author edmsamuel
     */
    public KoobenRequestStatusGET( JSONObject status ) {
        try {
            this.found = status.getBoolean( "found" );

            if ( status.has( "count" ) ) {
                this.count = status.getInt( "count" );
            }

        } catch ( Exception error ) {
            Log.e( this.getClass().toString(), error.getMessage() );
        }
    }

}
