package sv.edu.udb.service;

import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;

import java.util.List;

public interface IngresoService {
    List<IngresoResponse> findAllByIdUsuario(final Long id);
    IngresoResponse findById(final Long id);
    IngresoResponse save(final IngresoRequest ingresoRequest);
    IngresoResponse update(final Long id, final IngresoRequest ingresoRequest);
    void deleteById(final Long id);
}
