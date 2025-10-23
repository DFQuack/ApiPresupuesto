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
import sv.edu.udb.service.PresupuestoService;
import sv.edu.udb.service.mapper.PresupuestoMapper;

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

    @Mock
    private PresupuestoMapper presMapper;

    @InjectMocks
    private PresupuestoController presController;

    private ObjectMapper objectMapper;

    private PresupuestoRequest presRequest;
    private PresupuestoResponse presResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(presController).build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // ACTUALIZADO: Usar IDs en lugar de objetos completos
        presRequest = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)  // Cambiado de usuario a usuarioId
                .ingresoIds(List.of(1L, 2L))  // Cambiado de ingresos a ingresoIds
                .build();

        // ACTUALIZADO: Usar IDs en lugar de objetos completos
        presResponse = PresupuestoResponse.builder()
                .id(1L)  // Agregado id
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)  // Cambiado de usuario a usuarioId
                .ingresoIds(List.of(1L, 2L))  // Cambiado de ingresos a ingresoIds
                .build();
    }

    @Test
    void testFindPresByUsuario() throws Exception {
        when(presService.findByUsuario(1L)).thenReturn(presResponse);

        mockMvc.perform(get("/presupuestos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds[0]").value(1))
                .andExpect(jsonPath("$.ingresoIds[1]").value(2));

        verify(presService, times(1)).findByUsuario(1L);
    }

    @Test
    void testFindPresById() throws Exception {
        when(presService.findById(1L)).thenReturn(presResponse);

        mockMvc.perform(get("/presupuestos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds[0]").value(1))
                .andExpect(jsonPath("$.ingresoIds[1]").value(2));

        verify(presService, times(1)).findById(1L);
    }

    @Test
    void testSavePresupuesto() throws Exception {
        when(presService.save(any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(post("/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds[0]").value(1))
                .andExpect(jsonPath("$.ingresoIds[1]").value(2));

        verify(presService, times(1)).save(any(PresupuestoRequest.class));
    }

    @Test
    void testUpdatePresupuesto() throws Exception {
        when(presService.update(eq(1L), any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(put("/presupuestos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds[0]").value(1))
                .andExpect(jsonPath("$.ingresoIds[1]").value(2));

        verify(presService, times(1)).update(eq(1L), any(PresupuestoRequest.class));
    }

    @Test
    void testDeletePresupuesto() throws Exception {
        doNothing().when(presService).deleteById(1L);

        mockMvc.perform(delete("/presupuestos/1"))
                .andExpect(status().isNoContent());

        verify(presService, times(1)).deleteById(1L);
    }

    // Tests adicionales para casos edge
    @Test
    void testSavePresupuestoWithNullIngresoIds() throws Exception {
        // Request con ingresoIds null
        PresupuestoRequest requestWithNullIngresoIds = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)
                .ingresoIds(null)  // ingresoIds puede ser null
                .build();

        // Response con ingresoIds null (no estará en el JSON)
        PresupuestoResponse responseWithNullIngresoIds = PresupuestoResponse.builder()
                .id(2L)
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuarioId(1L)
                .ingresoIds(null)  // No estará en el JSON
                .build();

        when(presService.save(any(PresupuestoRequest.class))).thenReturn(responseWithNullIngresoIds);

        mockMvc.perform(post("/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullIngresoIds)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0))
                .andExpect(jsonPath("$.otrosGastos").value(30.0))
                .andExpect(jsonPath("$.ahorro").value(20.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds").doesNotExist());  // No debe existir en JSON

        verify(presService, times(1)).save(any(PresupuestoRequest.class));
    }

    @Test
    void testSavePresupuestoWithEmptyIngresoIds() throws Exception {
        // Request con lista vacía de ingresoIds
        PresupuestoRequest requestWithEmptyIngresoIds = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(80.00))
                .deudas(BigDecimal.valueOf(40.00))
                .otrosGastos(BigDecimal.valueOf(20.00))
                .ahorro(BigDecimal.valueOf(10.00))
                .usuarioId(1L)
                .ingresoIds(List.of())  // Lista vacía
                .build();

        // Response con lista vacía de ingresoIds
        PresupuestoResponse responseWithEmptyIngresoIds = PresupuestoResponse.builder()
                .id(3L)
                .gastosBasicos(BigDecimal.valueOf(80.00))
                .deudas(BigDecimal.valueOf(40.00))
                .otrosGastos(BigDecimal.valueOf(20.00))
                .ahorro(BigDecimal.valueOf(10.00))
                .usuarioId(1L)
                .ingresoIds(List.of())  // Lista vacía
                .build();

        when(presService.save(any(PresupuestoRequest.class))).thenReturn(responseWithEmptyIngresoIds);

        mockMvc.perform(post("/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithEmptyIngresoIds)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.gastosBasicos").value(80.0))
                .andExpect(jsonPath("$.deudas").value(40.0))
                .andExpect(jsonPath("$.otrosGastos").value(20.0))
                .andExpect(jsonPath("$.ahorro").value(10.0))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.ingresoIds").isArray())
                .andExpect(jsonPath("$.ingresoIds").isEmpty());  // Array vacío

        verify(presService, times(1)).save(any(PresupuestoRequest.class));
    }

    @Test
    void testSavePresupuestoWithInvalidData() throws Exception {
        // Test con datos inválidos
        PresupuestoRequest invalidRequest = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(-100))  // Violates @Positive
                .deudas(BigDecimal.valueOf(-50))  // Violates @PositiveOrZero
                .usuarioId(null)  // Violates @NotNull
                .build();

        mockMvc.perform(post("/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}