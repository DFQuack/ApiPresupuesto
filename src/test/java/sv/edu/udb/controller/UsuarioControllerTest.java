package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.service.UsuarioService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        usuarioRequest = UsuarioRequest.builder()
                .username("usuario1")
                .password("123456")
                .build();

        usuarioResponse = UsuarioResponse.builder()
                .username("usuario1")
                .password("123456")
                .build();
    }

    @Test
    void testFindAllUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.singletonList(usuarioResponse));

        mockMvc.perform(get("/api/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("usuario1"));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testFindUsuarioById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/api/usuarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testSaveUsuario() throws Exception {
        when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).save(any(UsuarioRequest.class));
    }

    @Test
    void testUpdateUsuario() throws Exception {
        when(usuarioService.update(eq(1L), any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).update(eq(1L), any(UsuarioRequest.class));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteById(1L);
    }
}