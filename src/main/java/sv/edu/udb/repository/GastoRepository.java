package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Gasto;

import java.time.Month;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    Gasto findGastoByMes(Month mes);
}
