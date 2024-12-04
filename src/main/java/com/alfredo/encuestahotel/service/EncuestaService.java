package com.alfredo.encuestahotel.service;

import com.alfredo.encuestahotel.entity.Encuesta;
import com.alfredo.encuestahotel.entity.Usuario;
import com.alfredo.encuestahotel.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncuestaService {
    @Autowired
    private EncuestaRepository encuestaRepository;

    public List<Encuesta> obtenerAnunciosPorUsuario(Usuario usuario) {
        return encuestaRepository.findByUsuario(usuario);
    }

    public List<Encuesta> obtenerEncuestas() {
        return encuestaRepository.findAll();
    }

    public long obtenerCantidadEncuestas() {
        return encuestaRepository.count();
    }

    public void guardarEncuesta(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    public Optional<Encuesta> obtenerEncuestaPorId(Long id) {
        return encuestaRepository.findById(id);
    }

    public void eliminarEncuesta(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        encuestaRepository.deleteById(id);
    }

    public List<Encuesta> obtenerEncuestasPorNivelSatisfaccion(Integer nivelSatisfaccion) {
        return encuestaRepository.findByNivelSatisfaccion(nivelSatisfaccion);
    }
}
