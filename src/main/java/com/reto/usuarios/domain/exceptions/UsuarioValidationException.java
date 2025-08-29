package com.reto.usuarios.domain.exceptions;

public class UsuarioValidationException extends RuntimeException {
    
    public UsuarioValidationException(String message) {
        super(message);
    }
    
    public static UsuarioValidationException correoInvalido() {
        return new UsuarioValidationException("Formato de correo inválido");
    }
    
    public static UsuarioValidationException celularInvalido() {
        return new UsuarioValidationException("Formato de celular inválido");
    }
    
    public static UsuarioValidationException documentoIdentidadInvalido() {
        return new UsuarioValidationException("Documento de identidad debe ser solo números");
    }
    
    public static UsuarioValidationException usuarioMenorDeEdad() {
        return new UsuarioValidationException("El usuario debe ser mayor de edad");
    }
    
    public static UsuarioValidationException correoDuplicado(String correo) {
        return new UsuarioValidationException("El correo " + correo + " ya está registrado");
    }
    
    public static UsuarioValidationException documentoIdentidadDuplicado(String documento) {
        return new UsuarioValidationException("El documento de identidad " + documento + " ya está registrado");
    }
}
