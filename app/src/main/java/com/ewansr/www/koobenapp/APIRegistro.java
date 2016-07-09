package com.ewansr.www.koobenapp;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

/**
 * @author edmsamuel 22/06/16.
 */
public class APIRegistro extends APIKoobenRequest {

    public Context context;
    public ProgressDialog progressDialog;



    /**
     * Metodo constructor
     *
     */
    public APIRegistro( Context context ) {
        this.context = context;
    }


    /**
     * Solicita la creación de un nuevo usuario
     *
     * @param usuario JSONObject Datos del nuevo usuario
     */
    public void registrar( JSONObject usuario ) {
        headers.add( "KOOBEN APPLICATION NAME", "android" );
        post( "/users", usuario );
    }



    /**
     * Llamado antes de iniciar el doInBackground
     *
     * @author edmsamuel
     */
    @Override
    public void KoobenRequestBeforeExecute() {
        progressDialog = ProgressDialog.show( context, "Creando su cuenta", "Por favor espere...", true, true );
    }



    /**
     * Llamado al terminar la solicitud
     *
     * @param response JSONObject
     */
    @Override
    public void KoobenRequestCompleted( JSONObject response ) {
        try {
            JSONObject status = response.getJSONObject( "status" );
            Boolean existe = status.getBoolean( "exists" );
            Boolean creado = status.getBoolean( "created" );
            Boolean valido = status.getBoolean( "valid" );

            if ( existe ) {
                throw new Exception( "Ya existe una cuenta existente con los datos proporcionados" );
            } else if ( !creado ) {
                throw new Exception( "Lo sentimos su cuenta no pudo ser creada, favor de verificar tus datos" );
            } else if ( !valido ) {
                throw new Exception( "Los datos proporcionados son incorrectos" );
            }

            APIRegistroModel usuario = new APIRegistroModel();
            usuario.mail = response.getString( "sMail" );
            usuario.nombre = response.getString( "sNombre" );
            usuario.imagenurl = response.getString( "sImg" );
            usuario.apellidos = response.getString( "sApellidos" );
            usuario.session = response.getString( "sessionId" );
            registroExitoso( usuario );

        } catch ( Exception error ) {
            registroError( error.getMessage() );
        }

        if ( progressDialog.isShowing() ) {
            progressDialog.dismiss();
        }
    }



    /**
     * Llamado al terminar la solicitud
     *
     * @param error Exception Resultado recibido por el servidor en JSON.
     */
    @Override
    public void KoobenRequestError( Exception error ) {
        printError(); registroError( error.getMessage() );
    }



    /**
     * Llamado en caso que el registo esté completo.
     *
     * @param usuario APIRegistroModel
     */
    public void registroExitoso( APIRegistroModel usuario ) {
    }



    /**
     * Llamado en caso que el registro no se haya completado.
     * @param error String Mensaje de error
     */
    public void registroError( String error ) {
    }
}
