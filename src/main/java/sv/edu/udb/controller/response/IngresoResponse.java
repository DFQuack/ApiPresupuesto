package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(toBuilder = true)
@FieldNameConstants
public class IngresoResponse {
    private String nombre;
    private BigDecimal sueldo;
    private Boolean ingresoFormal;
    private BigDecimal retencionAFP, retencionISSS, retencionRenta, sueldoNeto;
    private Usuario usuario;
    private Presupuesto presupuesto;
}
