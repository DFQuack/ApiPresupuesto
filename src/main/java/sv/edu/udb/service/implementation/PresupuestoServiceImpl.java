package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.controller.request.PresupuestoRequest;
import sv.edu.udb.controller.response.PresupuestoResponse;
import sv.edu.udb.repository.PresupuestoRepository;
import sv.edu.udb.repository.domain.Presupuesto;
import sv.edu.udb.service.PresupuestoService;
import sv.edu.udb.service.mapper.PresupuestoMapper;

@Service
@RequiredArgsConstructor
public class PresupuestoServiceImpl implements PresupuestoService {
    @NonNull
    private final PresupuestoRepository presRepo;
    @NonNull
    private final PresupuestoMapper presMapper;

    @Override
    public PresupuestoResponse findByUsuario(Long id) {
        return presMapper.toPresResponse(presRepo.findByUsuario_Id(id));
    }

    @Override
    public PresupuestoResponse findById(Long id) {
        return presMapper.toPresResponse(presRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id)));
    }

    @Override
    public PresupuestoResponse save(PresupuestoRequest presRequest) {
        final Presupuesto pres = presMapper.toPresupuesto(presRequest);
        return presMapper.toPresResponse(presRepo.save(pres));
    }

    @Override
    public PresupuestoResponse update(Long id, PresupuestoRequest presRequest) {
        final Presupuesto presToUpdate = presRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id));
        presToUpdate.setGastosBasicos(presRequest.getGastosBasicos());
        presToUpdate.setDeudas(presRequest.getDeudas());
        presToUpdate.setOtrosGastos(presRequest.getOtrosGastos());
        presToUpdate.setAhorro(presRequest.getAhorro());

        presRepo.save(presToUpdate);
        return presMapper.toPresResponse(presToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        presRepo.deleteById(id);
    }
}
