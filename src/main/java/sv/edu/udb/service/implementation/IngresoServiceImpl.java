package sv.edu.udb.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.edu.udb.controller.request.IngresoRequest;
import sv.edu.udb.controller.response.IngresoResponse;
import sv.edu.udb.repository.IngresoRepository;
import sv.edu.udb.repository.domain.Ingreso;
import sv.edu.udb.service.IngresoService;
import sv.edu.udb.service.mapper.IngresoMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngresoServiceImpl implements IngresoService {
    @NonNull
    private final IngresoRepository ingresoRepo;
    @NonNull
    private final IngresoMapper ingresoMapper;

    @Override
    public List<IngresoResponse> findAllByUsuario(Long id) {
        return ingresoMapper.toIngresoResponseList(ingresoRepo.findByUsuario_Id(id));
    }

    @Override
    public IngresoResponse findById(Long id) {
        return ingresoMapper.toIngresoResponse(ingresoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id)));
    }

    @Override
    public IngresoResponse save(IngresoRequest ingresoRequest) {
        final Ingreso ingreso = ingresoMapper.toIngreso(ingresoRequest);
        return ingresoMapper.toIngresoResponse(ingresoRepo.save(ingreso));
    }

    @Override
    public IngresoResponse update(Long id, IngresoRequest ingresoRequest) {
        final Ingreso ingresoToUpdate = ingresoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado: " + id));
        ingresoToUpdate.setNombre(ingresoRequest.getNombre());
        ingresoToUpdate.setSueldo(ingresoRequest.getSueldo());
        ingresoToUpdate.setIngresoFormal(ingresoRequest.getIngresoFormal());
        ingresoToUpdate.setRetencionAFP(ingresoRequest.getRetencionAFP());
        ingresoToUpdate.setRetencionISSS(ingresoRequest.getRetencionISSS());
        ingresoToUpdate.setRetencionRenta(ingresoRequest.getRetencionRenta());
        ingresoToUpdate.setSueldoNeto(ingresoRequest.getSueldoNeto());

        ingresoRepo.save(ingresoToUpdate);
        return ingresoMapper.toIngresoResponse(ingresoToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        ingresoRepo.deleteById(id);
    }
}
