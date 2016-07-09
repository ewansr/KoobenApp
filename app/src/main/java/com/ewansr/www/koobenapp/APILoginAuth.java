package com.ewansr.www.koobenapp;

import android.util.Log;
import org.json.JSONObject;

/**
 * Operaciones con las rutas de autenticación de la API.
 *
 * @author edmsamuel 21/06/16.
 */
public class APILoginAuth extends APIKoobenRequest implements APILoginAuthInterface {

    /**
     * Solicita a la api una nueva autenticación
     *
     * @param mail String Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    public void autenticar( String mail, String password ) {
        try {
            JSONObject datos = new JSONObject();
            datos.put( "mail", mail );
            datos.put( "password", password );
            headers.clear();
            headers.add( "KOOBEN APPLICATION NAME", "android" );
            post( "/auth", datos );
        } catch ( Exception e ) {}
    }



    public void autenticarConFacebook( String mail, String token ) {
        try {
            JSONObject usuario = new JSONObject();
            usuario.put( "mail", mail );
            usuario.put( "oauth", "facebook" );
            usuario.put( "updateToken", true );
            usuario.put( "token", token );
            headers.clear();
            headers.add( "KOOBEN-APPLICATION-NAME", "android" );
            post( "/auth", usuario );

        } catch ( Exception error ) {
            Log.e( "edmsamuel", error.getMessage() );
        }
    }


    /**
     * Llamado al concluir la solicitud de autenticación.
     *
     * @param response JSONObject Resultado de la respuesta.
     */
    @Override public void KoobenRequestCompleted( JSONObject response ) {
        try {
            JSONObject status = response.getJSONObject( "status" );
            Boolean found = status.getBoolean( "found" );
            Boolean valid = status.getBoolean( "valid" );

            if ( found && valid ) {
                APILoginAuthModel autenticacion = new APILoginAuthModel();
                autenticacion.username = response.getString( "username" );
                autenticacion.nombre = response.getString( "name" );
                autenticacion.apellidos = response.getString( "lastName" );
                autenticacion.imagenurl = response.getString( "image" );
                autenticacion.session = response.getString( "sessionId" );

                autenticacionExitosa( autenticacion );
            } else {
                autenticacionFallida();
            }
        } catch ( Exception e ) {
            Log.e( this.getClass().toString(), e.getMessage() );
        }
    }


    /**
     * Llamado en caso de ocurrir error en la autenticación
     *
     * @param error Exception Resultado recibido por el servidor en JSON.
     */
    @Override public void KoobenRequestError( Exception error ) {
        printError();
        autenticacionFallida();
    }



    /**
     * Debe ser llamado en caso que la autenticación sea exitosa.
     *
     * @param usuario APIModelLoginAuth Resultado de la autenticación
     */
    @Override public void autenticacionExitosa( APILoginAuthModel usuario ) {
    }



    /**
     * Debe ser llamado en caso que la autenticación sea incorrecta.
     *
     */
    @Override public void autenticacionFallida() {
    }
}
