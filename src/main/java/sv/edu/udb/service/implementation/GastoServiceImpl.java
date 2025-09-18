package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.controller.request.GastoRequest;
import sv.edu.udb.controller.response.GastoResponse;
import sv.edu.udb.repository.GastoRepository;
import sv.edu.udb.repository.domain.Gasto;
import sv.edu.udb.service.GastoService;
import sv.edu.udb.service.mapper.GastoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {
    @NonNull
    private final GastoRepository gastoRepo;
    @NonNull
    private final GastoMapper gastoMapper;

    @Override
    public List<GastoResponse> findAllByUsuario(Long id) {
        return gastoMapper.toGastoResponseList(gastoRepo.findByUsuario_Id(id));
    }

    @Override
    public GastoResponse findById(Long id) {
        return gastoMapper.toGastoResponse(gastoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id)));
    }

    @Override
    public GastoResponse save(GastoRequest gastoRequest) {
        final Gasto gasto = gastoMapper.toGasto(gastoRequest);
        return gastoMapper.toGastoResponse(gastoRepo.save(gasto));
    }

    @Override
    public GastoResponse update(Long id, GastoRequest gastoRequest) {
        final Gasto gastoToUpdate = gastoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id));
        gastoToUpdate.setGastosBasicos(gastoRequest.getGastosBasicos());
        gastoToUpdate.setDeudas(gastoRequest.getDeudas());
        gastoToUpdate.setOtrosGastos(gastoRequest.getOtrosGastos());
        gastoToUpdate.setAhorro(gastoRequest.getAhorro());

        gastoRepo.save(gastoToUpdate);
        return gastoMapper.toGastoResponse(gastoToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        gastoRepo.deleteById(id);
    }
}
