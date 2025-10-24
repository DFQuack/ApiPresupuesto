package sv.edu.udb.controller.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Presupuesto;

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
public class UsuarioResponse {
    private Long id;
    private String username, password;
    private Presupuesto presupuesto;
    @JsonManagedReference(value = "usuario-ingresos")
    private List<Ingreso> ingresos;
    @JsonManagedReference(value = "usuario-gastos")
    private List<Gasto> gastos;
}
