package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;
import sv.edu.udb.repository.domain.Presupuesto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PresupuestoMapper {
    PresupuestoResponse toPresResponse(final Presupuesto pres);
    Presupuesto toPresupuesto(final PresupuestoRequest presRequest);
}
