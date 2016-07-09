package com.ewansr.www.koobenapp;

import org.json.JSONObject;

/**
 * Created by Sam on 09/07/16.
 */
public class APIMisMenuItem extends APIMisMenuModel {
    public Boolean saved;

    /**
     * Metodo constructor
     *
     * @param id int Id del menú
     * @param nombre String Nombre del menú
     * @param portada String Url de la portada
     */
    public APIMisMenuItem(int id, String nombre, String portada) {
        super( id, nombre, portada );
        saved = ( id > -1 );
    }


    public void save() {
    }


    private void insertar() {
        APIRequest request = new APIRequest(){
            @Override
            public void KoobenRequestBeforeExecute() {
                menuItemBeforeCreate();
            }

            @Override
            public void KoobenRequestCompleted(JSONObject response) {
                try {
                    menuItemCreated( new APIMisMenuModel( -1, "", "" ) );
                } catch ( Exception error ) {
                    menuItemCreatedError( error );
                }
            }

            @Override
            public void KoobenRequestError(Exception error) {
                menuItemCreatedError( error );
            }
        };
    }



    public void menuItemBeforeCreate() {}
    public void menuItemCreated( APIMisMenuModel menu ) {}
    public void menuItemCreatedError( Exception error ) {}
}
