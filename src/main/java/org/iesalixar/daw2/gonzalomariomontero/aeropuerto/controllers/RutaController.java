package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
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
@RequestMapping("/rutas")
public class RutaController {

    private static final Logger logger = LoggerFactory.getLogger(RutaController.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private RutaRepository rutaRepository;

    @GetMapping
    public String listRutas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort, Model model)
    {
        logger.info("Solicitando la lista de todos las rutas..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Ruta> rutas;
        int totalPages = 0;
        rutas = rutaRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) rutaRepository.count() / 5);
        logger.info("Se han cargado {} rutas.", rutas.toList().size());
        model.addAttribute("listRutas", rutas.toList()); // Pasar la lista de regiones al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "ruta"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva ruta...");
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("ruta", new Ruta());
        return "ruta-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la ruta con ID {}", id);
        Ruta ruta = null;
        Optional<Ruta> rutaOpt = rutaRepository.findById(id);
        if (ruta == null) {
            logger.warn("No se encontró la ruta con ID {}", id);
        }
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("ruta", rutaOpt);

        return "ruta-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/insert")
    public String insertRuta(@ModelAttribute("ruta") Ruta ruta, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva ruta con código {}", ruta.getId());
        /*if (provinciaRepository.existsProvinceByCode(provincia.getCode())) {
            logger.warn("El código de la región {} ya existe.", provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
            // Corregido: ruta de redirección debe ser /provinces/new
            return "redirect:/provinces/new";
        }*/
        rutaRepository.save(ruta);
        logger.info("Aeropuerto {} insertado con éxito.", ruta.getId());

        return "redirect:/rutas"; // Redirigir a la lista de rutas
    }

    @PostMapping("/update")
    public String updateRuta(@ModelAttribute("ruta") Ruta ruta, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando ruta con ID {}", ruta.getId());
        /*if (provinciaRepository.existsProvinceByCodeAndNotId(provincia.getCode())) {
            logger.warn("El código de la región {} ya existe para otra región.", provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra región.");
            return "redirect:/provinces/edit?id=" + provincia.getId();
        }*/
        rutaRepository.save(ruta);
        logger.info("Ruta con ID {} actualizada con éxito.", ruta.getId());
        return "redirect:/rutas"; // Redirigir a la lista de rutas
    }

    @PostMapping("/delete")
    public String deleteRuta(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ruta con ID {}", id);
        rutaRepository.deleteById(id);
        logger.info("Ruta con ID {} eliminada con éxito.", id);
        return "redirect:/rutas"; // Redirigir a la lista de rutas
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "aeropuertoDestinoAsc" -> Sort.by("aeropuertoDestino").ascending();
            case "aeropuertoDestinoDesc" -> Sort.by("aeropuertoDestino").descending();
            case "aeropuertoOrigenAsc" -> Sort.by("aeropuertoOrigen").ascending();
            case "aeropuertoOrigenDesc" -> Sort.by("aeropuertoOrigen").descending();
            case "distanciaAsc" -> Sort.by("distancia").ascending();
            case "distanciaDesc" -> Sort.by("distancia").descending();
            case "duracionAsc" -> Sort.by("duracion").ascending();
            case "duracionDesc" -> Sort.by("duracion").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}