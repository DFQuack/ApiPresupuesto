package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;
import sv.edu.udb.repository.IngresoRepository;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.mapper.IngresoMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngresoServiceImplTest {

    @Mock
    private IngresoRepository ingresoRepo;

    @Mock
    private IngresoMapper ingresoMapper;

    @InjectMocks
    private IngresoServiceImpl ingresoService;

    private Ingreso ingreso;
    private IngresoRequest ingresoRequest;
    private IngresoResponse ingresoResponse;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Mock de objetos de dominio
        usuario = new Usuario();
        usuario.setId(1L);

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(1L);

        // Objeto Ingreso de prueba
        ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setNombre("Salario Mensual");
        ingreso.setSueldo(new BigDecimal("1500.00"));
        ingreso.setIngresoFormal(true);
        ingreso.setRetencionAFP(new BigDecimal("150.00"));
        ingreso.setRetencionISSS(new BigDecimal("45.00"));
        ingreso.setRetencionRenta(new BigDecimal("105.00"));
        ingreso.setSueldoNeto(new BigDecimal("1200.00"));
        ingreso.setUsuario(usuario);
        ingreso.setPresupuesto(presupuesto);

        // Objeto IngresoRequest de prueba - ACTUALIZADO
        ingresoRequest = IngresoRequest.builder()
                .nombre("Salario Mensual")
                .sueldo(new BigDecimal("1500.00"))
                .ingresoFormal(true)
                .usuarioId(1L)  // Cambiado de usuario a usuarioId
                .presupuestoId(1L)  // Cambiado de presupuesto a presupuestoId
                .build();

        // Objeto IngresoResponse de prueba - ACTUALIZADO
        ingresoResponse = IngresoResponse.builder()
                .id(1L)  // Agregado id
                .nombre("Salario Mensual")
                .sueldo(new BigDecimal("1500.00"))
                .ingresoFormal(true)
                .usuario(usuario)
                .presupuesto(presupuesto)
                .build();
    }

    @Test
    @DisplayName("Debería encontrar todos los ingresos de un usuario")
    void findAllByUsuario_shouldFindAllUserIngresos() {
        when(ingresoRepo.findByUsuario_Id(1L)).thenReturn(Collections.singletonList(ingreso));
        when(ingresoMapper.toIngresoResponseList(anyList())).thenReturn(Collections.singletonList(ingresoResponse));

        List<IngresoResponse> result = ingresoService.findAllByUsuario(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        // Verificar que el response tiene los campos correctos
        IngresoResponse response = result.get(0);
        assertEquals(1L, response.getId());
        assertEquals("Salario Mensual", response.getNombre());
        assertEquals(new BigDecimal("1500.00"), response.getSueldo());
        assertNotNull(response.getUsuario());
        assertNotNull(response.getPresupuesto());

        verify(ingresoRepo).findByUsuario_Id(1L);
        verify(ingresoMapper).toIngresoResponseList(Collections.singletonList(ingreso));
    }

    @Test
    @DisplayName("Debería encontrar un ingreso por ID")
    void findById_shouldFindIngresoById() {
        when(ingresoRepo.findById(1L)).thenReturn(Optional.of(ingreso));
        when(ingresoMapper.toIngresoResponse(ingreso)).thenReturn(ingresoResponse);

        IngresoResponse result = ingresoService.findById(1L);

        assertNotNull(result);
        assertEquals("Salario Mensual", result.getNombre());
        assertEquals(new BigDecimal("1500.00"), result.getSueldo());
        assertNotNull(result.getUsuario());
        assertNotNull(result.getPresupuesto());
        verify(ingresoRepo).findById(1L);
        verify(ingresoMapper).toIngresoResponse(ingreso);
    }

    @Test
    @DisplayName("Debería lanzar excepción al no encontrar ingreso por ID")
    void findById_shouldThrowExceptionWhenNotFound() {
        when(ingresoRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ingresoService.findById(2L));
        verify(ingresoRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería guardar un nuevo ingreso")
    void save_shouldSaveNewIngreso() {
        when(ingresoMapper.toIngreso(ingresoRequest)).thenReturn(ingreso);
        when(ingresoRepo.save(ingreso)).thenReturn(ingreso);
        when(ingresoMapper.toIngresoResponse(ingreso)).thenReturn(ingresoResponse);

        IngresoResponse result = ingresoService.save(ingresoRequest);

        assertNotNull(result);
        assertEquals("Salario Mensual", result.getNombre());
        assertNotNull(result.getUsuario());
        assertNotNull(result.getPresupuesto());
        verify(ingresoMapper).toIngreso(ingresoRequest);
        verify(ingresoRepo).save(ingreso);
        verify(ingresoMapper).toIngresoResponse(ingreso);
    }

    @Test
    @DisplayName("Debería actualizar un ingreso existente")
    void update_shouldUpdateExistingIngreso() {
        when(ingresoRepo.findById(1L)).thenReturn(Optional.of(ingreso));
        when(ingresoRepo.save(any(Ingreso.class))).thenReturn(ingreso);
        when(ingresoMapper.toIngresoResponse(ingreso)).thenReturn(ingresoResponse);

        IngresoResponse result = ingresoService.update(1L, ingresoRequest);

        assertNotNull(result);
        assertEquals("Salario Mensual", result.getNombre());
        assertNotNull(result.getUsuario());
        assertNotNull(result.getPresupuesto());
        verify(ingresoRepo).findById(1L);
        verify(ingresoRepo).save(ingreso);
        verify(ingresoMapper).toIngresoResponse(ingreso);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un ingreso no existente")
    void update_shouldThrowExceptionWhenNotFound() {
        when(ingresoRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ingresoService.update(2L, ingresoRequest));
        verify(ingresoRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería eliminar un ingreso por ID")
    void deleteById_shouldDeleteIngresoById() {
        doNothing().when(ingresoRepo).deleteById(1L);

        ingresoService.deleteById(1L);

        verify(ingresoRepo).deleteById(1L);
    }

    @Test
    @DisplayName("Debería manejar ingresos sin presupuesto asignado")
    void save_shouldHandleIngresoWithoutPresupuesto() {
        // Request sin presupuestoId
        IngresoRequest requestWithoutPresupuesto = IngresoRequest.builder()
                .nombre("Ingreso Informal")
                .sueldo(new BigDecimal("500.00"))
                .ingresoFormal(false)
                .usuarioId(1L)
                .presupuestoId(null)  // presupuestoId nulo
                .build();

        // Ingreso sin presupuesto
        Ingreso ingresoWithoutPresupuesto = new Ingreso();
        ingresoWithoutPresupuesto.setId(2L);
        ingresoWithoutPresupuesto.setNombre("Ingreso Informal");
        ingresoWithoutPresupuesto.setSueldo(new BigDecimal("500.00"));
        ingresoWithoutPresupuesto.setIngresoFormal(false);
        ingresoWithoutPresupuesto.setUsuario(usuario);
        ingresoWithoutPresupuesto.setPresupuesto(null);  // presupuesto nulo

        // Response sin presupuestoId
        IngresoResponse responseWithoutPresupuesto = IngresoResponse.builder()
                .id(2L)
                .nombre("Ingreso Informal")
                .sueldo(new BigDecimal("500.00"))
                .ingresoFormal(false)
                .usuario(usuario)
                .presupuesto(null)  // presupuesto nulo
                .build();

        when(ingresoMapper.toIngreso(requestWithoutPresupuesto)).thenReturn(ingresoWithoutPresupuesto);
        when(ingresoRepo.save(ingresoWithoutPresupuesto)).thenReturn(ingresoWithoutPresupuesto);
        when(ingresoMapper.toIngresoResponse(ingresoWithoutPresupuesto)).thenReturn(responseWithoutPresupuesto);

        IngresoResponse result = ingresoService.save(requestWithoutPresupuesto);

        assertNotNull(result);
        assertEquals("Ingreso Informal", result.getNombre());
        assertNull(result.getPresupuesto());  // Verificar que presupuesto es null
        verify(ingresoMapper).toIngreso(requestWithoutPresupuesto);
        verify(ingresoRepo).save(ingresoWithoutPresupuesto);
        verify(ingresoMapper).toIngresoResponse(ingresoWithoutPresupuesto);
    }
}