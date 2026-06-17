package com.tallerwebi.dominio.excepcion;

public class ImagenExcedeTamanoException extends RuntimeException {
    public ImagenExcedeTamanoException(String mensaje) {
        super(mensaje);
    }
}
