package com.ewansr.www.koobenapp;

import android.util.Log;

import org.json.JSONObject;

/**
 * Clase para estatus de tipo POST
 *
 * @author edmsamuel
 */
public class KoobenRequestStatusPOST {
    public Boolean created;
    public Boolean valid;

    public KoobenRequestStatusPOST( Boolean created, Boolean valid ) {
        this.created = created;
        this.valid = valid;
    }



    public KoobenRequestStatusPOST( JSONObject status ) {
        try {
            this.created = status.getBoolean( "created" );
            this.valid = status.getBoolean( "valid" );
        } catch ( Exception error ) {
            Log.e( this.getClass().toString(), error.getMessage() );
        }
    }
}
