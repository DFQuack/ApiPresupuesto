package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.repository.domain.Usuario;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    // Métodos existentes y nuevos
    Gasto findGastoByMes(Month mes);
    List<Gasto> findByUsuario(Usuario usuario);
    List<Gasto> findByUsuario_Id(Long usuarioId);
    Optional<Gasto> findByMesAndUsuario(Month mes, Usuario usuario);
    Optional<Gasto> findByMesAndUsuario_Id(Month mes, Long usuarioId);
}
