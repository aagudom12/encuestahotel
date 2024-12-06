package com.alfredo.encuestahotel.controller;

import com.alfredo.encuestahotel.entity.Encuesta;
import com.alfredo.encuestahotel.entity.Usuario;
import com.alfredo.encuestahotel.repository.EncuestaRepository;
import com.alfredo.encuestahotel.service.CustomUserDetailsService;
import com.alfredo.encuestahotel.service.EncuestaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EncuestaController {

    private EncuestaService encuestaService;
    private CustomUserDetailsService usuarioService;

    @Autowired
    public EncuestaController(EncuestaService encuestaService, CustomUserDetailsService usuarioService) {
        this.encuestaService = encuestaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/encuestas")
    public String index(Model model){
        List<Encuesta> encuestas = encuestaService.obtenerEncuestas();

        //Creamos una variable para al almacenar el nº de encuestas existentes en la bd y luego mostrarlo en las stats
        long numTotalEncuestas = encuestaService.obtenerCantidadEncuestas();

        //Creamos otra variable para calcular el promedio de edad de los huespedes que han realizado encuestas
        /*Con stream permitimos que procese el listado de encuestas para realizar operaciones secuenciales
        * y paralelas, permitiendonos acceder a métodos como mapToInt, que se encarga de transformar cada
        * una de las encuestas(objetos) existentes en un int y obteniendo la edad mediante la referencia
        * al metodo getEdad. Con estas edades realiza el calculo del promedio mediante el metodo average().
        * Debido a que average() devuelve un OptionalDouble y existe la posibilidad de que no haya ninguna
        * edad para realizar el promedio en el caso de que no haya ninguna encuesta realizada, se devolvería
        * el valor 0.0 por defecto*/
        double promedioEdad = encuestas.stream()
                .mapToInt(Encuesta::getEdad)
                .average()
                .orElse(0.0);

        /*Mediante stream y su metodo collect, recopilamos los elementos en una estructura de datos,
        * estos son guardados en un Map, donde String representa las categorías de satisfacción, y el Long
        * guarda el nº de encuestas que pertenecen a esa categoría.
        * Mediante un lambda y el Collectors.groupingBy, obtiene el nivel de satisfacción analizando
        * cada una de las encuestas y guardandolo según el valor del int mediante un switch*/
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
        //De esta manera, en el Map, se habrá guardado el nº de valores(Long)
        //para cada categoría del nivel de satisfaccion (String)

        long totalEncuestas = encuestas.size();
        //Convierte el Map obtenido anteriormente en otro Map que usa pares clave-valor
        //De esta forma, calcula el porcentaje de cada nivel de satisfacción
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
    public String newEncuestaInsertar(@Valid Encuesta encuesta, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "encuesta-new";
        }

        String emailUsuario = principal.getName();
        Usuario usuario = usuarioService.comprobarUsuarioPorEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        encuesta.setUsuario(usuario);
        usuario.getEncuestas().add(encuesta);

        encuestaService.guardarEncuesta(encuesta);

        return "encuesta-completed";
    }

    @GetMapping("/encuestas/view/{id}")
    public String view(@PathVariable Long id, Model model ){
        Optional<Encuesta> encuesta = encuestaService.obtenerEncuestaPorId(id);

        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-view";
        }
        return "redirect:/encuestas";
    }

    @GetMapping("/encuestas/del/{id}")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes){

        String emailUsuario = principal.getName();
        Usuario usuario = usuarioService.comprobarUsuarioPorEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Encuesta encuesta = encuestaService.obtenerEncuestaPorId(id)
                .orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));

        if (!encuesta.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta encuesta.");
            return "redirect:/misEncuestas";
        }

        encuestaService.eliminarEncuesta(id);
        redirectAttributes.addFlashAttribute("success", "Encuesta eliminada exitosamente.");
        return "redirect:/encuestas";
    }

    @GetMapping("/encuestas/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<Encuesta> encuesta = encuestaService.obtenerEncuestaPorId(id);
        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-edit";
        }
        return "redirect:/encuestas";
    }

    @PostMapping("/encuestas/edit/{id}")
    public String confirmarEdit(@PathVariable Long id, @Valid Encuesta encuesta, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "encuesta-edit";
        }

        Encuesta encuestaOriginal = encuestaService.obtenerEncuestaPorId(id)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        String emailUsuario = principal.getName();
        if (!encuestaOriginal.getUsuario().getEmail().equals(emailUsuario)) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta encuesta.");
            return "redirect:/misEncuestas";
        }

        encuesta.setUsuario(encuestaOriginal.getUsuario());

        encuestaService.guardarEncuesta(encuesta);
        redirectAttributes.addFlashAttribute("success", "Encuesta editada exitosamente.");
        return "redirect:/encuestas";
    }

    @GetMapping("/encuestas/filter")
    public String filtrarEncuestas(@RequestParam(required = false) Integer nivelSatisfaccion, Model model) {
        List<Encuesta> encuestas;

        if (nivelSatisfaccion != null && nivelSatisfaccion > 0) {
            encuestas = encuestaService.obtenerEncuestasPorNivelSatisfaccion(nivelSatisfaccion);
        } else {
            encuestas = encuestaService.obtenerEncuestas();
        }


        long numTotalEncuestas = encuestaService.obtenerCantidadEncuestas();
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

        return "encuesta-list";
    }

    @GetMapping("/misEncuestas")
    public String verMisAnuncios(Model model, Principal principal) {
        String emailUsuario = principal.getName();
        Usuario usuario = usuarioService.comprobarUsuarioPorEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Encuesta> encuestas = encuestaService.obtenerAnunciosPorUsuario(usuario);
        model.addAttribute("encuestas", encuestas);

        return "encuesta-propias";
    }
}
