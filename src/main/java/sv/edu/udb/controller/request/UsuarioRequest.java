package sv.edu.udb.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class UsuarioRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private List<Ingreso> ingresos;
    private Presupuesto presupuesto;
    private List<Gasto> gastos;
}
