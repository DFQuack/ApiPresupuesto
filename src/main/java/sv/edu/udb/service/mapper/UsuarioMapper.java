package sv.edu.udb.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.repository.domain.Usuario;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponse toUsuarioResponse(final Usuario usuario);
    List<UsuarioResponse> toUsuarioResponseList(final List<Usuario> usuarios);
    Usuario toUsuario(final UsuarioRequest usuarioRequest);
}
