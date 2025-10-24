package sv.edu.udb.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class PresupuestoResponse {
    private Long id;
    private BigDecimal gastosBasicos;
    private BigDecimal deudas;
    private BigDecimal otrosGastos;
    private BigDecimal ahorro;
    private Usuario usuario;
    @JsonManagedReference(value = "presupuesto-ingresos")
    private List<Ingreso> ingresos;
}
