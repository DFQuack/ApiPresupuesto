package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Presupuesto;

import java.util.List;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    List<Presupuesto> findByUsuario_Id(Long id);
}
