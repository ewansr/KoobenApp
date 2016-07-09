package com.ewansr.www.koobenapp;

import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Operaciones con menús del usuario
 *
 * @author edmsamuel
 */
public abstract class APIMisMenus extends AppCompatActivity {

    /**
     * Solicita la lista de menús del usuario
     *
     * @author edmsamuel
    */
    public void obtenerMisMenus() {
        APIRequest request = new APIRequest(){

            @Override
            public void KoobenRequestBeforeExecute() {
                headers.add( "KOOBEN SESSION ID", "" );
                misMenusBeforeLoad();
            }

            @Override
            public void KoobenRequestCompleted(JSONObject response) {
                misMenusRequestCompleted(response);
            }

            @Override
            public void KoobenRequestError(Exception error) {
                misMenusError(error);
            }
        };
        request.get( "/mis-menus" );
    }


    /**
     * Invocado al terminar la solicitud
     *
     * @param response JSONObject Respuesta recibida
     * @author edsamuel
     */
    private void misMenusRequestCompleted( JSONObject response ) {
        try {
            ArrayList<APIMisMenuModel> mis_menus;
            JSONObject status = response.getJSONObject( "status" );
            Boolean found = status.getBoolean( "found" );
            if ( !found ) {
                misMenusEmpty();
                return;
            }

            JSONObject item;
            JSONArray items = response.getJSONArray( "items" );
            int count = items.length();
            mis_menus = new ArrayList<APIMisMenuModel>();

            for ( int index = 0; index < count; index++ ) {
                item = items.getJSONObject( index );
                mis_menus.add( new APIMisMenuModel (
                        item.getInt( "id" ),
                        item.getString( "nombre" ),
                        item.getString( "portada" )
                ) );
            }

            misMenusRecibidos(mis_menus);
        } catch ( Exception error ) {
            misMenusError( error );
        }
    }

    /**
     * Invocado antes de enviar la solicitud
     *
     * @author edmsamuel
     */
    public abstract void misMenusBeforeLoad();

    /**
     * Invocado al recibir la lista de menús
     *
     * @param items ArrayList<APIMisMenuModel>
     * @author edmsamuel
     */
    public abstract void misMenusRecibidos( ArrayList<APIMisMenuModel> items );

    /**
     * Invocado al ocurrir un error en la obtencion de los menús
     *
     * @param error Exception
     * @author edmsamuel
     */
    public abstract void misMenusError( Exception error );

    /**
     * Invocado al recibir una respuesta del servidor pero la lista de menus es vacia
     *
     * @author edmsamuel
     */
    public abstract void misMenusEmpty();


    /**
     * Invocado antes de crear un menú
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemBeforeCreate();

    /**
     * Invocado si la creación de un menú ha sido exitosa
     *
     * @param menu APIMisMenuModel
     * @author edmsamuel
     */
    public abstract void misMenusItemCreated( APIMisMenuModel menu );

    /**
     * Invcado cuando ha ocurrido un error al crear el menú
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemCreatedError();


    /**
     * Invocado antes de enviar la solicitud de actualización de un menú
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemBeforeUpdate();

    /**
     * Invocado si el menú ha sido actualizado
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemUpdated();

    /**
     * Invocado si la actualización del menú no ha sido exitosa
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemUpdateError();


    /**
     * Invocado antes de enviar la solicitud para eliminar un menú
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemBeforeDelete();

    /**
     * Invocado si el menú ha sido eliminado
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemDeleted();


    /**
     * Invocado si ha ocurrido un error al eliminar el menú
     *
     * @author edmsamuel
     */
    public abstract void misMenusItemDeleteError();
}