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
    private ArrayList<APIMisMenuItem> misMenusItems;

    /**
     * Solicita la lista de menús del usuario
     *
    */
    public void obtenerMisMenus() {
        APIRequest request = new APIRequest(){
            @Override public void KoobenRequestBeforeExecute() {
                headers.add( "KOOBEN SESSION ID", "" );
                misMenusBeforeLoad();
            }

            @Override public void KoobenRequestCompleted(JSONObject response) {
                misMenusRequestCompleted(response);
            }

            @Override public void KoobenRequestError(Exception error) {
                misMenusLoadError(error);
            }
        };
        request.get( APIKoobenRoutes.misMenus );
    }


    /**
     * Invocado al terminar la solicitud
     *
     * @param response JSONObject Respuesta recibida
     */
    private void misMenusRequestCompleted( JSONObject response ) {
        try {
            ArrayList<APIMisMenuItem> mis_menus;
            JSONObject status = response.getJSONObject( "status" );
            Boolean found = status.getBoolean( "found" );
            if ( !found ) {
                misMenusLoad( new ArrayList<APIMisMenuItem>() );
                return;
            }

            JSONObject item;
            JSONArray items = response.getJSONArray( "items" );
            int count = items.length();
            mis_menus = new ArrayList<APIMisMenuItem>();

            for ( int index = 0; index < count; index++ ) {
                item = items.getJSONObject( index );
                mis_menus.add( new APIMisMenuItem (
                        item.getInt( "id" ),
                        item.getString( "nombre" ),
                        item.getString( "portada" )
                ) );
            }

            this.misMenusItems = mis_menus;
            misMenusLoad(mis_menus);
        } catch ( Exception error ) {
            misMenusLoadError( error );
        }
    }


    public void misMenusCreateNewItem( String nombre ) {
        APIMisMenuItem menu = new APIMisMenuItem( -1, nombre, "" ) {
            @Override public void menuItemBeforeCreate() {
                misMenusItemBeforeCreate();
            }
            @Override public void menuItemCreated(APIMisMenuModel menu) {
                misMenusItemCreated( menu );
            }
            @Override public void menuItemCreateError(KoobenException error) {
                misMenusItemCreateError( error );
            }
        };
        menu.save();
    }


    /**
     * Retorna un elemento de la lista de menús
     *
     * @param index int Indice en que se encuentre el elemento de la lista
     * @return APIMisMenuItem
     */
    public APIMisMenuItem misMenusItemForIndex( int index ) {
        return misMenusItems.get( index );
    }


    /**
     * Actualiza un elemento que se hubique en la lista de menús
     *
     * @param index int Posición en la lista
     */
    public void misMenusItemsSaveChangesForItemIndex( int index ) {
        APIMisMenuItem item_for_update = misMenusItems.get( index );

        APIMisMenuItem item = new APIMisMenuItem ( item_for_update.id, item_for_update.nombre, item_for_update.portada ) {
            @Override public void menuItemBeforeUpdate() {
                misMenusItemBeforeUpdate();
            }
            @Override public void menuItemUpdated( APIMisMenuModel menu ) {
                misMenusItemUpdated( menu );
            }
            @Override public void menuItemUpdateError( KoobenException error ) {
                misMenusItemUpdateError( error );
            }
        };
        item.save();
    }



    /**
     * Invoca la eliminación del elemento que se ubique en el indice especificado
     * @param index int Posición en la lista
     */
    public void misMenusDeleteItemForIndex( int index ) {
        APIMisMenuItem item = this.misMenusItems.get( index );
        APIMisMenuItem menu = new APIMisMenuItem( item.id, item.nombre, item.portada ) {
            @Override public void menuItemBeforeDelete() {
                misMenusItemBeforeDelete();
            }
            @Override public void menuItemDeleted(int id) {
                misMenusItemDeleted( id );
            }
            @Override public void menuItemDeleteError(KoobenException error) {
                misMenusItemDeleteError( error );
            }
        };
        menu.delete();
    }



    /**
     * Invocado antes de enviar la solicitud
     *
     */
    public abstract void misMenusBeforeLoad();

    /**
     * Invocado al recibir la lista de menús
     *
     * @param items ArrayList<APIMisMenuModel>
     */
    public abstract void misMenusLoad( ArrayList<APIMisMenuItem> items );

    /**
     * Invocado al ocurrir un error en la obtencion de los menús
     *
     * @param error Exception
     */
    public abstract void misMenusLoadError( Exception error );

    /**
     * Invocado antes de crear un menú
     *
     */
    public abstract void misMenusItemBeforeCreate();

    /**
     * Invocado si la creación de un menú ha sido exitosa
     *
     * @param menu APIMisMenuModel
     */
    public abstract void misMenusItemCreated( APIMisMenuModel menu );

    /**
     * Invcado cuando ha ocurrido un error al crear el menú
     *
     */
    public abstract void misMenusItemCreateError( KoobenException error );


    /**
     * Invocado antes de enviar la solicitud de actualización de un menú
     *
     */
    public abstract void misMenusItemBeforeUpdate();

    /**
     * Invocado si el menú ha sido actualizado
     *
     */
    public abstract void misMenusItemUpdated( APIMisMenuModel menu );

    /**
     * Invocado si la actualización del menú no ha sido exitosa
     *
     */
    public abstract void misMenusItemUpdateError( KoobenException error );


    /**
     * Invocado antes de enviar la solicitud para eliminar un menú
     *
     */
    public abstract void misMenusItemBeforeDelete();

    /**
     * Invocado si el menú ha sido eliminado
     *
     */
    public abstract void misMenusItemDeleted( int menuId );

    /**
     * Invocado si ha ocurrido un error al eliminar el menú
     *
     */
    public abstract void misMenusItemDeleteError( KoobenException error );
}