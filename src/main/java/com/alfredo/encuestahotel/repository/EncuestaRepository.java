package com.alfredo.encuestahotel.repository;

import com.alfredo.encuestahotel.entity.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta,Long> {

}
