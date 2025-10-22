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

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);

        presRequest = PresupuestoRequest.builder()
                .gastosBasicos(BigDecimal.valueOf(100.00))
                .deudas(BigDecimal.valueOf(50.00))
                .otrosGastos(BigDecimal.valueOf(30.00))
                .ahorro(BigDecimal.valueOf(20.00))
                .usuario(usuario)
                .ingresos(List.of(ingreso))
                .build();

        presResponse = PresupuestoResponse.builder()
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

        mockMvc.perform(get("/presupuestos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0));

        verify(presService, times(1)).findByUsuario(1L);
    }

    @Test
    void testFindPresById() throws Exception {
        when(presService.findById(1L)).thenReturn(presResponse);

        mockMvc.perform(get("/presupuestos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0));

        verify(presService, times(1)).findById(1L);
    }

    @Test
    void testSavePresupuesto() throws Exception {
        when(presService.save(any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(post("/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0));

        verify(presService, times(1)).save(any(PresupuestoRequest.class));
    }

    @Test
    void testUpdatePresupuesto() throws Exception {
        when(presService.update(eq(1L), any(PresupuestoRequest.class))).thenReturn(presResponse);

        mockMvc.perform(put("/presupuestos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(presRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gastosBasicos").value(100.0))
                .andExpect(jsonPath("$.deudas").value(50.0));

        verify(presService, times(1)).update(eq(1L), any(PresupuestoRequest.class));
    }

    @Test
    void testDeletePresupuesto() throws Exception {
        doNothing().when(presService).deleteById(1L);

        mockMvc.perform(delete("/presupuestos/1"))
                .andExpect(status().isNoContent());

        verify(presService, times(1)).deleteById(1L);
    }
}
