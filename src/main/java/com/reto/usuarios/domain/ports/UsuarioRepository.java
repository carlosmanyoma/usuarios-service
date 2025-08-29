package com.reto.usuarios.domain.ports;

import com.reto.usuarios.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByDocumentoIdentidad(String documentoIdentidad);
    List<Usuario> findAll();
    void deleteById(Long id);
    boolean existsByCorreo(String correo);
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
    boolean existsById(Long id);
}
