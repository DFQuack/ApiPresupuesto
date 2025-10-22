package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.service.UsuarioService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // Request mínimo, solo campos serializables
        usuarioRequest = UsuarioRequest.builder()
                .username("usuario1")
                .password("123456")
                .build();

        // Response mínimo
        usuarioResponse = UsuarioResponse.builder()
                .username("usuario1")
                .build();
    }

    @Test
    void testFindAllUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuarioResponse));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("usuario1"));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testFindUsuarioById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testSaveUsuario() throws Exception {
        when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).save(any(UsuarioRequest.class));
    }

    @Test
    void testUpdateUsuario() throws Exception {
        when(usuarioService.update(eq(1L), any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario1"));

        verify(usuarioService, times(1)).update(eq(1L), any(UsuarioRequest.class));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteById(1L);
    }
}

