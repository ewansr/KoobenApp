package com.ewansr.www.koobenapp;

import java.util.ArrayList;

/**
 * @author edmsamuel 22/06/16.
 */
public class APIKoobenRequestHeaders {

    public ArrayList<APIKoobenRequestHeader> items;

    /**
     * Constructor
     *
     */
    public APIKoobenRequestHeaders() {
        items = new ArrayList<APIKoobenRequestHeader>();
    }


    /**
     * Añadir un nuevo elemento al la lista de headers.
     *
     * @param name String Nombre del header.
     * @param value String valor del header.
     * @return
     */
    public APIKoobenRequestHeader add( String name, String value ) {
        APIKoobenRequestHeader item = new APIKoobenRequestHeader( name, value );
        items.add( item );
        return item;
    }


    /**
     * Limpia la lista de headers.
     *
     */
    public void clear() {
        items.clear();
    }


    /**
     * Retorna el elemento que se encuentre en el indice especificado.
     *
     * @param index int Indice del elemento.
     * @return APIKoobenRequestHeader
     */
    public APIKoobenRequestHeader get( int index ) {
        return items.get( index );
    }


    /**
     * Busca en la lista con base al nombre y retorna el valor del header.
     *
     * @param name String nombre del header.
     * @return String
     */
    public String get( String name ) {
        String value = "";
        APIKoobenRequestHeader item;

        for ( int index = 0; index < items.size(); index++ ) {
            item = items.get( index );
            if ( item.name == name ) {
                value = item.value; break;
            }
        }

        return value;
    }

}
