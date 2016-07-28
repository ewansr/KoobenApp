package com.ewansr.www.koobenapp;

import org.json.JSONObject;

/**
 * Menú de usuario y operaciones
 *
 * @author edmsamuel
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
    public APIMisMenuItem( int id, String nombre, String portada ) {
        super( id, nombre, portada );
        saved = ( id > -1 );
    }


    /**
     * Envia al la API la solicitud para crear el menú
     *
     */
    private void insert() {
        try {
            APIRequest request = new APIRequest(){
                @Override public void KoobenRequestBeforeExecute() {
                    headers.clear();
                    headers.add( "KOOBEN SESSION ID", "" );
                    menuItemBeforeCreate();
                }

                @Override public void KoobenRequestCompleted(JSONObject response) {
                    try {
                        KoobenRequestStatusPOST status = new KoobenRequestStatusPOST( response.getJSONObject( "status" ) );
                        if ( !status.created ) {
                            if ( !status.valid ) {
                                throw new KoobenException( KoobenExceptionCode.INVALID_VALUES, "Valores invalidos" );
                            } else {
                                throw new KoobenException( KoobenExceptionCode.ITEM_NOT_CREATED, "Menú no creado" );
                            }
                        }

                        menuItemCreated(new APIMisMenuModel(
                                response.getInt( "id" ),
                                response.getString( "nombre" ),
                                response.getString( "portada" )
                        ) );
                    } catch ( KoobenException error ) {
                        menuItemCreateError( error );
                    } catch ( Exception error ) {
                        menuItemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                    }
                }

                @Override public void KoobenRequestError(Exception error) {
                    menuItemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            };

            JSONObject menu = new JSONObject();
            menu.put( "nombre", this.nombre );
            menu.put( "portada", this.portada );
            request.post( APIKoobenRoutes.misMenus, menu );
        } catch ( Exception error ) {
            menuItemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
        }
    }


    /**
     * Envia una solicitud para actualizar los datos del menú
     *
     */
    private void update() {
        try {

            APIRequest request = new APIRequest() {
                @Override public void KoobenRequestBeforeExecute() {
                    menuItemBeforeUpdate();
                }

                @Override public void KoobenRequestCompleted( JSONObject response ) {
                    try {
                        KoobenRequestStatusPUT status = new KoobenRequestStatusPUT( response.getJSONObject( "status" ) );

                        if ( !status.updated ) {
                            throw new KoobenException( KoobenExceptionCode.ITEM_NOT_UPDATED, "Menú " );
                        }

                        menuItemUpdated( new APIMisMenuModel(
                                response.getInt( "id" ),
                                response.getString( "nombre" ),
                                response.getString( "portada" )
                        ) );

                    } catch ( Exception error ) {
                        menuItemUpdateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                    }
                }

                @Override public void KoobenRequestError( Exception error ) {
                    menuItemUpdateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            };

            JSONObject menu = new JSONObject();
            menu.put( "nombre", this.nombre );
            menu.put( "portada", this.portada );
            request.put( APIKoobenRoutes.miMenu( this.id ), menu );

        } catch ( Exception error ) {
            menuItemUpdateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
        }
    }



    /**
     * Envía la solicitud para eliminar el menú
     *
     */
    public void delete() {
        try {

            APIRequest request = new APIRequest(){
                @Override public void KoobenRequestBeforeExecute() {
                    menuItemBeforeDelete();
                }

                @Override public void KoobenRequestCompleted(JSONObject response) {
                    try {

                        KoobenRequestStatusDELETE status = new KoobenRequestStatusDELETE( response.getJSONObject( "status" ) );

                        if ( status.deleted ) {
                            menuItemDeleted( response.getInt( "id" ) );
                        } else {
                            menuItemDeleteError( new KoobenException( KoobenExceptionCode.ITEM_NOT_DELETED, "El menú no ha podido ser eliminado" ) );
                        }

                    } catch ( Exception error ) {
                        menuItemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                    }
                }

                @Override public void KoobenRequestError(Exception error) {
                    menuItemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            };

            request.delete( APIKoobenRoutes.miMenu( this.id ) );

        } catch ( Exception error ) {
            menuItemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
        }
    }



    /**
     * En caso que el menú no haya sido creado lo insertará
     * en caso contrario actualizará los nuevos valores
     *
     */
    public void save() {
        if ( saved ) {
            update();
        } else {
            insert();
        }
    }



    /**
     * Es invocado antes de enviar la solicitud de creación
     *
     */
    public void menuItemBeforeCreate() {}

    /**
     * Es invocado si la creación fue exitosa
     *
     */
    public void menuItemCreated( APIMisMenuModel menu ) {}

    /**
     * Es invocado si hay un error en la creación
     *
     */
    public void menuItemCreateError(KoobenException error ) {}

    /**
     * Es invocado antes de enviar la solicitud de actualización
     *
     */
    public void menuItemBeforeUpdate() {}

    /**
     * Es invocado cuando la actualización es exitosa
     *
     */
    public void menuItemUpdated( APIMisMenuModel menu ) {}

    /**
     * Es invocado cuando hay un error en la actualización
     *
     */
    public void menuItemUpdateError( KoobenException error ) {}

    /**
     * Es invocado antes de enviar la solicitud de eliminación del menú
     *
     */
    public void menuItemBeforeDelete() {}

    /**
     * Es invocado cuando el menú ha sido eliminado
     *
     */
    public void menuItemDeleted( int id ) {}

    /**
     * Es invocado si hay un error al eliminar el menú
     *
     */
    public void menuItemDeleteError( KoobenException error ) {}
}
