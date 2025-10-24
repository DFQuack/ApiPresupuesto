package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;
import sv.edu.udb.service.PresupuestoService;
import sv.edu.udb.service.mapper.PresupuestoMapper;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/presupuestos")
public class PresupuestoController {
    private final PresupuestoService presService;

    @GetMapping(path = "usuario/{id}")
    public PresupuestoResponse findPresByUsuario(@PathVariable final Long id) {
        return presService.findByUsuario(id);
    }

    @GetMapping(path = "{id}")
    public PresupuestoResponse findPresById(@PathVariable final Long id) {
        return presService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public PresupuestoResponse savePresupuesto(@Valid @RequestBody final PresupuestoRequest presRequest) {
        return presService.save(presRequest);
    }

    @PutMapping(path = "{id}")
    public PresupuestoResponse updatePresupuesto(@PathVariable final Long id, @Valid @RequestBody final PresupuestoRequest presRequest) {
        return presService.update(id, presRequest);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletePresupuesto(@PathVariable final Long id) {
        presService.deleteById(id);
    }
}
