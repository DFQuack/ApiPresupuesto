package sv.edu.udb.service;

import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponse> findAll();
    UsuarioResponse findById(Long id);
    UsuarioResponse save(UsuarioRequest usuarioRequest);
    UsuarioResponse update(final Long id, final UsuarioRequest usuarioRequest);
    void deleteById(final Long id);
}
