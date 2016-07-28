package com.ewansr.www.koobenapp;

import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Carrito de compra del usuario
 *
 * @author edmsamuel
 */
public abstract class APICarrito extends AppCompatActivity {
    private ArrayList<APICarritoItem> carritoItems;


    /**
     * Constructor
     *
     */
    public APICarrito() {
        carritoItems = new ArrayList<APICarritoItem>();
    }



    /**
     * Solicita a la API la obtención de los item en el carro de compra
     *
     */
    public void carritoGetList() {
        APIRequest request = new APIRequest(){
            @Override public void KoobenRequestBeforeExecute() {
                carritoItemsBeforeLoad();
            }

            @Override public void KoobenRequestCompleted(JSONObject response) {
                try {
                    KoobenRequestStatusGET status = new KoobenRequestStatusGET( response.getJSONObject( "status" ) );
                    JSONArray items = response.getJSONArray( "items" );
                    JSONObject item;
                    int count = items.length();

                    for ( int i = 0; i < count; i++ ) {
                        item = items.getJSONObject( i );
                        carritoItems.add( new APICarritoItem(
                                item.getInt( "id" ),
                                item.getInt( "productoId" ),
                                item.getString( "productoNombre" )
                        ) );
                    }

                    carritoItemsLoaded();
                } catch ( Exception error ) {
                    carritoItemsLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            }

            @Override public void KoobenRequestError(Exception error) {
                carritoItemsLoadError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
            }
        };
        request.get( "/carrito/lista" );
    }


    /**
     * Añade un nuevo elemento al carro de compra
     *
     * @param productoId int
     */
    public void addCarritoItem( int productoId ) {
        APICarritoItem item = new APICarritoItem( -1, productoId, "" ){
            @Override public void itemBeforeCreate() {
                carritoItemBeforeCreate();
            }

            @Override public void itemCreated(APICarritoItemModel item) {
                carritoItems.add( new APICarritoItem( item ) );
                carritoItemCreated( item );
            }

            @Override public void itemCreateError(KoobenException error) {
                carritoItemCreateError( error );
            }
        };
        item.save();
    }



    /**
     * Invoca la eliminación del elemento que se ubique en el indice especificado
     *
     * @param idx int Posición en la lista
     */
    public void carritoDeleteItemForIndex( int idx ) {
        final int indice = idx;
        APICarritoItem producto = new APICarritoItem( carritoItems.get( idx ) ){
            @Override public void itemBeforeDelete() {
                carritoItemBeforeDelete();
            }

            @Override public void itemDeleted( int id ) {
                carritoItems.remove( indice );
                carritoItemDeleted( id );
            }

            @Override public void itemDeleteError( KoobenException error ) {
                carritoItemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
            }
        };
        producto.delete();
    }



    // llamado antes de cargar la lista
    public abstract void carritoItemsBeforeLoad();
    // llamado al cargar la lista
    public abstract void carritoItemsLoaded();
    // llamado cuando ocurre un error al cargar la lista
    public abstract void carritoItemsLoadError( KoobenException error );

    // llamado antes de crear un nuevo item
    public abstract void carritoItemBeforeCreate();
    // llamado cuando se ha creado un nuevo item
    public abstract void carritoItemCreated( APICarritoItemModel item );
    // llamado cuando ocurre un error al crear un nuevo item
    public abstract void carritoItemCreateError( KoobenException error );

    // llamado antes eliminar un item
    public abstract void carritoItemBeforeDelete();
    // llamado al eliminar un item de la lista
    public abstract void carritoItemDeleted( int id );
    // llamado al ocurrir un error al eliminar un item
    public abstract void carritoItemDeleteError( KoobenException error );
}
