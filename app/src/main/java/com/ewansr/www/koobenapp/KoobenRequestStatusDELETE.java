package com.ewansr.www.koobenapp;

import android.util.Log;
import org.json.JSONObject;

/**
 * Clase para estatus de tipo DELETE
 *
 * @author edmsamuel
 */
public class KoobenRequestStatusDELETE {

    public Boolean deleted;

    public KoobenRequestStatusDELETE( Boolean deleted ) {
        this.deleted = deleted;
    }


    public KoobenRequestStatusDELETE( JSONObject status ) {
        try {
            this.deleted = status.getBoolean( "deleted" );
        } catch ( Exception error ) {
            Log.e( this.getClass().toString(), error.getMessage() );
        }
    }

}
