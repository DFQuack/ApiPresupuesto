package sv.edu.udb.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.repository.GastoRepository;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.mapper.GastoMapper;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GastoServiceImplTest {

    @Mock
    private GastoRepository gastoRepo;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GastoMapper gastoMapper;

    @InjectMocks
    private GastoServiceImpl gastoService;

    private Gasto gasto;
    private GastoRequest gastoRequest;
    private GastoResponse gastoResponse;
    private Usuario usuario;

    @BeforeEach
    void setUp() {

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario1");


        gasto = new Gasto();
        gasto.setId(1L);
        gasto.setMes(Month.JANUARY);
        gasto.setGastosBasicos(new BigDecimal("100.00"));
        gasto.setDeudas(new BigDecimal("50.00"));
        gasto.setOtrosGastos(new BigDecimal("20.00"));
        gasto.setAhorro(new BigDecimal("30.00"));
        gasto.setUsuario(usuario);


        gastoRequest = GastoRequest.builder()
                .mes(Month.JANUARY)
                .gastosBasicos(new BigDecimal("100.00"))
                .deudas(new BigDecimal("50.00"))
                .otrosGastos(new BigDecimal("20.00"))
                .ahorro(new BigDecimal("30.00"))
                .usuarioId(1L)
                .build();


        gastoResponse = GastoResponse.builder()
                .id(1L)
                .mes(Month.JANUARY)
                .gastosBasicos(new BigDecimal("100.00"))
                .deudas(new BigDecimal("50.00"))
                .otrosGastos(new BigDecimal("20.00"))
                .ahorro(new BigDecimal("30.00"))
                .usuarioId(1L)
                .build();
    }

    @Test
    @DisplayName("Debería encontrar todos los gastos de un usuario")
    void findAllByUsuario_shouldFindAllUserGastos() {
        when(gastoRepo.findByUsuario_Id(1L)).thenReturn(Collections.singletonList(gasto));
        when(gastoMapper.toGastoResponseList(anyList())).thenReturn(Collections.singletonList(gastoResponse));

        List<GastoResponse> result = gastoService.findAllByUsuario(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
        verify(gastoRepo).findByUsuario_Id(1L);
        verify(gastoMapper).toGastoResponseList(Collections.singletonList(gasto));
    }

    @Test
    @DisplayName("Debería encontrar un gasto por ID")
    void findById_shouldFindGastoById() {
        when(gastoRepo.findById(1L)).thenReturn(Optional.of(gasto));
        when(gastoMapper.toGastoResponse(gasto)).thenReturn(gastoResponse);

        GastoResponse result = gastoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Month.JANUARY, result.getMes());
        assertEquals(new BigDecimal("100.00"), result.getGastosBasicos());
        assertEquals(1L, result.getUsuarioId());
        verify(gastoRepo).findById(1L);
        verify(gastoMapper).toGastoResponse(gasto);
    }

    @Test
    @DisplayName("Debería lanzar excepción al no encontrar gasto por ID")
    void findById_shouldThrowExceptionWhenNotFound() {
        when(gastoRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gastoService.findById(2L));
        verify(gastoRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería guardar un nuevo gasto")
    void save_shouldSaveNewGasto() {
        // Mock del usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(gastoMapper.toGasto(gastoRequest)).thenReturn(gasto);
        when(gastoRepo.save(gasto)).thenReturn(gasto);
        when(gastoMapper.toGastoResponse(gasto)).thenReturn(gastoResponse);

        GastoResponse result = gastoService.save(gastoRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Month.JANUARY, result.getMes());
        assertEquals(1L, result.getUsuarioId());
        verify(gastoMapper).toGasto(gastoRequest);
        verify(gastoRepo).save(gasto);
        verify(gastoMapper).toGastoResponse(gasto);
    }

    @Test
    @DisplayName("Debería actualizar un gasto existente")
    void update_shouldUpdateExistingGasto() {
        when(gastoRepo.findById(1L)).thenReturn(Optional.of(gasto));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(gastoRepo.save(any(Gasto.class))).thenReturn(gasto);
        when(gastoMapper.toGastoResponse(gasto)).thenReturn(gastoResponse);

        GastoResponse result = gastoService.update(1L, gastoRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Month.JANUARY, result.getMes());
        assertEquals(1L, result.getUsuarioId());
        verify(gastoRepo).findById(1L);
        verify(gastoRepo).save(gasto);
        verify(gastoMapper).toGastoResponse(gasto);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un gasto no existente")
    void update_shouldThrowExceptionWhenNotFound() {
        when(gastoRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gastoService.update(2L, gastoRequest));
        verify(gastoRepo).findById(2L);
    }

    @Test
    @DisplayName("Debería eliminar un gasto por ID")
    void deleteById_shouldDeleteGastoById() {
        doNothing().when(gastoRepo).deleteById(1L);

        gastoService.deleteById(1L);

        verify(gastoRepo).deleteById(1L);
    }
}