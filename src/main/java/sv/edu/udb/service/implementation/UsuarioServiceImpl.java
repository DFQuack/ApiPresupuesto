package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.repository.UsuarioRepository;
import sv.edu.udb.repository.domain.Usuario;
import sv.edu.udb.service.UsuarioService;
import sv.edu.udb.service.mapper.UsuarioMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    @NonNull
    private final UsuarioRepository usuarioRepo;
    @NonNull
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<UsuarioResponse> findAll() {
        return usuarioMapper.toUsuarioResponseList(usuarioRepo.findAll());
    }

    @Override
    public UsuarioResponse findById(Long id) {
        return usuarioMapper.toUsuarioResponse(usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id)));
    }

    @Override
    public UsuarioResponse save(UsuarioRequest usuarioRequest) {
        final Usuario usuario = usuarioMapper.toUsuario(usuarioRequest);
        return usuarioMapper.toUsuarioResponse(usuarioRepo.save(usuario));
    }

    @Override
    public UsuarioResponse update(Long id, UsuarioRequest usuarioRequest) {
        final Usuario usuarioToUpdate = usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id));
        usuarioToUpdate.setUsername(usuarioRequest.getUsername());
        usuarioToUpdate.setPassword(usuarioRequest.getPassword());

        usuarioRepo.save(usuarioToUpdate);
        return usuarioMapper.toUsuarioResponse(usuarioToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepo.deleteById(id);
    }
}
