package com.ewansr.www.koobenapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Clase para solicitudes HTTP
 *
 * @author edmsamuel 20/06/16.
 */
public abstract class APIKoobenRequest extends AsyncTask<String, Void, JSONObject> {
    private Boolean hasErrors;
    private Exception error;
    private JSONObject datos;
    private JSONObject response;
    public String response_text;
    public APIKoobenRequestType tipo;
    public APIKoobenRequestHeaders headers;


    /**
     * Constructor.
     *
     */
    public APIKoobenRequest() {
        headers = new APIKoobenRequestHeaders();
    }




    /**
     * Llamado antes de iniciar el doInBackground
     *
     * @author edmsamuel
     */
    @Override
    public void onPreExecute() {
        KoobenRequestBeforeExecute();
    }



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
            Log.i( "KoobenRequest", "Preparando solicitud `" + getRequestMethod() + "` a `" + params[0] + "`" );
            InputStream inputStream;
            String line;
            String body;
            StringBuilder responseText = new StringBuilder();
            BufferedReader bufferedReader;
            int responseCode;

            URL url = new URL( params[0] );
            HttpURLConnection connection = ( HttpsURLConnection ) url.openConnection();
            connection.setDoInput(true);

            if ( this.tipo != APIKoobenRequestType.GET && this.tipo != APIKoobenRequestType.DELETE ) {

                int content_length;
                byte[] content_bytes;
                DataOutputStream dataOutputStream;
                APIKoobenRequestHeader header;

                // información del body
                body = datos.toString();
                content_bytes = body.getBytes();
                content_length = content_bytes.length;

                // debug
                Log.i( "KoobenRequest", "Adjuntando a la solicitud `" + body + "`" );

                // establecer headers.
                connection.setRequestProperty( "Content-Type", "application/json" );
                connection.setRequestProperty( "Content-Length", Integer.toString( content_length  ) );
                for ( int header_idx = 0; header_idx < headers.items.size(); header_idx++ ) {
                    header = headers.get( header_idx );
                    connection.setRequestProperty( header.name, header.value );
                }
                connection.setDoOutput( true );

                // añadir datos al body
                dataOutputStream = new DataOutputStream( connection.getOutputStream() );
                dataOutputStream.writeBytes( body );
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            // establecer el tipo de verbo y leer código de respuesta
            connection.setConnectTimeout( 15000 );

            try {
                connection.setRequestMethod( this.getRequestMethod() );
            } catch ( ProtocolException error ) {
                Log.e( this.getClass().toString(), error.getMessage() );
            }

            responseCode = connection.getResponseCode();
            hasErrors = ( responseCode != 200 );

            // obtener el resultado de la solicitud
            inputStream = new BufferedInputStream( connection.getInputStream() );
            bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );
            while ( ( line = bufferedReader.readLine() ) != null ) {
                responseText.append( line );
            }
            response_text = responseText.toString();

            // si la solicitud fue exitosa convertir el resultado en JSONObject
            if ( !hasErrors ) {
                resultado = new JSONObject( response_text );
                this.response = resultado;
            }

            // terminar la conexión
            connection.disconnect();

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
            Log.i( "KoobenRequest", "Solicitud completada con resultado `" + resultado.toString() + "`" );
            KoobenRequestCompleted( resultado );
        } else {
            Log.e( "KoobenRequest", "Error en la solicitud `" + error.getMessage() + "`" );
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
        this.tipo = APIKoobenRequestType.GET;
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
        this.tipo = APIKoobenRequestType.PUT;
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
        this.tipo = APIKoobenRequestType.DELETE;
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
        this.tipo = APIKoobenRequestType.POST;
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
        //String API_BASE_URL = "http://192.168.1.66/Repositories/Kooben-CocinaComparte/API";
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
     * Llamado antes de iniciar el doInBackground
     *
     * @author edmsamuel
     */
    public abstract void KoobenRequestBeforeExecute();



    /**
     * Es invocado al finalizar la tarea en segundo plano cuando no han ocurrido errores en la solicitud HTTP.
     *
     * @param response JSONObject Resultado recibido por el servidor en JSON.
     */
    public abstract void KoobenRequestCompleted( JSONObject response );



    /**
     * Es invocado en caso de ocurrir errores durante el envio de la solicutud.
     *
     * @param error Exception Resultado recibido por el servidor en JSON.
     */
    public abstract void KoobenRequestError( Exception error );
}