package com.tallerwebi.dominio.excepcion;

public class PasswordInvalidaException extends RuntimeException {

  // Esta línea es la que PMD exige sí o sí
  private static final long serialVersionUID = 1L;

  public PasswordInvalidaException(String mensaje) {
    super(mensaje);
  }
}
