package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.UsuarioRequest;
import sv.edu.udb.controller.response.UsuarioResponse;
import sv.edu.udb.service.UsuarioService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping(path = "{id}")
    public UsuarioResponse findById(@PathVariable(name = "id") Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UsuarioResponse saveUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        return usuarioService.save(usuarioRequest);
    }
    @PutMapping(path = "{id}")
    public UsuarioResponse update(@PathVariable(name = "id") final Long id, @Valid @RequestBody final UsuarioRequest usuarioRequest) {
        return usuarioService.update(id, usuarioRequest);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") final Long id) {
        usuarioService.deleteById(id);
    }
}
