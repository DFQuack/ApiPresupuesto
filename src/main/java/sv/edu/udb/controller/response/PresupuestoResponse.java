package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@FieldNameConstants
public class PresupuestoResponse {
    private BigDecimal gastosBasicos, deudas, otrosGastos, ahorro;
    private Usuario usuario;
    private List<Ingreso> ingresos;
}
