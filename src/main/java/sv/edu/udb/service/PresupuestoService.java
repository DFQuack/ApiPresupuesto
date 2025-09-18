package sv.edu.udb.service;

import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;

public interface PresupuestoService {
    PresupuestoResponse findByUsuario(final Long id);
    PresupuestoResponse findById(final Long id);
    PresupuestoResponse save(final PresupuestoRequest presRequest);
    PresupuestoResponse update(final Long id, final PresupuestoRequest presRequest);
    void deleteById(final Long id);
}
