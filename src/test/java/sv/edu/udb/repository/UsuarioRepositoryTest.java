package sv.edu.udb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sv.edu.udb.repository.domain.Usuario;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveAndFindUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("juanperez");
        usuario.setPassword("password123");

        // When
        Usuario savedUsuario = usuarioRepository.save(usuario);
        Optional<Usuario> foundUsuario = usuarioRepository.findById(savedUsuario.getId());

        // Then
        assertTrue(foundUsuario.isPresent());
        assertEquals("juanperez", foundUsuario.get().getUsername());
    }

    @Test
    public void testFindByUsername() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("mariagarcia");
        usuario.setPassword("pass456");
        entityManager.persist(usuario);

        // When
        Optional<Usuario> foundUsuario = usuarioRepository.findByUsername("mariagarcia");

        // Then
        assertTrue(foundUsuario.isPresent());
        assertEquals("mariagarcia", foundUsuario.get().getUsername());
    }

    @Test
    public void testExistsByUsername() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("carlopez");
        usuario.setPassword("pass789");
        entityManager.persist(usuario);

        // When
        boolean exists = usuarioRepository.existsByUsername("carlopez");

        // Then
        assertTrue(exists);
    }

    @Test
    public void testDeleteUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername("usuarioborrar");
        usuario.setPassword("pass123");
        Usuario savedUsuario = entityManager.persist(usuario);

        // When
        usuarioRepository.deleteById(savedUsuario.getId());
        Optional<Usuario> deletedUsuario = usuarioRepository.findById(savedUsuario.getId());

        // Then
        assertFalse(deletedUsuario.isPresent());
    }
}