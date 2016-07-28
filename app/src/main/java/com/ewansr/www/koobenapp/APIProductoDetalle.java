package com.ewansr.www.koobenapp;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * Clase para el detalle de un producto
 *
 * @author edmsamuel
 */
public abstract class APIProductoDetalle extends AppCompatActivity {

    private int productoId;
    private ArrayList<APIPresentacionModel> productoPresentaciones;
    private String productoImagenUrl;


    /**
     * Constructor
     *
     */
    public APIProductoDetalle() {
        productoPresentaciones = new ArrayList<APIPresentacionModel>();
    }



    /**
     * Debe llamarse para que la clase sepa consultar la información del producto correcto
     *
     * @param producto Id del producto
     */
    public void inicializarProducto( int producto ) {
        productoPresentaciones.clear();
        productoId = producto;
        productoImagenUrl = KoobenResources.makeUrl( "/supply_" + productoId + ".jpg" );

        APIRequest presentaciones = new APIRequest(){
            @Override public void KoobenRequestBeforeExecute() {
                presentacionesBeforeLoad();
            }

            @Override public void KoobenRequestCompleted(JSONObject response) {
                try {

                    KoobenRequestStatusGET status = new KoobenRequestStatusGET( response.getJSONObject( "status" ) );
                    if ( !status.found ) {
                        throw new KoobenException( KoobenExceptionCode.ITEMS_EMPTY, "No se encontraron presentaciones para el producto" );
                    }

                    JSONArray items = response.getJSONArray( "items" );
                    for ( int i = 0; i < status.count; i++ ) {
                        productoPresentaciones.add( new APIPresentacionModel( items.getJSONObject(i) ) );
                    }

                    presentacionesLoaded();

                } catch ( KoobenException error ) {
                    presentacionesLoadError( error );
                } catch ( Exception error ) {
                    presentacionesLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            }

            @Override public void KoobenRequestError(Exception error) {
                presentacionesLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
            }
        };
        presentaciones.get( "/productos/" + producto + "/presentaciones" );
    }



    // llamado antes de cargar las presentaciones
    public abstract void presentacionesBeforeLoad();
    // llamado al cargar las presentaciones
    public abstract void presentacionesLoaded();
    // llamado cuando hay error al cargar las presentaciones
    public abstract void presentacionesLoadError( KoobenException error );
}
