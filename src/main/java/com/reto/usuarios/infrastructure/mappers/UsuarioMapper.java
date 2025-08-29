package com.reto.usuarios.infrastructure.mappers;

import com.reto.usuarios.domain.model.Usuario;
import com.reto.usuarios.infrastructure.dto.UsuarioResponse;
import com.reto.usuarios.infrastructure.entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .documentoIdentidad(entity.getDocumentoIdentidad())
                .celular(entity.getCelular())
                .fechaNacimiento(entity.getFechaNacimiento())
                .correo(entity.getCorreo())
                .clave(entity.getClave())
                .rol(entity.getRol())
                .activo(entity.isActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioEntity.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .documentoIdentidad(usuario.getDocumentoIdentidad())
                .celular(usuario.getCelular())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .correo(usuario.getCorreo())
                .clave(usuario.getClave())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .documentoIdentidad(usuario.getDocumentoIdentidad())
                .celular(usuario.getCelular())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }
}
