package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.repository.domain.Usuario;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GastoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveGastoMensual() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("usuariogastos");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto = new Gasto();
        gasto.setMes(Month.JANUARY);
        gasto.setGastosBasicos(new BigDecimal("200.00"));
        gasto.setDeudas(new BigDecimal("100.00"));
        gasto.setOtrosGastos(new BigDecimal("50.00"));
        gasto.setAhorro(new BigDecimal("150.00"));
        gasto.setUsuario(savedUsuario);

        // When
        Gasto savedGasto = gastoRepository.save(gasto);

        // Then
        assertNotNull(savedGasto.getIdGasto());
        assertEquals(Month.JANUARY, savedGasto.getMes());
        assertEquals(new BigDecimal("200.00"), savedGasto.getGastosBasicos());
        assertEquals(savedUsuario.getId(), savedGasto.getUsuario().getId());
    }

    @Test
    public void testFindByUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser4");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto1 = new Gasto();
        gasto1.setMes(Month.JANUARY);
        gasto1.setGastosBasicos(new BigDecimal("300.00"));
        gasto1.setUsuario(savedUsuario);
        gastoRepository.save(gasto1);

        Gasto gasto2 = new Gasto();
        gasto2.setMes(Month.FEBRUARY);
        gasto2.setGastosBasicos(new BigDecimal("350.00"));
        gasto2.setUsuario(savedUsuario);
        gastoRepository.save(gasto2);

        // When
        List<Gasto> gastos = gastoRepository.findByUsuario(savedUsuario);

        // Then
        assertEquals(2, gastos.size());
        assertEquals(Month.JANUARY, gastos.get(0).getMes());
        assertEquals(Month.FEBRUARY, gastos.get(1).getMes());
    }

    @Test
    public void testFindByUsuarioId() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser5");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto = new Gasto();
        gasto.setMes(Month.MARCH);
        gasto.setGastosBasicos(new BigDecimal("400.00"));
        gasto.setUsuario(savedUsuario);
        gastoRepository.save(gasto);

        // When
        List<Gasto> gastos = gastoRepository.findByUsuario_Id(savedUsuario.getId());

        // Then
        assertEquals(1, gastos.size());
        assertEquals(Month.MARCH, gastos.get(0).getMes());
    }

    @Test
    public void testFindByMesAndUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser6");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto = new Gasto();
        gasto.setMes(Month.MARCH);
        gasto.setGastosBasicos(new BigDecimal("400.00"));
        gasto.setUsuario(savedUsuario);
        gastoRepository.save(gasto);

        // When
        Optional<Gasto> foundGasto = gastoRepository.findByMesAndUsuario(Month.MARCH, savedUsuario);

        // Then
        assertTrue(foundGasto.isPresent());
        assertEquals(Month.MARCH, foundGasto.get().getMes());
        assertEquals(new BigDecimal("400.00"), foundGasto.get().getGastosBasicos());
    }

    @Test
    public void testFindByMesAndUsuarioId() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser7");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto = new Gasto();
        gasto.setMes(Month.APRIL);
        gasto.setGastosBasicos(new BigDecimal("500.00"));
        gasto.setUsuario(savedUsuario);
        gastoRepository.save(gasto);

        // When
        Optional<Gasto> foundGasto = gastoRepository.findByMesAndUsuario_Id(Month.APRIL, savedUsuario.getId());

        // Then
        assertTrue(foundGasto.isPresent());
        assertEquals(Month.APRIL, foundGasto.get().getMes());
        assertEquals(new BigDecimal("500.00"), foundGasto.get().getGastosBasicos());
    }

    @Test
    public void testFindGastoByMes() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser8");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Gasto gasto = new Gasto();
        gasto.setMes(Month.MAY);
        gasto.setGastosBasicos(new BigDecimal("600.00"));
        gasto.setUsuario(savedUsuario);
        gastoRepository.save(gasto);

        // When
        Gasto foundGasto = gastoRepository.findGastoByMes(Month.MAY);

        // Then
        assertNotNull(foundGasto);
        assertEquals(Month.MAY, foundGasto.getMes());
        assertEquals(new BigDecimal("600.00"), foundGasto.getGastosBasicos());
    }

    @Test
    public void testFindByMesAndUsuario_NotFound() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser9");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // When - Buscar gasto que no existe
        Optional<Gasto> foundGasto = gastoRepository.findByMesAndUsuario(Month.JUNE, savedUsuario);

        // Then
        assertFalse(foundGasto.isPresent());
    }

    @Test
    public void testFindByUsuario_EmptyList() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser10");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // When - Buscar gastos de usuario sin gastos
        List<Gasto> gastos = gastoRepository.findByUsuario(savedUsuario);

        // Then
        assertTrue(gastos.isEmpty());
        assertEquals(0, gastos.size());
    }
}