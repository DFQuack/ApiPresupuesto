package sv.edu.udb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.service.GastoService;
import sv.edu.udb.service.mapper.GastoMapper;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "gastos")
public class GastoController {
    private final GastoService gastoService;
    private final GastoMapper gastoMapper;

    @GetMapping(path = "usuario/{id}")
    public List<GastoResponse> findAllGastosByUsuario(@PathVariable(name = "id") final Long id) {
        return gastoService.findAllByUsuario(id);
    }

    @GetMapping(path = "{id}")
    public GastoResponse findGastoById(@PathVariable(name = "id") final Long id) {
        return gastoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public GastoResponse saveGasto(@Valid @RequestBody final GastoRequest gastoRequest) {
        return gastoService.save(gastoRequest);
    }

    @PutMapping(path = "{id}")
    public GastoResponse updateGasto(@PathVariable(name = "id") final Long id,
                                     @Valid @RequestBody final GastoRequest gastoRequest) {
        return gastoService.update(id, gastoRequest);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteGasto(@PathVariable(name = "id") final Long id) {
        gastoService.deleteById(id);
    }
}
