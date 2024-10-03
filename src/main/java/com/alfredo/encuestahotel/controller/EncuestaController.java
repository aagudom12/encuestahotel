package com.alfredo.encuestahotel.controller;

import com.alfredo.encuestahotel.entity.Encuesta;
import com.alfredo.encuestahotel.repository.EncuestaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/encuestas/new")
    public String newEncuestaInsertar(@Valid Encuesta encuesta, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "encuesta-new";
        }

        encuestaRepository.save(encuesta);

        return "encuesta-completed";
    }

    @GetMapping("/encuestas/view/{id}")
    public String view(@PathVariable Long id, Model model ){
        Optional<Encuesta> encuesta = encuestaRepository.findById(id);

        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-view";
        }
        return "redirect:/encuestas";
    }

    @GetMapping("encuestas/del/{id}")
    public String delete(@PathVariable Long id, Model model){
        Optional<Encuesta> encuesta = encuestaRepository.findById(id);
        if(encuesta.isPresent()){
            encuestaRepository.delete(encuesta.get());

        }
        return "redirect:/encuestas";
    }

    @GetMapping("/encuestas/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<Encuesta> encuesta = encuestaRepository.findById(id);
        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-edit";
        }
        return "redirect:/encuestas";
    }

    @PostMapping("/encuestas/edit/{id}")
    public String confirmarEdit(@Valid Encuesta encuesta, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "encuesta-edit";
        }
        encuestaRepository.save(encuesta);
        return "redirect:/encuestas";
    }
}
