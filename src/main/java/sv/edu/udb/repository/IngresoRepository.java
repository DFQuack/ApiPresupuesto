package sv.edu.udb.repository;

import sv.edu.udb.repository.domain.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Usuario;

import java.util.List;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByUsuario_Id(Long id);
}