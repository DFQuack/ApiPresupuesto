package sv.edu.udb.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.repository.domain.Presupuesto;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@FieldNameConstants
public class UsuarioResponse {
    private String username, password;
    private List<Ingreso> ingresos;
    private Presupuesto presupuesto;
    private List<Gasto> gastos;
}
