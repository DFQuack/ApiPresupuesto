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
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.service.GastoService;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GastoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GastoService gastoService;

    private GastoRequest gastoRequest;
    private GastoResponse gastoResponse;

    @BeforeEach
    void setUp() {
        gastoRequest = GastoRequest.builder()
                .mes(Month.JANUARY)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)
                .build();

        gastoResponse = GastoResponse.builder()
                .id(1L)
                .mes(Month.JANUARY)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)
                .build();
    }

    @Test
    void testFindAllGastosByUsuario() throws Exception {
        when(gastoService.findAllByUsuario(1L)).thenReturn(Arrays.asList(gastoResponse));

        mockMvc.perform(get("/gastos/usuario/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].mes").value("JANUARY"));

        verify(gastoService, times(1)).findAllByUsuario(1L);
    }

    @Test
    void testFindGastoById() throws Exception {
        when(gastoService.findById(1L)).thenReturn(gastoResponse);

        mockMvc.perform(get("/gastos/1")
                        .accept(MediaType.APPLICATION_JSON))
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
                .andExpect(status().isCreated())
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