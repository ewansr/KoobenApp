package com.ewansr.www.koobenapp;

import android.util.Log;

import org.json.JSONObject;

/**
 * Clase para estatus de tipo PUT
 *
 * @author edmsamuel
 */
public class KoobenRequestStatusPUT {

    public Boolean updated;

    public KoobenRequestStatusPUT( Boolean updated ){
        this.updated = updated;
    }

    public KoobenRequestStatusPUT( JSONObject status ) {
        try {
            this.updated = status.getBoolean( "updated" );
        } catch ( Exception error ) {
            Log.e( this.getClass().toString(), error.getMessage() );
        }
    }

}
