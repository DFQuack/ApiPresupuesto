package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Usuario;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IngresoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IngresoRepository ingresoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveIngresoWithRetenciones() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Ingreso ingreso = new Ingreso();
        ingreso.setNombre("Salario Formal");
        ingreso.setSueldo(new BigDecimal("1000.00"));
        ingreso.setIngresoFormal(true);
        ingreso.setUsuario(savedUsuario);
        ingreso.calcularRetenciones(); // Calcular retenciones

        // When
        Ingreso savedIngreso = ingresoRepository.save(ingreso);

        // Then
        assertNotNull(savedIngreso.getId());
        assertEquals("Salario Formal", savedIngreso.getNombre());
        assertTrue(savedIngreso.getIngresoFormal());
        assertNotNull(savedIngreso.getRetencionAFP());
        assertNotNull(savedIngreso.getRetencionISSS());
        assertNotNull(savedIngreso.getRetencionRenta());
        assertNotNull(savedIngreso.getSueldoNeto());
    }

    @Test
    public void testIngresoInformalNoRetenciones() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser2");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Ingreso ingreso = new Ingreso();
        ingreso.setNombre("Freelance");
        ingreso.setSueldo(new BigDecimal("800.00"));
        ingreso.setIngresoFormal(false);
        ingreso.setUsuario(savedUsuario);
        ingreso.calcularRetenciones();

        // When
        Ingreso savedIngreso = ingresoRepository.save(ingreso);

        // Then
        assertEquals(BigDecimal.ZERO, savedIngreso.getRetencionAFP());
        assertEquals(BigDecimal.ZERO, savedIngreso.getRetencionISSS());
        assertEquals(BigDecimal.ZERO, savedIngreso.getRetencionRenta());
        assertEquals(new BigDecimal("800.00"), savedIngreso.getSueldoNeto());
    }

    @Test
    public void testFindByUsuarioId() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("usuarioconingresos");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        Ingreso ingreso1 = new Ingreso();
        ingreso1.setNombre("Salario 1");
        ingreso1.setSueldo(new BigDecimal("1000.00"));
        ingreso1.setIngresoFormal(true);
        ingreso1.setUsuario(savedUsuario);
        ingresoRepository.save(ingreso1);

        Ingreso ingreso2 = new Ingreso();
        ingreso2.setNombre("Salario 2");
        ingreso2.setSueldo(new BigDecimal("500.00"));
        ingreso2.setIngresoFormal(false);
        ingreso2.setUsuario(savedUsuario);
        ingresoRepository.save(ingreso2);

        // When
        List<Ingreso> ingresos = ingresoRepository.findByUsuario_Id(savedUsuario.getId());

        // Then
        assertEquals(2, ingresos.size());
    }

    @Test
    public void testFindByUsuarioId_NoIngresos() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("usuariosiningresos");
        usuario.setContraseña("password");
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // When
        List<Ingreso> ingresos = ingresoRepository.findByUsuario_Id(savedUsuario.getId());

        // Then
        assertTrue(ingresos.isEmpty());
        assertEquals(0, ingresos.size());
    }
}