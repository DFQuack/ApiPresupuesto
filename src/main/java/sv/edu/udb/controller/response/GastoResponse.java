package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Setter
@Builder(toBuilder = true)
@FieldNameConstants
public class GastoResponse {
    private Long id;
    private Month mes;
    private BigDecimal gastosBasicos, deudas, otrosGastos, ahorro;
    private Usuario usuario;
}