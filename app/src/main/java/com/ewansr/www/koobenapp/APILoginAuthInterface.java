package com.ewansr.www.koobenapp;

/**
 * Metodos para los resutlados de la autenticación de un usuario.
 *
 * @author edmsamuel 21/06/16.
 */
public interface APILoginAuthInterface {

    /**
     * Debe ser llamado en caso que la autenticación sea exitosa.
     *
     * @param usuario APIModelLoginAuth Resultado de la autenticación
     */
    public void autenticacionExitosa( APILoginAuthModel usuario );


    /**
     * Debe ser llamado en caso que la autenticación sea incorrecta.
     *
     */
    public void autenticacionFallida();
}
