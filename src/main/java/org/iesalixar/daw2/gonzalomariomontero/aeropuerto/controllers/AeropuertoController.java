package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AeropuertoRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.DirectorRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
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
import org.springframework.dao.DataIntegrityViolationException;

import javax.naming.Binding;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Controller
@RequestMapping("/aeropuertos")
public class AeropuertoController {

    private static final Logger logger = LoggerFactory.getLogger(AeropuertoController.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private AeropuertoRepository aeropuertoRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listAeropuertos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort, Model model, Locale locale)
    {
        logger.info("Solicitando la lista de todos los aeropuertos..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Aeropuerto> aeropuertos;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            aeropuertos = aeropuertoRepository.findByNombreContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) aeropuertoRepository.countByNombreContainingIgnoreCase(search) / 5);
        } else {
            aeropuertos = aeropuertoRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) aeropuertoRepository.count() / 5);
        }
        logger.info("Se han cargado {} aeropuertos.", aeropuertos.toList().size());
        model.addAttribute("listAeropuertos", aeropuertos.toList()); // Pasar la lista de regiones al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/aeropuerto/aeropuerto"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo aeropuerto...");
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("aeropuerto", new Aeropuerto());
        model.addAttribute("directores", directorRepository.findAll());
        return "pages/aeropuerto/aeropuerto-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el aeropuerto con ID {}", id);
        Aeropuerto aeropuerto = aeropuertoRepository.findById(id).orElse(null);
        if (aeropuerto == null) {
            logger.warn("No se encontró el aeropuerto con ID {}", id);
        }
        model.addAttribute("aeropuerto", aeropuerto);
        model.addAttribute("directores", directorRepository.findAll());
        ///* Para mostrar en el /aeropuertos/edit?id=1 los aviones que hay */
        model.addAttribute("avionesEnAeropuerto", avionRepository.findByAeropuertoId(id));

        return "pages/aeropuerto/aeropuerto-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/insert")
    public String insertAeropuerto(@ModelAttribute("aeropuerto") Aeropuerto aeropuerto, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            return "pages/aeropuerto/aeropuerto-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        logger.info("Insertando nuevo aeropuerto con código {}", aeropuerto.getCodIata());
        if (aeropuertoRepository.existsByCodIata(aeropuerto.getCodIata())) {
            logger.warn("El código IATA {} ya existe.", aeropuerto.getCodIata());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.aeropuerto.codIata.duplicate", null, locale));
            return "redirect:/aeropuertos/new";
        }
        try {
            aeropuertoRepository.save(aeropuerto);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violación de integridad al insertar aeropuerto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.aeropuerto.codIata.duplicate", null, locale));
            return "redirect:/aeropuertos/new";
        }
        logger.info("Aeropuerto {} insertado con éxito.", aeropuerto.getCodIata());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.aeropuerto.insert.success", null, locale));
        return "redirect:/aeropuertos"; // Redirigir a la lista de regiones
    }

    @PostMapping("/update")
    public String updateAeropuerto(@ModelAttribute("aeropuerto") Aeropuerto aeropuerto,BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            return "pages/aeropuerto/aeropuerto-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        logger.info("Actualizando aeropuerto con ID {}", aeropuerto.getId());
        if (aeropuertoRepository.existsByCodIataAndIdNot(aeropuerto.getCodIata(), aeropuerto.getId())) {
            logger.warn("El código IATA {} ya existe para otro aeropuerto.", aeropuerto.getCodIata());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.aeropuerto.codIata.duplicate", null, locale));
            return "redirect:/aeropuertos/edit?id=" + aeropuerto.getId();
        }
        try {
            aeropuertoRepository.save(aeropuerto);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violación de integridad al actualizar aeropuerto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.aeropuerto.codIata.duplicate", null, locale));
            return "redirect:/aeropuertos/edit?id=" + aeropuerto.getId();
        }
        logger.info("Aeropuerto con ID {} actualizada con éxito.", aeropuerto.getId());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.aeropuerto.update.success", null, locale));
        return "redirect:/aeropuertos";
    }

    @PostMapping("/delete")
    public String deleteAeropuerto(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando aeropuerto con ID {}", id);
        aeropuertoRepository.deleteById(id);
        logger.info("Aeropuerto con ID {} eliminada con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.aeropuerto.delete.success", null, locale));
        return "redirect:/aeropuertos";
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "nombreAsc" -> Sort.by("nombre").ascending();
            case "nombreDesc" -> Sort.by("nombre").descending();
            case "codIataAsc" -> Sort.by("codIata").ascending();
            case "codIataDesc" -> Sort.by("codIata").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
