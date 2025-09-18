package sv.edu.udb.controller.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

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
}
