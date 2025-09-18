package sv.edu.udb.service;

import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;

import java.util.List;

public interface GastoService {
    List<GastoResponse> findAllByUsuario(final Long id);
    GastoResponse findById(final Long id);
    GastoResponse save(final GastoRequest gastoRequest);
    GastoResponse update(final Long id, final GastoRequest gastoRequest);
    void deleteById(final Long id);
}
