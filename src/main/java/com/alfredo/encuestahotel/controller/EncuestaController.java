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

    //Repositorio con el que accederemos a los datos de la bd y realizaremos las distintas acciones
    //private EncuestaRepository encuestaRepository;

    //Es necesario crearle un constructor para poder inicializarlo y trabajar con él
    //Otra opción, es poner la anotación @Autowired sobre el repositorio
    /*public EncuestaController(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }*/

    //Se define la ruta en la que mostraremos el listado de encuestas
    //Estas se obtendrán mediante un get a las encuestras creadas en la bd de H2
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

        //Con el model, pasamos a la vista los datos obtenidos desde el controlador que a su vez, estos son
        //obtenidos del repositorio
        model.addAttribute("encuestas", encuestas);
        model.addAttribute("numTotalEncuestas", numTotalEncuestas);
        model.addAttribute("promedioEdad", promedioEdad);
        model.addAttribute("distribucionPorcentajes", distribucionPorcentajes);

        return "encuesta-list";
    }

    //Al acceder a esta ruta, se crea un nuevo objeto en blanco
    @GetMapping("/encuestas/new")
    public String newEncuesta(Model model){
        model.addAttribute("encuesta", new Encuesta());
        return "encuesta-new";
    }

    //Y según lo que escriba el huesped, este objeto encuesta se guarda en el repositorio,
    // siguiendo unas validaciones para comprobar que la encuesta es válida
    @PostMapping("/encuestas/new")
    public String newEncuestaInsertar(@Valid Encuesta encuesta, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "encuesta-new";
        }

        // Obtener el usuario autenticado
        String emailUsuario = principal.getName();
        Usuario usuario = usuarioService.comprobarUsuarioPorEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar el usuario al anuncio
        encuesta.setUsuario(usuario);
        usuario.getEncuestas().add(encuesta);

        encuestaService.guardarEncuesta(encuesta);

        return "encuesta-completed";
    }

    //Desde esta ruta, obtendremos los valores de la encuesta según el id y si este existe en la bd, lo visualizaremos
    @GetMapping("/encuestas/view/{id}")
    public String view(@PathVariable Long id, Model model ){
        Optional<Encuesta> encuesta = encuestaService.obtenerEncuestaPorId(id);

        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-view";
        }
        return "redirect:/encuestas";
    }

    //Al pulsar el botón correspondiente con esta ruta, si existe el objeto en la bd, será eliminado
    @GetMapping("encuestas/del/{id}")
    public String delete(@PathVariable Long id, Model model){
        Optional<Encuesta> encuesta = encuestaService.obtenerEncuestaPorId(id);
        if(encuesta.isPresent()){
            encuestaService.eliminarEncuesta(id);

        }
        return "redirect:/encuestas";
    }

    //Muy parecido al new, solo que al acceder a una encuesta ya existente, en vex de crearla, obtendrá
    //los datos desde la bd y los mostrará si existe la encuesta.
    @GetMapping("/encuestas/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<Encuesta> encuesta = encuestaService.obtenerEncuestaPorId(id);
        if(encuesta.isPresent()){
            model.addAttribute("encuesta", encuesta.get());
            return "encuesta-edit";
        }
        return "redirect:/encuestas";
    }

    //Funcionamiento muy parecido al de cuando se guarda la nueva encuesta
    @PostMapping("/encuestas/edit/{id}")
    public String confirmarEdit(@Valid Encuesta encuesta, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "encuesta-edit";
        }
        encuestaService.guardarEncuesta(encuesta);
        return "redirect:/encuestas";
    }


    //Metodo para mostrar todas las encuestas o filtrar por nivel de satisfacción
    @GetMapping("encuestas/filter")
    public String filtrarEncuestas(@RequestParam(required = false) Integer nivelSatisfaccion, Model model) {
        List<Encuesta> encuestas;

        // Si el nivel de satisfacción está presente, se filtran las encuestas
        if (nivelSatisfaccion != null && nivelSatisfaccion > 0) {
            encuestas = encuestaService.obtenerEncuestasPorNivelSatisfaccion(nivelSatisfaccion);
        } else {
            // Si no hay filtro, se muestran todas las encuestas
            encuestas = encuestaService.obtenerEncuestas();
        }

        //He creado otra vez las estadisticas dentro de este controlador, ya que también me interesa
        //saberlas dentro de cada nivel de satisfacción. Aunque la distribución de porcentajes no tenga
        //sentido ya que siempre va a ser del 100% dentro del filtro
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

        return "encuesta-propias"; // vista para los anuncios del usuario
    }
}
