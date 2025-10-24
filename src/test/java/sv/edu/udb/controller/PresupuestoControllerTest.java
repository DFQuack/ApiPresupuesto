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
import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.PresupuestoService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PresupuestoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PresupuestoService presService;

    @InjectMocks
    private PresupuestoController presController;

    private ObjectMapper objectMapper;

    private PresupuestoRequest presRequest;
    private PresupuestoResponse presResponse;

    final Usuario usuario = new Usuario();
    final Ingreso ingreso = new Ingreso();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(presController).build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        usuario.setId(1L);
        ingreso.setId(1L);

        // CORREGIDO: Usar objeto Usuario completo según PresupuestoRequest
        presRequest = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuario(usuario)  // Cambiado de usuarioId a usuario (objeto completo)
                .build();

        // CORREGIDO: Mantener estructura original según PresupuestoResponse
        presResponse = PresupuestoResponse.builder()
                .id(1L)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuario(usuario)
                .ingresos(List.of(ingreso))
                .build();
    }

    @Test
    void testFindPresByUsuario() throws Exception {
        when(presService.findByUsuario(1L)).thenReturn(presResponse);

        mockMvc.perform(get("/api/presupuestos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuario.id").value(1)) // Verificar usuario completo
                .andExpect(jsonPath("$.ingresos[0].id").value(1)); // Verificar ingresos

        verify(presService, times(1)).findByUsuario(1L);
    }

    @Test
    void testFindPresById() throws Exception {
        when(presService.findById(1L)).thenReturn(presResponse);

        mockMvc.perform(get("/api/presupuestos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.ingresos[0].id").value(1));

        verify(presService, times(1)).findById(1L);
    }

    @Test
    void testSavePresupuesto() throws Exception {
        when(presService.save(any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(post("/api/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.ingresos[0].id").value(1));

        verify(presService, times(1)).save(any(PresupuestoRequest.class));
    }

    @Test
    void testUpdatePresupuesto() throws Exception {
        when(presService.update(eq(1L), any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(put("/api/presupuestos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.ingresos[0].id").value(1));

        verify(presService, times(1)).update(eq(1L), any(PresupuestoRequest.class));
    }

    @Test
    void testDeletePresupuesto() throws Exception {
        doNothing().when(presService).deleteById(1L);

        mockMvc.perform(delete("/api/presupuestos/1"))
                .andExpect(status().isNoContent());

        verify(presService, times(1)).deleteById(1L);
    }

    @Test
    void testSavePresupuestoWithInvalidData() throws Exception {
        // Test con datos inválidos
        PresupuestoRequest invalidRequest = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(-100))  // Violates @Positive
                .deudas(BigDecimal.valueOf(-50))  // Violates @PositiveOrZero
                .usuario(null)  // Violates @NotNull
                .build();

        mockMvc.perform(post("/api/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}