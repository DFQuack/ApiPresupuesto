package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.mapper.UsuarioMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        // Objeto Usuario de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("password123");

        // Objeto UsuarioRequest de prueba
        usuarioRequest = UsuarioRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        // Objeto UsuarioResponse de prueba
        usuarioResponse = UsuarioResponse.builder()
                .username("testuser")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void findAll_shouldReturnAllUsers() {
        when(usuarioRepo.findAll()).thenReturn(Collections.singletonList(usuario));
        when(usuarioMapper.toUsuarioResponseList(anyList())).thenReturn(Collections.singletonList(usuarioResponse));

        List<UsuarioResponse> result = usuarioService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(usuarioRepo).findAll();
        verify(usuarioMapper).toUsuarioResponseList(Collections.singletonList(usuario));
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void findById_shouldFindUserById() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(usuarioRepo).findById(1L);
        verify(usuarioMapper).toUsuarioResponse(usuario);
    }

    @Test
    @DisplayName("Debería lanzar excepción si no encuentra usuario por ID")
    void findById_shouldThrowExceptionWhenNotFound() {
        when(usuarioRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.findById(2L));
        verify(usuarioRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería guardar un nuevo usuario")
    void save_shouldSaveNewUser() {
        when(usuarioMapper.toUsuario(usuarioRequest)).thenReturn(usuario);
        when(usuarioRepo.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = usuarioService.save(usuarioRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(usuarioMapper).toUsuario(usuarioRequest);
        verify(usuarioRepo).save(usuario);
        verify(usuarioMapper).toUsuarioResponse(usuario);
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente")
    void update_shouldUpdateExistingUser() {
        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);

        UsuarioResponse result = usuarioService.update(1L, usuarioRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(usuarioRepo).findById(1L);
        verify(usuarioRepo).save(usuario);
        verify(usuarioMapper).toUsuarioResponse(usuario);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un usuario no existente")
    void update_shouldThrowExceptionWhenNotFound() {
        when(usuarioRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.update(2L, usuarioRequest));
        verify(usuarioRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería eliminar un usuario por ID")
    void deleteById_shouldDeleteUserById() {
        doNothing().when(usuarioRepo).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(usuarioRepo).deleteById(1L);
    }
}