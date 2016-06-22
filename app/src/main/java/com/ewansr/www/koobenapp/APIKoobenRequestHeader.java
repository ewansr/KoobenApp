package com.ewansr.www.koobenapp;

/**
 * Representa un item en los headers de una petición HTTP.
 *
 * @author edmsamuel 22/06/16.
 */
public class APIKoobenRequestHeader {

    public String name;
    public String value;

    public APIKoobenRequestHeader( String name, String value ) {
        this.name = name.replaceAll( "\\s+", "-" );
        this.value = value;
    }
}
