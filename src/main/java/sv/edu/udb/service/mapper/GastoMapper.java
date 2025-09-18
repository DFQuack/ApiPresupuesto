package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.repository.domain.Gasto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GastoMapper {
    GastoResponse toGastoResponse(final Gasto gasto);
    List<GastoResponse> toGastoResponseList(final List<Gasto> gastos);
    Gasto toGasto(final GastoRequest gastoRequest);
}
