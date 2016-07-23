package com.ewansr.www.koobenapp;

import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Clase para la carga de productos
 *
 * @author edmsamuel
 */
public abstract class APIProductos extends AppCompatActivity {
    private ArrayList<APIProductoModel> productosItems;
    private int productosDesdeId = -1;
    private int productosHasta = 15;


    /**
     * Constructor
     *
     */
    public APIProductos() {
        productosItems = new ArrayList<APIProductoModel>();
    }


    /**
     * Solicita la siguiente pagina
     *
     */
    public void productosLoadNextPage() {
        APIRequest request = new APIRequest(){
            @Override public void KoobenRequestBeforeExecute() {
                productosBeforeLoad();
            }

            @Override public void KoobenRequestCompleted(JSONObject response) {
                try {
                    KoobenRequestStatusGET status = new KoobenRequestStatusGET( response.getJSONObject( "status" ) );
                    if ( !status.found ) {
                        throw new KoobenException( KoobenExceptionCode.ITEMS_EMPTY, "No se encontrarón productos" );
                    }

                    ArrayList<APIProductoModel> nuevos_items = new ArrayList<APIProductoModel>();
                    JSONArray items = response.getJSONArray( "items" );
                    JSONObject item;

                    for ( int i = 0; i < status.count; i++ ) {
                        item = items.getJSONObject( i );
                        APIProductoModel nuevo_producto = new APIProductoModel(
                                item.getInt( "id" ),
                                item.getInt( "tipoId" ),
                                item.getString( "tipoNombre" ),
                                item.getString( "nombre" ),
                                item.getString( "descripcion" )
                        );
                        productosItems.add( nuevo_producto );
                        nuevos_items.add( nuevo_producto );
                    }

                    int last_idx = productosItems.size() - 1;
                    if ( last_idx >= 0 ) {
                        productosDesdeId = productosItems.get( last_idx ).id;
                    }

                    productosItemsLoaded( nuevos_items );
                } catch ( KoobenException error ) {
                    productosLoadError( error );
                } catch ( Exception error ) {
                    productosLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            }

            @Override public void KoobenRequestError(Exception error) {
                productosLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
            }
        };
        request.get( "/productos/paginar/" + productosDesdeId + "/" + productosHasta );
    }




    // invocado antes de enviar la solicitud para la obtención de la lista
    public abstract void productosBeforeLoad();
    // invocado cuando se ha recibido la lista
    public abstract void productosItemsLoaded( ArrayList<APIProductoModel> items );
    // invocado en caso que haya un error al obtener la lista
    public abstract void productosLoadError( KoobenException error );
}
