package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Pasajero;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.PasajeroRepository;
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
import java.util.Locale;

import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Pasajero`.
 */
@Controller
@RequestMapping("/pasajeros")
public class PasajeroController {

    private static final Logger logger = LoggerFactory.getLogger(PasajeroController.class);

    // DAO para gestionar las operaciones de los pasajeros en la base de datos
    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los pasajeros y los pasa como atributo al modelo para que sean
     * accesibles en la vista `pasajero.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de pasajeros.
     */
    @GetMapping()
    public String listPasajeros(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los pasajeros..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Pasajero> pasajeros;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            pasajeros = pasajeroRepository.findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(search, search, pageable);
            totalPages = (int) Math.ceil((double) pasajeroRepository.countByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(search, search) / 5);
        } else {
            pasajeros = pasajeroRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) pasajeroRepository.count() / 5);
        }
        logger.info("Se han cargado {} pasajeros.", pasajeros.toList().size());
        model.addAttribute("listPasajeros", pasajeros.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/pasajero/pasajero";
    }

    /**
     * Muestra el formulario para crear un nuevo pasajero.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo pasajero.");
        model.addAttribute("pasajero", new Pasajero());
        model.addAttribute("aviones", avionRepository.findAll());
        return "pages/pasajero/pasajero-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el pasajero con ID {}", id);
        Optional<Pasajero> pasajeroOpt = pasajeroRepository.findById(id);
        if (!pasajeroOpt.isPresent()) {
            logger.warn("No se encontró el pasajero con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró el pasajero.");
        } else {
            model.addAttribute("pasajero", pasajeroOpt.get());
        }
        model.addAttribute("aviones", avionRepository.findAll());
        return "pages/pasajero/pasajero-form";
    }

    /**
     * Inserta un nuevo pasajero en la base de datos.
     *
     * @param pasajero Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de pasajeros.
     */
    @PostMapping("/insert")
    public String insertPasajero(@Valid @ModelAttribute("pasajero") Pasajero pasajero,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Locale locale) {
        logger.info("Insertando nuevo pasajero con nombre {}", pasajero.getNombre());

        if (result.hasErrors()) {
            model.addAttribute("aviones", avionRepository.findAll());
            return "pages/pasajero/pasajero-form";
        }

        pasajeroRepository.save(pasajero);
        logger.info("Pasajero {} insertado con éxito.", pasajero.getNombre());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.pasajero.insert.success", null, locale));
        return "redirect:/pasajeros";
    }

    /**
     * Actualiza un pasajero existente en la base de datos.
     *
     * @param pasajero Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de pasajeros.
     */
    @PostMapping("/update")
    public String updatePasajero(@Valid @ModelAttribute("pasajero") Pasajero pasajero,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 Locale locale) {
        logger.info("Actualizando pasajero con ID {}", pasajero.getId());

        if (result.hasErrors()) {
            model.addAttribute("aviones", avionRepository.findAll());
            return "pages/pasajero/pasajero-form";
        }

        pasajeroRepository.save(pasajero);
        logger.info("Pasajero con ID {} actualizado con éxito.", pasajero.getId());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.pasajero.update.success", null, locale));
        return "redirect:/pasajeros";
    }

    /**
     * Elimina un pasajero de la base de datos.
     *
     * @param id ID del pasajero a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de pasajeros.
     */
    @PostMapping("/delete")
    public String deletePasajero(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando pasajero con ID {}", id);
        pasajeroRepository.deleteById(id);
        logger.info("Pasajero con ID {} eliminado con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.pasajero.delete.success", null, locale));
        return "redirect:/pasajeros"; // Redirigir a la lista de pasajeros
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
            case "documentoAsc" -> Sort.by("documento").ascending();
            case "documentoDesc" -> Sort.by("documento").descending();
            case "emailAsc" -> Sort.by("email").ascending();
            case "emailDesc" -> Sort.by("email").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}

