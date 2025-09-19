package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PresupuestoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSavePresupuesto() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("usuariopresupuesto");
        usuario.setPassword("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setGastosBasicos(new BigDecimal("300.00"));
        presupuesto.setDeudas(new BigDecimal("100.00"));
        presupuesto.setOtrosGastos(new BigDecimal("50.00"));
        presupuesto.setAhorro(new BigDecimal("150.00"));
        presupuesto.setUsuario(savedUsuario);

        // When
        Presupuesto savedPresupuesto = presupuestoRepository.save(presupuesto);

        // Then
        assertNotNull(savedPresupuesto.getId());
        assertEquals(new BigDecimal("300.00"), savedPresupuesto.getGastosBasicos());
        assertEquals(savedUsuario.getId(), savedPresupuesto.getUsuario().getId());
    }

    @Test
    public void testFindPresupuestoByUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser3");
        usuario.setPassword("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setGastosBasicos(new BigDecimal("400.00"));
        presupuesto.setDeudas(new BigDecimal("200.00"));
        presupuesto.setOtrosGastos(new BigDecimal("100.00"));
        presupuesto.setAhorro(new BigDecimal("300.00"));
        presupuesto.setUsuario(savedUsuario);
        presupuestoRepository.save(presupuesto);

        // When
        Optional<Presupuesto> foundPresupuesto = presupuestoRepository.findByUsuario(savedUsuario);

        // Then
        assertTrue(foundPresupuesto.isPresent());
        assertEquals(new BigDecimal("400.00"), foundPresupuesto.get().getGastosBasicos());
        assertEquals(savedUsuario.getId(), foundPresupuesto.get().getUsuario().getId());
    }

    @Test
    public void testFindPresupuestoByUsuarioId() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser4");
        usuario.setPassword("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setGastosBasicos(new BigDecimal("500.00"));
        presupuesto.setDeudas(new BigDecimal("150.00"));
        presupuesto.setOtrosGastos(new BigDecimal("75.00"));
        presupuesto.setAhorro(new BigDecimal("200.00"));
        presupuesto.setUsuario(savedUsuario);
        presupuestoRepository.save(presupuesto);

        // When
        Presupuesto foundPresupuesto = presupuestoRepository.findByUsuario_Id(savedUsuario.getId());

        // Then
        assertNotNull(foundPresupuesto);
        assertEquals(new BigDecimal("500.00"), foundPresupuesto.getGastosBasicos());
    }

    @Test
    public void testPresupuestoNotFound() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser5");
        usuario.setPassword("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // When - Buscar presupuesto para usuario que no tiene
        Optional<Presupuesto> foundPresupuesto = presupuestoRepository.findByUsuario(savedUsuario);
        Presupuesto foundPresupuestoById = presupuestoRepository.findByUsuario_Id(savedUsuario.getId());

        // Then
        assertFalse(foundPresupuesto.isPresent());
        assertNull(foundPresupuestoById);
    }

    @Test
    public void testUniquePresupuestoPerUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser6");
        usuario.setPassword("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Crear primer presupuesto
        Presupuesto presupuesto1 = new Presupuesto();
        presupuesto1.setGastosBasicos(new BigDecimal("400.00"));
        presupuesto1.setUsuario(savedUsuario);
        presupuestoRepository.save(presupuesto1);

        // Crear segundo presupuesto para el mismo usuario (debería ser único por la relación @OneToOne)
        Presupuesto presupuesto2 = new Presupuesto();
        presupuesto2.setGastosBasicos(new BigDecimal("600.00"));
        presupuesto2.setUsuario(savedUsuario);

        // Then - Solo debería haber un presupuesto activo por usuario
        Optional<Presupuesto> foundPresupuesto = presupuestoRepository.findByUsuario(savedUsuario);
        assertTrue(foundPresupuesto.isPresent());
    }
}