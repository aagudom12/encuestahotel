package com.alfredo.encuestahotel.repository;

import com.alfredo.encuestahotel.entity.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta,Long> {
    // Encuestas filtradas por nivel de satisfacción
    List<Encuesta> findByNivelSatisfaccion(Integer nivelSatisfaccion);

}
