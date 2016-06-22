package com.ewansr.www.koobenapp;

/**
 * Created by EwanS on 03/06/2016.
 */
public class cRutasAPI {
    /** En caso de cambiar la ruta, dominio, ip, nombre de api etc
     * solo deberán cambiarse los siguientes dos valores para que sea mas modular
     * y evitar modificar una por una*/
    public static  String urlDominio = "https://www.inteli-code.com.mx/";

    public static String nombreAPI = "CocinaComparte/API/";
    public static String nombreAPI2 = "Kooben/API/";

    /** Extensión default para imagen*/
    public static String extDefault = ".png";
    public static String extjpg = ".jpg";

    /** Url general para carga de recursos para la App */
    public static String resources = "CocinaComparte/Resources/";
    public static String resources2 = "CocinaComparte/Resources/";
    public static String icons = "icons/";
    public static String icons2 = "Storage/";
    /** Archivo identificador de imagenes para el tipo de receta */
    public static String imgName = "receta_tipo_";
    public static String imgNameRecipes = "recipe_";

    public static String urlImgTipoReceta =  urlDominio + resources + icons;
    public static String urlImgRecetas =  urlDominio + resources2 + icons2 ;



    /** Recetas Paginadas
     * http://www.inteli-code.com.mx/CocinaComparte/API/recetas/paginar/0/50
     * donde 0 es desde donde inicia y el 5 es el total de elementos a cargar*/

    public static int vIdTipo = -1;
    public static int vDesde = 0;
    public static int vHasta = 50;

    public static String url_recetas_paginadas = urlDominio + nombreAPI + "recetas/tipo/"+vIdTipo+"/paginar/"+vDesde+"/"+vHasta;
    public static String UrlJSON_tiposReceta = urlDominio + nombreAPI + "recetas/tipos";
    public static int VisibleElements = 5;

}
