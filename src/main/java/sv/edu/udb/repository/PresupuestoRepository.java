package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
import java.util.Optional;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Optional<Presupuesto> findByUsuario(Usuario usuario);
    Presupuesto findByUsuario_Id(Long id);
}
