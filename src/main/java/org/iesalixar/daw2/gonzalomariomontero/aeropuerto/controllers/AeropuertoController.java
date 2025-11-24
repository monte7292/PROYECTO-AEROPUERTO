package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AeropuertoRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/aeropuertos")
public class AeropuertoController {

    private static final Logger logger = LoggerFactory.getLogger(AeropuertoController.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    @GetMapping
    public String listAeropuertos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort, Model model)
    {
        logger.info("Solicitando la lista de todos los aeropuertos..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Aeropuerto> aeropuertos;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            aeropuertos = aeropuertoRepository.findByNameContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) aeropuertoRepository.countByNameContainingIgnoreCase(search) / 5);
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
        return "province"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo aeropuerto...");
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("aeropuerto", new Aeropuerto());
        return "aeropuerto-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el aeropuerto con ID {}", id);
        Aeropuerto aeropuerto = null;
        Optional<Aeropuerto> aeropuertoOpt = aeropuertoRepository.findById(id);
        if (aeropuerto == null) {
            logger.warn("No se encontró el aeropuerto con ID {}", id);
        }
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("aeropuerto", aeropuertoOpt);

        return "aeropuerto-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/insert")
    public String insertAeropuerto(@ModelAttribute("aeropuerto") Aeropuerto aeropuerto, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nuevo aeropuerto con código {}", aeropuerto.getCodIata());
        /*if (provinciaRepository.existsProvinceByCode(provincia.getCode())) {
            logger.warn("El código de la región {} ya existe.", provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
            // Corregido: ruta de redirección debe ser /provinces/new
            return "redirect:/provinces/new";
        }*/
        aeropuertoRepository.save(aeropuerto);
        logger.info("Aeropuerto {} insertado con éxito.", aeropuerto.getCodIata());

        return "redirect:/aeropuertos"; // Redirigir a la lista de regiones
    }

    @PostMapping("/update")
    public String updateAeropuerto(@ModelAttribute("aeropuerto") Aeropuerto aeropuerto, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando aeropuerto con ID {}", aeropuerto.getId());
        /*if (provinciaRepository.existsProvinceByCodeAndNotId(provincia.getCode())) {
            logger.warn("El código de la región {} ya existe para otra región.", provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra región.");
            return "redirect:/provinces/edit?id=" + provincia.getId();
        }*/
        aeropuertoRepository.save(aeropuerto);
        logger.info("Aeropuerto con ID {} actualizada con éxito.", aeropuerto.getId());
        return "redirect:/aeropuertos"; // Redirigir a la lista de regiones
    }

    @PostMapping("/delete")
    public String deleteAeropuerto(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando aeropuerto con ID {}", id);
        aeropuertoRepository.deleteById(id);
        logger.info("Aeropuerto con ID {} eliminada con éxito.", id);
        return "redirect:/aeropuertos"; // Redirigir a la lista de regiones
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
