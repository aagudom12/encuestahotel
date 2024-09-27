package com.alfredo.encuestahotel.controller;

import com.alfredo.encuestahotel.entity.Encuesta;
import com.alfredo.encuestahotel.repository.EncuestaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EncuestaController {

    private EncuestaRepository encuestaRepository;

    public EncuestaController(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    @GetMapping("/encuestas")
    public String index(Model model){
        List<Encuesta> encuestas = encuestaRepository.findAll();

        model.addAttribute("encuestas", encuestas);

        return "encuesta-list";
    }

    @GetMapping("/encuestas/new")
    public String newEncuesta(Model model){
        model.addAttribute("encuesta", new Encuesta());
        return "encuesta-new";
    }
}
