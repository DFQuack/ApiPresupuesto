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
import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;
import sv.edu.udb.service.IngresoService;
import sv.edu.udb.service.mapper.IngresoMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngresoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IngresoService ingresoService;

    @Mock
    private IngresoMapper ingresoMapper;

    @InjectMocks
    private IngresoController ingresoController;

    private ObjectMapper objectMapper;

    private IngresoRequest ingresoRequest;
    private IngresoResponse ingresoResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingresoController).build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // IngresoRequest con todos los campos
        ingresoRequest = IngresoRequest.builder()
                .nombre("Salario")
                .sueldo(BigDecimal.valueOf(1000.00))
                .ingresoFormal(true)
                .usuarioId(1L)
                .presupuestoId(1L)
                .build();

        // IngresoResponse con todos los campos
        ingresoResponse = IngresoResponse.builder()
                .id(1L)
                .nombre("Salario")
                .sueldo(BigDecimal.valueOf(1000.00))
                .ingresoFormal(true)
                .usuarioId(1L)
                .presupuestoId(1L)
                .build();
    }

    @Test
    void testFindAllIngresosByUsuario() throws Exception {
        when(ingresoService.findAllByUsuario(1L)).thenReturn(List.of(ingresoResponse));

        mockMvc.perform(get("/ingresos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Salario"))
                .andExpect(jsonPath("$[0].sueldo").value(1000.0))
                .andExpect(jsonPath("$[0].ingresoFormal").value(true))
                .andExpect(jsonPath("$[0].usuarioId").value(1))
                .andExpect(jsonPath("$[0].presupuestoId").value(1));

        verify(ingresoService, times(1)).findAllByUsuario(1L);
    }

    @Test
    void testFindIngresoById() throws Exception {
        when(ingresoService.findById(1L)).thenReturn(ingresoResponse);

        mockMvc.perform(get("/ingresos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Salario"))
                .andExpect(jsonPath("$.sueldo").value(1000.0))
                .andExpect(jsonPath("$.ingresoFormal").value(true))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.presupuestoId").value(1));

        verify(ingresoService, times(1)).findById(1L);
    }

    @Test
    void testSaveIngreso() throws Exception {
        when(ingresoService.save(any(IngresoRequest.class))).thenReturn(ingresoResponse);

        mockMvc.perform(post("/ingresos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Salario"))
                .andExpect(jsonPath("$.sueldo").value(1000.0))
                .andExpect(jsonPath("$.ingresoFormal").value(true))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.presupuestoId").value(1));

        verify(ingresoService, times(1)).save(any(IngresoRequest.class));
    }

    @Test
    void testUpdateIngreso() throws Exception {
        when(ingresoService.update(eq(1L), any(IngresoRequest.class))).thenReturn(ingresoResponse);

        mockMvc.perform(put("/ingresos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Salario"))
                .andExpect(jsonPath("$.sueldo").value(1000.0))
                .andExpect(jsonPath("$.ingresoFormal").value(true))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.presupuestoId").value(1));

        verify(ingresoService, times(1)).update(eq(1L), any(IngresoRequest.class));
    }

    @Test
    void testDeleteIngreso() throws Exception {
        doNothing().when(ingresoService).deleteById(1L);

        mockMvc.perform(delete("/ingresos/1"))
                .andExpect(status().isNoContent());

        verify(ingresoService, times(1)).deleteById(1L);
    }

    @Test
    void testSaveIngresoWithInvalidData() throws Exception {
        // Test con datos inválidos
        IngresoRequest invalidRequest = IngresoRequest.builder()
                .nombre("") // Nombre vacío - viola @NotBlank
                .sueldo(BigDecimal.valueOf(-100)) // Sueldo negativo - viola @Positive
                .usuarioId(null) // UsuarioId nulo - viola @NotNull
                .build();

        mockMvc.perform(post("/ingresos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()); // Esperar error de validación
    }

    @Test
    void testSaveIngresoWithValidDataButNullOptionalFields() throws Exception {
        // Test con campos opcionales nulos - SOLUCIÓN AL ERROR
        IngresoRequest requestWithNullOptionalFields = IngresoRequest.builder()
                .nombre("Salario Informal")
                .sueldo(BigDecimal.valueOf(500.00))
                .ingresoFormal(false)
                .usuarioId(1L)
                .presupuestoId(null) // presupuestoId puede ser null
                .build();

        // Response con presupuestoId null - PERO como está anotado con @JsonInclude(NON_NULL)
        // no se incluirá en el JSON, así que no debemos verificar su existencia
        IngresoResponse responseWithNullOptionalFields = IngresoResponse.builder()
                .id(2L)
                .nombre("Salario Informal")
                .sueldo(BigDecimal.valueOf(500.00))
                .ingresoFormal(false)
                .usuarioId(1L)
                .presupuestoId(null) // Este campo no estará en el JSON
                .build();

        when(ingresoService.save(any(IngresoRequest.class))).thenReturn(responseWithNullOptionalFields);

        mockMvc.perform(post("/ingresos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullOptionalFields)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Salario Informal"))
                .andExpect(jsonPath("$.sueldo").value(500.0))
                .andExpect(jsonPath("$.ingresoFormal").value(false))
                .andExpect(jsonPath("$.usuarioId").value(1))
                // NO verificamos presupuestoId porque cuando es null, no se incluye en el JSON
                // debido a @JsonInclude(NON_NULL)
                .andExpect(jsonPath("$.presupuestoId").doesNotExist()); // Verificar que NO existe

        verify(ingresoService, times(1)).save(any(IngresoRequest.class));
    }

    // Test adicional para cuando ingresoFormal es null
    @Test
    void testSaveIngresoWithNullIngresoFormal() throws Exception {
        IngresoRequest requestWithNullIngresoFormal = IngresoRequest.builder()
                .nombre("Ingreso Extra")
                .sueldo(BigDecimal.valueOf(300.00))
                .ingresoFormal(null) // ingresoFormal puede ser null
                .usuarioId(1L)
                .presupuestoId(2L)
                .build();

        IngresoResponse responseWithNullIngresoFormal = IngresoResponse.builder()
                .id(3L)
                .nombre("Ingreso Extra")
                .sueldo(BigDecimal.valueOf(300.00))
                .ingresoFormal(null) // No estará en el JSON
                .usuarioId(1L)
                .presupuestoId(2L)
                .build();

        when(ingresoService.save(any(IngresoRequest.class))).thenReturn(responseWithNullIngresoFormal);

        mockMvc.perform(post("/ingresos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullIngresoFormal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Ingreso Extra"))
                .andExpect(jsonPath("$.sueldo").value(300.0))
                .andExpect(jsonPath("$.ingresoFormal").doesNotExist()) // No debe existir en JSON
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.presupuestoId").value(2));

        verify(ingresoService, times(1)).save(any(IngresoRequest.class));
    }
}