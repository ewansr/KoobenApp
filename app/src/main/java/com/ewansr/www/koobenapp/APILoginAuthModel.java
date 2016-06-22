package com.ewansr.www.koobenapp;

/**
 * Estructurá recibida por parte del servidor en caso de haber una operación exitosa
 * en la autenticación de un usuario.
 *
 * @author edmsamuel 21/06/16.
 */
public class APILoginAuthModel {

    public String username;
    public String nombre;
    public String apellidos;
    public String imagenurl;
    public String session;

}