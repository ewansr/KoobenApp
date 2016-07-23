package com.ewansr.www.koobenapp;

/**
 * Recursos remotos
 *
 * @author edmsamuel
 */
public class KoobenResources {
    public static String storage_url = "https://www.inteli-code.com.mx/CocinaComparte/Resources/Storage/";

    public static String makeUrl( String file ) {
        return ( storage_url + file );
    }
}
