package com.reto.usuarios.domain.services;

import com.reto.usuarios.domain.model.Rol;
import com.reto.usuarios.domain.model.Usuario;
import com.reto.usuarios.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDomainServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioDomainService usuarioService;

    private Usuario usuarioValido;
    private LocalDate fechaNacimientoValida;

    @BeforeEach
    void setUp() {
        fechaNacimientoValida = LocalDate.now().minusYears(25);
        usuarioValido = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .documentoIdentidad("12345678")
                .celular("+573001234567")
                .fechaNacimiento(fechaNacimientoValida)
                .correo("juan.perez@email.com")
                .clave("password123")
                .rol(Rol.PROPIETARIO)
                .build();


    }

    @Test
    void crearUsuario_ConDatosValidos_DebeCrearUsuarioExitosamente() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(usuarioRepository.existsByDocumentoIdentidad(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L); // Simular ID generado
            return usuario;
        });

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        assertEquals(Rol.PROPIETARIO, resultado.getRol());
        assertTrue(resultado.isActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertNotNull(resultado.getFechaActualizacion());

        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConCorreoInvalido_DebeLanzarExcepcion() {
        // Arrange
        usuarioValido.setCorreo("correo-invalido");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConCelularInvalido_DebeLanzarExcepcion() {
        // Arrange
        usuarioValido.setCelular("celular-muy-largo-que-excede-el-limite");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConDocumentoIdentidadInvalido_DebeLanzarExcepcion() {
        // Arrange
        usuarioValido.setDocumentoIdentidad("123ABC");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConUsuarioMenorDeEdad_DebeLanzarExcepcion() {
        // Arrange
        usuarioValido.setFechaNacimiento(LocalDate.now().minusYears(17));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConCorreoDuplicado_DebeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByCorreo("juan.perez@email.com")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_ConDocumentoIdentidadDuplicado_DebeLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByDocumentoIdentidad("12345678")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuarioValido));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void validarEdad_ConUsuarioMayorDeEdad_DebeRetornarTrue() {
        // Act
        boolean resultado = usuarioService.validarEdad(fechaNacimientoValida);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void validarEdad_ConUsuarioMenorDeEdad_DebeRetornarFalse() {
        // Arrange
        LocalDate fechaNacimientoMenor = LocalDate.now().minusYears(17);

        // Act
        boolean resultado = usuarioService.validarEdad(fechaNacimientoMenor);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void validarCorreo_ConCorreoValido_DebeRetornarTrue() {
        // Act
        boolean resultado = usuarioService.validarCorreo("test@email.com");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void validarCorreo_ConCorreoInvalido_DebeRetornarFalse() {
        // Act
        boolean resultado = usuarioService.validarCorreo("correo-invalido");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void validarCelular_ConCelularValido_DebeRetornarTrue() {
        // Act
        boolean resultado = usuarioService.validarCelular("+573001234567");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void validarCelular_ConCelularInvalido_DebeRetornarFalse() {
        // Act
        boolean resultado = usuarioService.validarCelular("celular-muy-largo");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void validarDocumentoIdentidad_ConDocumentoValido_DebeRetornarTrue() {
        // Act
        boolean resultado = usuarioService.validarDocumentoIdentidad("12345678");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void validarDocumentoIdentidad_ConDocumentoInvalido_DebeRetornarFalse() {
        // Act
        boolean resultado = usuarioService.validarDocumentoIdentidad("123ABC");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void cambiarPassword_ConIdValido_DebeCambiarPasswordExitosamente() {
        // Arrange
        Long idUsuario = 1L;
        String nuevaPassword = "nuevaPassword123";
        Usuario usuarioExistente = Usuario.builder()
                .id(idUsuario)
                .nombre("Juan")
                .apellido("Pérez")
                .clave("passwordAnterior")
                .build();
        
        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioExistente));
        when(passwordEncoder.encode(nuevaPassword)).thenReturn("encodedNewPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = usuarioService.cambiarPassword(idUsuario, nuevaPassword);

        // Assert
        assertNotNull(resultado);
        verify(passwordEncoder).encode(nuevaPassword);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_ConDatosValidos_DebeActualizarUsuarioExitosamente() {
        // Arrange
        Long idUsuario = 1L;
        Usuario usuarioExistente = Usuario.builder()
                .id(idUsuario)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@email.com")
                .fechaCreacion(LocalDate.now().minusDays(1))
                .activo(true)
                .clave("password123")
                .build();
        
        Usuario usuarioActualizado = Usuario.builder()
                .id(idUsuario)
                .nombre("Juan Carlos")
                .apellido("Pérez")
                .correo("juan.carlos@email.com") // Cambio el correo para que se ejecute la validación
                .documentoIdentidad("12345678")
                .celular("+573001234567")
                .fechaNacimiento(fechaNacimientoValida)
                .rol(Rol.PROPIETARIO)
                .build();

        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioExistente));
        when(usuarioRepository.existsByCorreo("juan.carlos@email.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        Usuario resultado = usuarioService.actualizarUsuario(usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("juan.carlos@email.com", resultado.getCorreo());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_ConCorreoNoCambiado_DebeActualizarSinValidarDuplicado() {
        // Arrange
        Long idUsuario = 1L;
        Usuario usuarioExistente = Usuario.builder()
                .id(idUsuario)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@email.com")
                .fechaCreacion(LocalDate.now().minusDays(1))
                .activo(true)
                .clave("password123")
                .build();
        
        Usuario usuarioActualizado = Usuario.builder()
                .id(idUsuario)
                .nombre("Juan Carlos")
                .apellido("Pérez")
                .correo("juan.perez@email.com") // Mismo correo, no se ejecuta validación
                .documentoIdentidad("12345678")
                .celular("+573001234567")
                .fechaNacimiento(fechaNacimientoValida)
                .rol(Rol.PROPIETARIO)
                .build();

        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        Usuario resultado = usuarioService.actualizarUsuario(usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("juan.perez@email.com", resultado.getCorreo());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioRepository, never()).existsByCorreo(anyString()); // No se debe llamar
    }

    @Test
    void findAll_DebeRetornarListaDeUsuarios() {
        // Arrange
        List<Usuario> usuarios = List.of(usuarioValido);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(usuarioValido, resultado.get(0));
        verify(usuarioRepository).findAll();
    }
}
