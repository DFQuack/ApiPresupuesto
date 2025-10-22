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
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
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

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(1L);

        ingresoRequest = IngresoRequest.builder()
                .nombre("Salario")
                .sueldo(BigDecimal.valueOf(1000.00))
                .ingresoFormal(true)
                .usuario(usuario)
                .presupuesto(presupuesto)
                .build();

        ingresoResponse = IngresoResponse.builder()
                .nombre("Salario")
                .sueldo(BigDecimal.valueOf(1000.00))
                .ingresoFormal(true)
                .retencionAFP(BigDecimal.valueOf(50.00))
                .retencionISSS(BigDecimal.valueOf(20.00))
                .retencionRenta(BigDecimal.valueOf(30.00))
                .sueldoNeto(BigDecimal.valueOf(900.00))
                .usuario(usuario)
                .presupuesto(presupuesto)
                .build();
    }

    @Test
    void testFindAllIngresosByUsuario() throws Exception {
        when(ingresoService.findAllByUsuario(1L)).thenReturn(List.of(ingresoResponse));

        mockMvc.perform(get("/ingresos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Salario"))
                .andExpect(jsonPath("$[0].sueldo").value(1000.0));

        verify(ingresoService, times(1)).findAllByUsuario(1L);
    }

    @Test
    void testFindIngresoById() throws Exception {
        when(ingresoService.findById(1L)).thenReturn(ingresoResponse);

        mockMvc.perform(get("/ingresos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Salario"))
                .andExpect(jsonPath("$.sueldo").value(1000.0));

        verify(ingresoService, times(1)).findById(1L);
    }

    @Test
    void testSaveIngreso() throws Exception {
        when(ingresoService.save(any(IngresoRequest.class))).thenReturn(ingresoResponse);

        mockMvc.perform(post("/ingresos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Salario"))
                .andExpect(jsonPath("$.sueldo").value(1000.0));

        verify(ingresoService, times(1)).save(any(IngresoRequest.class));
    }

    @Test
    void testUpdateIngreso() throws Exception {
        when(ingresoService.update(eq(1L), any(IngresoRequest.class))).thenReturn(ingresoResponse);

        mockMvc.perform(put("/ingresos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresoRequest)))
                .andExpect(status().isOk());


        verify(ingresoService, times(1)).update(eq(1L), any(IngresoRequest.class));
    }

    @Test
    void testDeleteIngreso() throws Exception {
        doNothing().when(ingresoService).deleteById(1L);

        mockMvc.perform(delete("/ingresos/1"))
                .andExpect(status().isNoContent());

        verify(ingresoService, times(1)).deleteById(1L);
    }
}
