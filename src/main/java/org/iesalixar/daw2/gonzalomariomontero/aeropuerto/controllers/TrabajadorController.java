package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Trabajador;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.TrabajadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;
import java.util.Optional;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Trabajador`.
 */
@Controller
@RequestMapping("/trabajadores")
public class TrabajadorController {

    private static final Logger logger = LoggerFactory.getLogger(TrabajadorController.class);

    // DAO para gestionar las operaciones de los trabajadores en la base de datos.
    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los trabajadores y los pasa como atributo al modelo para que sean
     * accesibles en la vista `trabajador.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de trabajadores.
     */
    @GetMapping()
    public String listTrabajadores(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los trabajadores..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Trabajador> trabajador;
        int totalPages = 0;
        trabajador = trabajadorRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) trabajadorRepository.count() / 5);
        logger.info("Se han cargado {} trabajadores.", trabajador.toList().size());
        model.addAttribute("listTrabajadores", trabajador.toList()); // Pasar la lista de trabajadores al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/trabajador/trabajador"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear un nuevo trabajador.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo trabajador.");
        model.addAttribute("trabajador", new Trabajador());
        model.addAttribute("aviones", avionRepository.findAll()); // Agregar lista de aviones para elegir
        return "pages/trabajador/trabajador-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el trabajador con ID {}", id);
        Optional<Trabajador> trabajadorOpt = trabajadorRepository.findById(id);
        if (!trabajadorOpt.isPresent()) {
            logger.warn("No se encontró el trabajador con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró el trabajador.");
        } else {
            model.addAttribute("trabajador", trabajadorOpt.get());
        }
        model.addAttribute("aviones", avionRepository.findAll()); // Agregar lista de aviones para elegir
        return "pages/trabajador/trabajador-form";
    }

    /**
     * Inserta un nuevo trabajador en la base de datos.
     *
     * @param trabajador Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de trabajadores.
     */
    @PostMapping("/insert")
    public String insertTrabajador(@Valid @ModelAttribute("trabajador") Trabajador trabajador,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Locale locale) {
        logger.info("Insertando nuevo trabajador con nombre {}", trabajador.getNombre());

        if (result.hasErrors()) {
            model.addAttribute("aviones", avionRepository.findAll());
            return "pages/trabajador/trabajador-form";
        }

        trabajadorRepository.save(trabajador);
        logger.info("Trabajador {} insertado con éxito.", trabajador.getNombre());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.trabajador.insert.success", null, locale));
        return "redirect:/trabajadores";
    }

    /**
     * Actualiza un trabajador existente en la base de datos.
     *
     * @param trabajador Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de directores.
     */
    @PostMapping("/update")
    public String updateTrabajador(@Valid @ModelAttribute("trabajador") Trabajador trabajador,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Locale locale) {
        logger.info("Actualizando trabajador con ID {}", trabajador.getId());

        if (result.hasErrors()) {
            model.addAttribute("aviones", avionRepository.findAll());
            return "pages/trabajador/trabajador-form";
        }

        trabajadorRepository.save(trabajador);
        logger.info("Trabajador con ID {} actualizado con éxito.", trabajador.getId());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.trabajador.update.success", null, locale));
        return "redirect:/trabajadores";
    }

    /**
     * Elimina un trabajador de la base de datos.
     *
     * @param id ID del trabajador a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de trabajadores.
     */
    @PostMapping("/delete")
    public String deleteTrabajador(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando trabajador con ID {}", id);
        trabajadorRepository.deleteById(id);
        logger.info("Trabajador con ID {} eliminado con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.trabajador.delete.success", null, locale));
        return "redirect:/trabajadores"; // Redirigir a la lista de trabajadores
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "nombreAsc" -> Sort.by("nombre").ascending();
            case "nombreDesc" -> Sort.by("nombre").descending();
            case "apellidosAsc" -> Sort.by("apellidos").ascending();
            case "apellidosDesc" -> Sort.by("apellidos").descending();
            case "cargoAsc" -> Sort.by("cargo").ascending();
            case "cargoDesc" -> Sort.by("cargo").descending();
            case "fechaContratacionAsc" -> Sort.by("fechaContratacion").ascending();
            case "fechaContratacionDesc" -> Sort.by("fechaContratacion").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}

