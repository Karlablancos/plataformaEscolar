package com.colegio.establecimiento.service;

import com.colegio.establecimiento.model.Establecimiento;
import com.colegio.establecimiento.repository.EstablecimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;

    public Optional<Establecimiento> buscarPorRbd(String rbd) {
        return establecimientoRepository.findByRbd(rbd);
    }

    public boolean existeRbd(String rbd) {
        return establecimientoRepository.findByRbd(rbd).isPresent();
    }

}