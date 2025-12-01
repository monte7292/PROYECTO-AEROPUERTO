package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Director;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AeropuertoRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.DirectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Director`.
 */
@Controller
@RequestMapping("/directores")
public class DirectorController {

    private static final Logger logger = LoggerFactory.getLogger(DirectorController.class);

    // DAO para gestionar las operaciones de los directores en la base de datos
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    /**
     * Lista todos los directores y los pasa como atributo al modelo para que sean
     * accesibles en la vista `director.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de directores.
     */
    @GetMapping()
    public String listDirectores(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model) {
        logger.info("Solicitando la lista de todos los directores..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Director> directores;
        int totalPages = 0;
        directores = directorRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) directorRepository.count() / 5);
        logger.info("Se han cargado {} directores.", directores.toList().size());
        model.addAttribute("listDirectores", directores.toList()); // Pasar la lista de directores al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "director"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear un nuevo director.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo director.");
        model.addAttribute("director", new Director());
        model.addAttribute("aeropuerto", aeropuertoRepository.findAll()); // Agregar lista de aeropuertos para elegir
        return "director-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el director con ID {}", id);
        Optional<Director> directorOpt = directorRepository.findById(id);
        if (!directorOpt.isPresent()) {
            logger.warn("No se encontró el director con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró el director.");
        } else {
            model.addAttribute("director", directorOpt);
        }
        model.addAttribute("director", directorOpt.get());
        model.addAttribute("aeropuertos", aeropuertoRepository.findAll()); // Agregar lista de aeropuertos para elegir
        return "director-form";
    }

    /**
     * Inserta un nuevo director en la base de datos.
     *
     * @param director Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de directores.
     */
    @PostMapping("/insert")
    public String insertDirector(@ModelAttribute("director") Director director, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nuevo director con nombre {}", director.getNombre());

        directorRepository.save(director);
        logger.info("Director {} insertado con éxito.", director.getNombre());
        return "redirect:/directores"; // Redirigir a la lista de directores
    }

    /**
     * Actualiza un director existente en la base de datos.
     *
     * @param director Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de directores.
     */
    @PostMapping("/update")
    public String updateDirector(@ModelAttribute("director") Director director, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando director con ID {}", director.getId());

        directorRepository.save(director);
        logger.info("Director con ID {} actualizado con éxito.", director.getId());
        return "redirect:/directores"; // Redirigir a la lista de directores
    }

    /**
     * Elimina un director de la base de datos.
     *
     * @param id ID del director a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de directores.
     */
    @PostMapping("/delete")
    public String deleteDirector(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando director con ID {}", id);
        directorRepository.deleteById(id);
        logger.info("Director con ID {} eliminado con éxito.", id);
        return "redirect:/directores"; // Redirigir a la lista de directores
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
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}

