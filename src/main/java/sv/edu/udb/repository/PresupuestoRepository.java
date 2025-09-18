package sv.edu.udb.repository;

import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Optional<Presupuesto> findByUsuario(Usuario usuario);
    Optional<Presupuesto> findByUsuario_Id(Long usuarioId);
}