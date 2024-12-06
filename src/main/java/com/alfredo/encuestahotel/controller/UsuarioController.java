package com.alfredo.encuestahotel.controller;

import com.alfredo.encuestahotel.entity.Usuario;
import com.alfredo.encuestahotel.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UsuarioController {

    private CustomUserDetailsService usuarioService;

    @Autowired
    public UsuarioController(CustomUserDetailsService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo accesible para administradores
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuario-list";
    }

    @GetMapping("/nuevoUsuario")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-new";
    }

    @PostMapping("/nuevoUsuario")
    public String registerUser(@Valid @ModelAttribute("usuario") Usuario usuario,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "usuario-new"; // Devuelve al formulario con los mensajes de error
        }

        // Verificar si el email ya está en uso
        if (usuarioService.comprobarUsuario(usuario).isPresent()) {
            result.rejectValue("email", "error.usuario", "El email ya está en uso");
            return "usuario-new";
        }

        // Guardar el usuario
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword())); // Cifrado de contraseña
        usuario.setRol("USER");
        usuarioService.guardarUsuario(usuario);

        model.addAttribute("registroExitoso", true);
        return "redirect:/login"; // Redirige al login tras el registro exitoso
    }

    @GetMapping("/usuarios/eliminar/{id}")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo accesible para administradores
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);

        if (usuario.isPresent()) {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }

        return "redirect:/usuarios";
    }


}
