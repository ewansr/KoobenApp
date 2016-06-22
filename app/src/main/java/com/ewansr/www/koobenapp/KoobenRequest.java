package com.ewansr.www.koobenapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.io.InputStream;

/**
 * Clase para solicitudes HTTP
 *
 * @author edmsamuel 20/06/16.
 */
public abstract class KoobenRequest extends AsyncTask<String, Void, JSONObject> {
    private Boolean hasErrors;
    private Exception error;
    private JSONObject datos;
    private JSONObject response;
    public String response_text;
    public KoobenRequestType tipo;



    /**
     * Proceso de ejecución en segundo plano.
     *
     * @param params String Lista de urls( solo se toma la primera ).
     * @return JSONObject
     */
    @Override
    protected JSONObject doInBackground( String... params ) {
        JSONObject resultado = new JSONObject();
        response_text = "";

        try {
            InputStream inputStream;
            String line;
            String body;
            StringBuilder responseText = new StringBuilder();
            BufferedReader bufferedReader;
            int responseCode;

            URL url = new URL( params[0] );
            HttpURLConnection url_connection = ( HttpURLConnection ) url.openConnection();

            if ( this.tipo != KoobenRequestType.GET && this.tipo != KoobenRequestType.DELETE ) {

                int content_length;
                byte[] content_bytes;
                DataOutputStream dataOutputStream;

                body = datos.toString();
                content_bytes = body.getBytes();
                content_length = content_bytes.length;

                url_connection.setRequestProperty( "Content-Type", "application/json" );
                url_connection.setRequestProperty( "Content-Length", Integer.toString( content_length  ) );
                url_connection.setDoOutput( true );

                dataOutputStream = new DataOutputStream( url_connection.getOutputStream() );
                dataOutputStream.writeBytes( body );
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            url_connection.setRequestMethod( this.getRequestMethod() );
            responseCode = url_connection.getResponseCode();
            hasErrors = ( responseCode != 200 );

            inputStream = new BufferedInputStream( url_connection.getInputStream() );
            bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );

            while ( ( line = bufferedReader.readLine() ) != null ) {
                responseText.append( line );
            }

            response_text = responseText.toString();

            if ( !hasErrors ) {
                resultado = new JSONObject( response_text );
                this.response = resultado;
            }

            url_connection.disconnect();
        } catch ( Exception error ) {
            hasErrors = true;
            this.error = error;
        }

        return resultado;
    }



    /**
     * Llamado al terminar el proceso en segúndo plano.
     *
     * @param resultado JSONObject Recibido al terminar el proceso en segundo plano.
     */
    @Override
    protected void onPostExecute( JSONObject resultado ) {
        if ( !hasErrors ) {
            KoobenRequestCompleted( resultado );
        } else {
            KoobenRequestError( error );
        }
    }



    /**
     * Envia una solitud de tipo GET.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void get( String path ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = KoobenRequestType.GET;
        this.hasErrors = false;
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void put( String path, JSONObject datos ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = KoobenRequestType.PUT;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void delete( String path ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = KoobenRequestType.DELETE;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Envia una solitud de tipo POST.
     *
     * @param path String Ruta a la que se desea acceder.
     */
    public void post( String path, JSONObject datos ) {
        String[] urls = { prepareUrl( path ) };
        this.tipo = KoobenRequestType.POST;
        this.hasErrors = false;
        this.setBody( datos );
        this.execute( urls );
    }



    /**
     * Establece los datos al cuerpo de la solicitud.
     *
     * @param datos JSONObject Datos a enviar en el cuerpo de la solicitud.
     */
    public void setBody( JSONObject datos ) {
        this.datos = datos;
    }



    /**
     * Imprime en la consola el error.
     *
     */
    public void printError() {
        if ( error != null ) {
            Log.e( "KoobenRequestError", error.getMessage() + " : " + response_text );
        } else {
            Log.e( "KoobenRequestError", response_text );
        }
    }



    /**
     * Construye una url completa.
     *
     * @param path String Path ó Ruta en la API.
     * @return String
     */
    private String prepareUrl( String path ) {
        String API_BASE_URL = "https://www.inteli-code.com.mx/CocinaComparte/API";
        return API_BASE_URL + path;
    }



    /**
     * Retorna el tipo de verbo HTTP.
     *
     * @return String
     */
    private String getRequestMethod() {
        String method = "";
        switch ( this.tipo ) {
            case GET:
                method = "GET";
                break;
            case POST:
                method = "POST";
                break;
            case PUT:
                method = "PUT";
                break;
            case DELETE:
                method = "DELETE";
                break;
        }

        return method;
    }



    /**
     * Es invocado al finalizar la tarea en segundo plano cuando no han ocurrido errores en la solicitud HTTP.
     *
     * @param result JSONObject Resultado recibido por el servidor en JSON.
     */
    public abstract void KoobenRequestCompleted( JSONObject result );



    /**
     * Es invocado en caso de ocurrir errores durante el envio de la solicutud.
     *
     * @param error Exception Resultado recibido por el servidor en JSON.
     */
    public abstract void KoobenRequestError( Exception error );
}