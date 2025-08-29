package com.reto.usuarios.domain.services;

import com.reto.usuarios.domain.exceptions.UsuarioNotFoundException;
import com.reto.usuarios.domain.exceptions.UsuarioValidationException;
import com.reto.usuarios.domain.model.Usuario;
import com.reto.usuarios.domain.ports.UsuarioRepository;
import com.reto.usuarios.domain.ports.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsuarioDomainService implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Validaciones de negocio
        validarDatosUsuario(usuario);
        
        // Encriptar contraseña
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        
        // Establecer valores por defecto
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDate.now());
        usuario.setFechaActualizacion(LocalDate.now());
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        Usuario usuarioExistente = obtenerUsuarioPorId(usuario.getId());
        
        // Validar que el correo no esté duplicado si cambió
        if (!usuarioExistente.getCorreo().equals(usuario.getCorreo()) &&
            usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw UsuarioValidationException.correoDuplicado(usuario.getCorreo());
        }
        
        // Preservar campos que no deben cambiar
        usuario.setFechaCreacion(usuarioExistente.getFechaCreacion());
        usuario.setFechaActualizacion(LocalDate.now());
        usuario.setActivo(usuarioExistente.isActivo());
        usuario.setClave(usuarioExistente.getClave()); // Preservar contraseña
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setClave(passwordEncoder.encode(nuevaPassword));
        usuario.setFechaActualizacion(LocalDate.now());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean validarEdad(LocalDate fechaNacimiento) {
        Period edad = Period.between(fechaNacimiento, LocalDate.now());
        return edad.getYears() >= 18;
    }

    @Override
    public boolean validarCorreo(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(correo).matches();
    }

    @Override
    public boolean validarCelular(String celular) {
        // Máximo 13 caracteres, puede contener +
        return celular != null && celular.length() <= 13 && 
               celular.matches("^\\+?[0-9]+$");
    }

    @Override
    public boolean validarDocumentoIdentidad(String documentoIdentidad) {
        // Solo números
        return documentoIdentidad != null && documentoIdentidad.matches("^[0-9]+$");
    }

    private void validarDatosUsuario(Usuario usuario) {
        if (!validarCorreo(usuario.getCorreo())) {
            throw UsuarioValidationException.correoInvalido();
        }
        
        if (!validarCelular(usuario.getCelular())) {
            throw UsuarioValidationException.celularInvalido();
        }
        
        if (!validarDocumentoIdentidad(usuario.getDocumentoIdentidad())) {
            throw UsuarioValidationException.documentoIdentidadInvalido();
        }
        
        if (!validarEdad(usuario.getFechaNacimiento())) {
            throw UsuarioValidationException.usuarioMenorDeEdad();
        }
        
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw UsuarioValidationException.correoDuplicado(usuario.getCorreo());
        }
        
        if (usuarioRepository.existsByDocumentoIdentidad(usuario.getDocumentoIdentidad())) {
            throw UsuarioValidationException.documentoIdentidadDuplicado(usuario.getDocumentoIdentidad());
        }
    }
}
