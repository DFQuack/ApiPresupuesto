package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;
import sv.edu.udb.service.IngresoService;
import sv.edu.udb.service.mapper.IngresoMapper;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "ingresos")
public class IngresoController {
    private final IngresoService ingresoService;
    private final IngresoMapper ingresoMapper;

    @GetMapping(path = "usuario/{id}")
    public List<IngresoResponse> findAllIngresosByUsuarioId(@PathVariable(name = "id") final Long id) {
        return ingresoService.findAllByUsuario(id);
    }

    @GetMapping(path = "{id}")
    public IngresoResponse findIngresoById(@PathVariable(name = "id") final Long id) {
        return ingresoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public IngresoResponse saveIngreso(@Valid @RequestBody final IngresoRequest ingresoRequest) {
        return ingresoService.save(ingresoRequest);
    }

    @PutMapping(path = "{id}")
    public IngresoResponse updateIngreso(@PathVariable final Long id,
                                         @Valid @RequestBody final IngresoRequest ingresoRequest) {
        return ingresoService.update(id, ingresoRequest);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteIngreso(@PathVariable(name = "id") final Long id) {
        ingresoService.deleteById(id);
    }
}
