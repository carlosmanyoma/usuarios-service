package com.reto.usuarios.infrastructure.controllers;

import com.reto.usuarios.domain.model.Usuario;
import com.reto.usuarios.domain.ports.UsuarioService;
import com.reto.usuarios.infrastructure.dto.ActualizarUsuarioRequest;
import com.reto.usuarios.infrastructure.dto.CambiarPasswordRequest;
import com.reto.usuarios.infrastructure.dto.CrearUsuarioRequest;
import com.reto.usuarios.infrastructure.dto.UsuarioResponse;
import com.reto.usuarios.infrastructure.mappers.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema con validaciones de negocio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    })
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @Valid @RequestBody CrearUsuarioRequest request) {
        
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .documentoIdentidad(request.getDocumentoIdentidad())
                .celular(request.getCelular())
                .fechaNacimiento(request.getFechaNacimiento())
                .correo(request.getCorreo())
                .clave(request.getClave())
                .rol(request.getRol())
                .build();

        Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
        UsuarioResponse response = usuarioMapper.toResponse(usuarioCreado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Recupera un usuario específico por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        UsuarioResponse response = usuarioMapper.toResponse(usuario);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Recupera la lista de todos los usuarios del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente")
    })
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioResponse> response = usuarios.stream()
                .map(usuarioMapper::toResponse)
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        
        Usuario usuario = Usuario.builder()
                .id(id)
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .documentoIdentidad(request.getDocumentoIdentidad())
                .celular(request.getCelular())
                .fechaNacimiento(request.getFechaNacimiento())
                .correo(request.getCorreo())
                .rol(request.getRol())
                .build();

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);
        UsuarioResponse response = usuarioMapper.toResponse(usuarioActualizado);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cambiar-password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<UsuarioResponse> cambiarPassword(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos para cambiar contraseña", required = true)
            @Valid @RequestBody CambiarPasswordRequest request) {
        
        Usuario usuarioActualizado = usuarioService.cambiarPassword(id, request.getNuevaPassword());
        UsuarioResponse response = usuarioMapper.toResponse(usuarioActualizado);
        
        return ResponseEntity.ok(response);
    }
}
