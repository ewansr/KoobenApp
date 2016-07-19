package com.ewansr.www.koobenapp;

/**
 * Clase de rutas
 *
 * @author edmsamuel
 */
public class APIKoobenRoutes {
    public static String misMenus = "/mis-menus";
    public static String miMenuItem = "/mi-menu/";

    public static String miMenu( int id ) {
        return ( miMenuItem + id );
    }
}
