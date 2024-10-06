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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EncuestaController {

    private EncuestaRepository encuestaRepository;

    public EncuestaController(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    @GetMapping("/encuestas")
    public String index(Model model){
        List<Encuesta> encuestas = encuestaRepository.findAll();
        long numTotalEncuestas = encuestaRepository.count();
        double promedioEdad = encuestas.stream()
                .mapToInt(Encuesta::getEdad)
                .average()
                .orElse(0.0);

        Map<String, Long> distribucion = encuestas.stream()
                .collect(Collectors.groupingBy(encuesta -> {
                    switch (encuesta.getNivelSatisfaccion()) {
                        case 1: return "1 - Muy satisfecho";
                        case 2: return "2 - Satisfecho";
                        case 3: return "3 - Neutral";
                        case 4: return "4 - Insatisfecho";
                        case 5: return "5 - Muy insatisfecho";
                        default: return "Desconocido";
                    }
                }, Collectors.counting()));

        /*Map<String, Long> distribucion = encuestas.stream()
                .collect(Collectors.groupingBy(Encuesta::getNivelSatisfaccion, Collectors.counting()));*/

        long totalEncuestas = encuestas.size();
        Map<String, Double> distribucionPorcentajes = distribucion.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (entry.getValue() * 100.0) / totalEncuestas
                ));

        model.addAttribute("encuestas", encuestas);
        model.addAttribute("numTotalEncuestas", numTotalEncuestas);
        model.addAttribute("promedioEdad", promedioEdad);
        model.addAttribute("distribucionPorcentajes", distribucionPorcentajes);

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

    //Metodo para mostrar todas las encuestas o filtrar por nivel de satisfacción
    @GetMapping("encuestas/filter")
    public String filtrarEncuestas(@RequestParam(required = false) Integer nivelSatisfaccion, Model model) {
        List<Encuesta> encuestas;

        // Si el nivel de satisfacción está presente, se filtran las encuestas
        if (nivelSatisfaccion != null && nivelSatisfaccion > 0) {
            encuestas = encuestaRepository.findByNivelSatisfaccion(nivelSatisfaccion);
        } else {
            // Si no hay filtro, se muestran todas las encuestas
            encuestas = encuestaRepository.findAll();
        }

        long numTotalEncuestas = encuestaRepository.count();
        double promedioEdad = encuestas.stream()
                .mapToInt(Encuesta::getEdad)
                .average()
                .orElse(0.0);

        Map<String, Long> distribucion = encuestas.stream()
                .collect(Collectors.groupingBy(encuesta -> {
                    switch (encuesta.getNivelSatisfaccion()) {
                        case 1: return "1 - Muy satisfecho";
                        case 2: return "2 - Satisfecho";
                        case 3: return "3 - Neutral";
                        case 4: return "4 - Insatisfecho";
                        case 5: return "5 - Muy insatisfecho";
                        default: return "Desconocido";
                    }
                }, Collectors.counting()));

        long totalEncuestas = encuestas.size();
        Map<String, Double> distribucionPorcentajes = distribucion.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (entry.getValue() * 100.0) / totalEncuestas
                ));

        model.addAttribute("encuestas", encuestas);
        model.addAttribute("numTotalEncuestas", numTotalEncuestas);
        model.addAttribute("promedioEdad", promedioEdad);
        model.addAttribute("distribucionPorcentajes", distribucionPorcentajes);

        return "encuesta-list";  // La misma vista que muestras la lista de encuestas
    }
}
