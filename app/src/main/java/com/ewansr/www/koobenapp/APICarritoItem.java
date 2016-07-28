package com.ewansr.www.koobenapp;

import org.json.JSONObject;

/**
 * Operaciones con item del carrito de compra
 *
 * @author edmsamuel
 */
public class APICarritoItem extends APICarritoItemModel {
    private Boolean saved;

    public APICarritoItem( int id, int productoId, String productoNombre ) {
        super( id, productoId, productoNombre );
        saved = ( id > -1 );
    }


    public APICarritoItem( APICarritoItemModel item ) {
        super( item.id, item.productoId, item.productoNombre );
        saved = ( item.id > -1 );
    }

    /**
     * Guarda el item en la lista de carro de compra del usuario
     *
     */
    public void save() {
        try {
            APIRequest request = new APIRequest() {
                @Override public void KoobenRequestBeforeExecute() {
                    itemBeforeCreate();
                }

                @Override public void KoobenRequestCompleted(JSONObject response) {
                    try {
                        KoobenRequestStatusPOST status = new KoobenRequestStatusPOST( response.getJSONObject( "status" ) );

                        if ( !status.created ) {
                            throw new KoobenException( KoobenExceptionCode.ITEM_NOT_CREATED, "El producto no pudo ser agregado al carro de compra" );
                        }

                        itemCreated( new APICarritoItemModel(
                                response.getInt( "id" ),
                                response.getInt( "productoId" ),
                                response.getString( "productoNombre" )
                        ) );

                    } catch ( KoobenException error ) {
                        itemCreateError( error );
                    } catch ( Exception error ) {
                        itemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                    }
                }

                @Override public void KoobenRequestError(Exception error) {
                    itemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            };

            JSONObject producto = new JSONObject();
            producto.put( "productoId", productoId );
            request.post( "/carrito/items", producto );

        } catch ( Exception error ) {
            itemCreateError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
        }
    }


    /**
     * Elimina el producto del carrito de compra
     *
     */
    public void delete() {
        APIRequest request = new APIRequest(){
            @Override public void KoobenRequestBeforeExecute() {
                itemBeforeDelete();
            }

            @Override public void KoobenRequestCompleted(JSONObject response) {
                try {
                    KoobenRequestStatusDELETE status = new KoobenRequestStatusDELETE(response.getJSONObject("status"));
                    if (!status.deleted) {
                        throw new KoobenException(KoobenExceptionCode.ITEM_NOT_DELETED, "El producto no pudo ser removido del carro de compra");
                    }

                    itemDeleted( id );
                } catch ( KoobenException error ) {
                    itemDeleteError( error );
                } catch ( Exception error ) {
                    itemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
                }
            }

            @Override public void KoobenRequestError(Exception error) {
                itemDeleteError( new KoobenException( KoobenExceptionCode.UNKNOWN, error.getMessage() ) );
            }
        };
        request.delete( "/carrito/items/" + id );
    }



    public void itemBeforeCreate() {}
    public void itemCreated(APICarritoItemModel item ) {}
    public void itemCreateError(KoobenException error ) {}

    public void itemBeforeDelete() {}
    public void itemDeleted(int id ) {}
    public void itemDeleteError(KoobenException error ) {}
}
