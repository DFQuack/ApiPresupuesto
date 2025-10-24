package sv.edu.udb.controller.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class IngresoResponse {
    private Long id;
    private String nombre;
    private BigDecimal sueldo;
    private Boolean ingresoFormal;

    @JsonBackReference(value = "usuario-ingresos")
    private Usuario usuario;
    @JsonBackReference(value = "presupuesto-ingresos")
    private Presupuesto presupuesto;
}