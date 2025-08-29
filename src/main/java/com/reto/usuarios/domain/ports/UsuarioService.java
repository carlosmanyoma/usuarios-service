package com.reto.usuarios.domain.ports;

import com.reto.usuarios.domain.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    
    Usuario crearUsuario(Usuario usuario);
    
    Usuario obtenerUsuarioPorId(Long id);
    
    Optional<Usuario> obtenerUsuarioPorCorreo(String correo);
    
    List<Usuario> findAll();
    
    Usuario actualizarUsuario(Usuario usuario);
    
    void eliminarUsuario(Long id);
    
    boolean validarEdad(LocalDate fechaNacimiento);
    
    boolean validarCorreo(String correo);
    
    boolean validarCelular(String celular);
    
    boolean validarDocumentoIdentidad(String documentoIdentidad);
    
    Usuario cambiarPassword(Long id, String nuevaPassword);
}
