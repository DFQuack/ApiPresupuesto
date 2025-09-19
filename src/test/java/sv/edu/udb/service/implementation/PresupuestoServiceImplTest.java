package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;
import sv.edu.udb.repository.PresupuestoRepository;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.mapper.PresupuestoMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresupuestoServiceImplTest {

    @Mock
    private PresupuestoRepository presRepo;

    @Mock
    private PresupuestoMapper presMapper;

    @InjectMocks
    private PresupuestoServiceImpl presupuestoService;

    private Presupuesto presupuesto;
    private PresupuestoRequest presupuestoRequest;
    private PresupuestoResponse presupuestoResponse;
    private Usuario usuario;
    private Ingreso ingreso;

    @BeforeEach
    void setUp() {
        // Mock de objetos de dominio
        usuario = new Usuario();
        usuario.setId(1L);

        ingreso = new Ingreso();
        ingreso.setId(1L);

        // Objeto Presupuesto de prueba
        presupuesto = new Presupuesto();
        presupuesto.setId(1L);
        presupuesto.setGastosBasicos(new BigDecimal("500.00"));
        presupuesto.setDeudas(new BigDecimal("200.00"));
        presupuesto.setOtrosGastos(new BigDecimal("150.00"));
        presupuesto.setAhorro(new BigDecimal("150.00"));
        presupuesto.setUsuario(usuario);
        presupuesto.setIngresos(Collections.singletonList(ingreso));

        // Objeto PresupuestoRequest de prueba
        presupuestoRequest = PresupuestoRequest.builder()
                .gastosBasicos(new BigDecimal("500.00"))
                .deudas(new BigDecimal("200.00"))
                .otrosGastos(new BigDecimal("150.00"))
                .ahorro(new BigDecimal("150.00"))
                .usuario(usuario)
                .ingresos(Collections.singletonList(ingreso))
                .build();

        // Objeto PresupuestoResponse de prueba
        presupuestoResponse = PresupuestoResponse.builder()
                .gastosBasicos(new BigDecimal("500.00"))
                .deudas(new BigDecimal("200.00"))
                .otrosGastos(new BigDecimal("150.00"))
                .ahorro(new BigDecimal("150.00"))
                .usuario(usuario)
                .ingresos(Collections.singletonList(ingreso))
                .build();
    }

    @Test
    @DisplayName("Debería encontrar un presupuesto por ID de usuario")
    void findByUsuario_shouldFindPresupuestoByUsuarioId() {
        when(presRepo.findByUsuario_Id(1L)).thenReturn(presupuesto);
        when(presMapper.toPresResponse(presupuesto)).thenReturn(presupuestoResponse);

        PresupuestoResponse result = presupuestoService.findByUsuario(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("500.00"), result.getGastosBasicos());
        verify(presRepo).findByUsuario_Id(1L);
        verify(presMapper).toPresResponse(presupuesto);
    }

    @Test
    @DisplayName("Debería encontrar un presupuesto por su propio ID")
    void findById_shouldFindPresupuestoById() {
        when(presRepo.findById(1L)).thenReturn(Optional.of(presupuesto));
        when(presMapper.toPresResponse(presupuesto)).thenReturn(presupuestoResponse);

        PresupuestoResponse result = presupuestoService.findById(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("500.00"), result.getGastosBasicos());
        verify(presRepo).findById(1L);
        verify(presMapper).toPresResponse(presupuesto);
    }

    @Test
    @DisplayName("Debería lanzar excepción si no encuentra presupuesto por ID")
    void findById_shouldThrowExceptionWhenNotFound() {
        when(presRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> presupuestoService.findById(2L));
        verify(presRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería guardar un nuevo presupuesto")
    void save_shouldSaveNewPresupuesto() {
        when(presMapper.toPresupuesto(presupuestoRequest)).thenReturn(presupuesto);
        when(presRepo.save(presupuesto)).thenReturn(presupuesto);
        when(presMapper.toPresResponse(presupuesto)).thenReturn(presupuestoResponse);

        PresupuestoResponse result = presupuestoService.save(presupuestoRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("500.00"), result.getGastosBasicos());
        verify(presMapper).toPresupuesto(presupuestoRequest);
        verify(presRepo).save(presupuesto);
        verify(presMapper).toPresResponse(presupuesto);
    }

    @Test
    @DisplayName("Debería actualizar un presupuesto existente")
    void update_shouldUpdateExistingPresupuesto() {
        when(presRepo.findById(1L)).thenReturn(Optional.of(presupuesto));
        when(presRepo.save(any(Presupuesto.class))).thenReturn(presupuesto);
        when(presMapper.toPresResponse(presupuesto)).thenReturn(presupuestoResponse);

        PresupuestoResponse result = presupuestoService.update(1L, presupuestoRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("500.00"), result.getGastosBasicos());
        verify(presRepo).findById(1L);
        verify(presRepo).save(presupuesto);
        verify(presMapper).toPresResponse(presupuesto);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un presupuesto no existente")
    void update_shouldThrowExceptionWhenNotFound() {
        when(presRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> presupuestoService.update(2L, presupuestoRequest));
        verify(presRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería eliminar un presupuesto por ID")
    void deleteById_shouldDeletePresupuestoById() {
        doNothing().when(presRepo).deleteById(1L);

        presupuestoService.deleteById(1L);

        verify(presRepo).deleteById(1L);
    }
}