package com.tallerwebi.dominio.excepcion;

public class FormatoImagenInvalidaException extends RuntimeException {
    public FormatoImagenInvalidaException(String mensaje) {
        super(mensaje);
    }
}
