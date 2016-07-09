package com.ewansr.www.koobenapp;

/**
 * Created by Sam on 09/07/16.
 */
public class KoobenException extends Exception {

    public KoobenExceptionCode code;
    public String message;

    public KoobenException( KoobenExceptionCode code, String message ) {
        super( message );
        this.code = code;
    }
}
