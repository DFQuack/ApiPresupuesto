package sv.edu.udb.Repositories;

import sv.edu.udb.Entities.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByIdUsuario(Long idUsuario);
    List<Ingreso> findByIdUsuarioAndIngresoFormal(Long idUsuario, Boolean ingresoFormal);
}