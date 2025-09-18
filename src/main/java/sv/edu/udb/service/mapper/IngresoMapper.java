package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;
import sv.edu.udb.repository.domain.Ingreso;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngresoMapper {
    IngresoResponse toIngresoResponse(final Ingreso ingreso);
    List<IngresoResponse> toIngresoResponseList(final List<Ingreso> ingresoList);
    Ingreso toIngreso(final IngresoRequest ingresoRequest);
}
