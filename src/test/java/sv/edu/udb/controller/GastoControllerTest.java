package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.GastoService;
import sv.edu.udb.service.mapper.GastoMapper;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GastoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GastoService gastoService;

    @Mock
    private GastoMapper gastoMapper;

    @InjectMocks
    private GastoController gastoController;

    private ObjectMapper objectMapper;

    private GastoRequest gastoRequest;
    private GastoResponse gastoResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gastoController).build();
        objectMapper = new ObjectMapper();

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        gastoRequest = GastoRequest.builder()
                .mes(Month.JANUARY)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuario(usuario)
                .build();

        gastoResponse = GastoResponse.builder()
                .id(1L)
                .mes(Month.JANUARY)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuario(usuario)
                .build();
    }

    @Test
    void testFindAllGastosByUsuario() throws Exception {
        when(gastoService.findAllByUsuario(1L)).thenReturn(List.of(gastoResponse));

        mockMvc.perform(get("/gastos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].mes").value("JANUARY"));

        verify(gastoService, times(1)).findAllByUsuario(1L);
    }

    @Test
    void testFindGastoById() throws Exception {
        when(gastoService.findById(1L)).thenReturn(gastoResponse);

        mockMvc.perform(get("/gastos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mes").value("JANUARY"));

        verify(gastoService, times(1)).findById(1L);
    }

    @Test
    void testSaveGasto() throws Exception {
        when(gastoService.save(any(GastoRequest.class))).thenReturn(gastoResponse);

        mockMvc.perform(post("/gastos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gastoRequest)))
                .andExpect(status().isOk()) // Cambiado de isCreated() a isOk()
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mes").value("JANUARY"));

        verify(gastoService, times(1)).save(any(GastoRequest.class));
    }

    @Test
    void testUpdateGasto() throws Exception {
        when(gastoService.update(eq(1L), any(GastoRequest.class))).thenReturn(gastoResponse);

        mockMvc.perform(put("/gastos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gastoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mes").value("JANUARY"));

        verify(gastoService, times(1)).update(eq(1L), any(GastoRequest.class));
    }

    @Test
    void testDeleteGasto() throws Exception {
        doNothing().when(gastoService).deleteById(1L);

        mockMvc.perform(delete("/gastos/1"))
                .andExpect(status().isNoContent());

        verify(gastoService, times(1)).deleteById(1L);
    }
}
