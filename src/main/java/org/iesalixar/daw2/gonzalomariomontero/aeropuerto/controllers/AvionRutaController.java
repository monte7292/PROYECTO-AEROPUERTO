package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AeropuertoRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/aviones/rutas")
public class AvionRutaController {

    private static final Logger logger = LoggerFactory.getLogger(AvionRutaController.class);

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    @Autowired
    private MessageSource messageSource;

    // =====================================================
    // LISTA DE AVIONES
    // =====================================================
    /*@GetMapping
    public String listAviones(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los aviones...");
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Avion> aviones;
        int totalPages = 0;
        aviones = avionRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) avionRepository.count() / 5);
        logger.info("Se han cargado {} aviones.", aviones.toList().size());
        model.addAttribute("listAviones", aviones.toList()); // Pasar la lista de directores al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/avion/avion"; // Nombre de la plantilla Thymeleaf a renderizar

        /*
        try {
            List<Avion> listAviones = avionRepository.findAll();
            model.addAttribute("listAviones", listAviones);
            logger.info("Se han cargado {} aviones.", listAviones.size());
        } catch (Exception e) {
            logger.error("Error al listar los aviones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los aviones.");
        }

        return "pages/avion/avion";*/
    }

    /*
    // =====================================================
    // NUEVO AVION (FORMULARIO)
    // =====================================================
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo avión.");

        model.addAttribute("avion", new Avion());
        model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());

        return "pages/avion/avion-form";
    }

    // =====================================================
    // INSERTAR AVION
    // =====================================================
    @PostMapping("/insert")
    public String insertAvion(@Valid @ModelAttribute("avion") Avion avion,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale, Model model) {

        logger.info("Insertando nuevo avión.");

        if (result.hasErrors()) {
            model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
            return "pages/avion/avion-form";
        }

        try {
            avionRepository.save(avion);
        } catch (Exception e) {
            logger.error("Error al insertar avión: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.avion-controller.insert.error", null, locale));
        }

        return "redirect:/aviones/rutas";
    }

    // =====================================================
    // EDITAR AVION (FORMULARIO)
    // =====================================================
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para avión {}", id);

        Optional<Avion> avion = avionRepository.findById(id);

        if (avion.isEmpty()) {
            return "redirect:/aviones/rutas";
        }

        model.addAttribute("avion", avion.get());
        model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());

        return "pages/avion/avion-form";
    }

    // =====================================================
    // ACTUALIZAR AVION
    // =====================================================
    @PostMapping("/update")
    public String updateAvion(@Valid @ModelAttribute("avion") Avion avion,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale, Model model) {

        logger.info("Actualizando avión ID {}", avion.getId());

        if (result.hasErrors()) {
            model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
            return "pages/avion/avion-form";
        }

        try {
            avionRepository.save(avion);
        } catch (Exception e) {
            logger.error("Error al actualizar avión: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.avion-controller.update.error", null, locale));
        }

        return "redirect:/aviones/rutas";
    }

    // =====================================================
    // ELIMINAR AVION
    // =====================================================
    @PostMapping("/delete")
    public String deleteAvion(@RequestParam("id") Long id,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        logger.info("Eliminando avión ID {}", id);

        try {
            avionRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar avión: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el avión.");
        }

        return "redirect:/aviones/rutas";
    }

    // =====================================================
    // DETALLE DE AVION
    // =====================================================
    @GetMapping("/detail")
    public String showAvionDetail(@RequestParam("id") Long id,
                                  Model model, Locale locale) {

        logger.info("Mostrando detalles del avión ID {}", id);

        Optional<Avion> avionOptional = avionRepository.findById(id);

        if (avionOptional.isEmpty()) {
            return "redirect:/aviones/rutas";
        }

        Avion avion = avionOptional.get();
        model.addAttribute("avion", avion);
        model.addAttribute("rutas", avion.getRutas());

        return "pages/avion/avion-detail";
    }

    // =====================================================
    // BUSCAR Y MOSTRAR RUTAS PARA AÑADIR
    // =====================================================
    @PostMapping("/addExistingRuta")
    public String searchRuta(@RequestParam("rutaSearch") Aeropuerto rutaSearch1,
                             @RequestParam("rutaSearch") Aeropuerto rutaSearch2,
                             @RequestParam("avionId") Long avionId,
                             Model model, Locale locale) {

        logger.info("Buscando rutas con origen {} y destino {}", rutaSearch1, rutaSearch2);

        List<Ruta> searchResults =
                rutaRepository.findRutaByAeropuertoOrigenAndAeropuertoDestino(rutaSearch1, rutaSearch2);

        Optional<Avion> avionOpt = avionRepository.findById(avionId);

        if (avionOpt.isEmpty()) {
            return "redirect:/aviones/rutas";
        }

        model.addAttribute("avion", avionOpt.get());
        model.addAttribute("rutas", avionOpt.get().getRutas());
        model.addAttribute("searchResults", searchResults);

        return "pages/avion/avion-detail";
    }

    // =====================================================
    // AÑADIR RUTA EXISTENTE
    // =====================================================
    @PostMapping("/addRuta")
    public String addRutaToAvion(@RequestParam("aviontId") Long avionId,
                                 @RequestParam("rutaId") Long rutaId,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {

        logger.info("Añadiendo ruta {} al avión {}", rutaId, avionId);

        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);

            if (avionOpt.isPresent() && rutaOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Ruta ruta = rutaOpt.get();
                avion.getRutas().add(ruta);
                avionRepository.save(avion);
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.avion-controller.insert.integrity-violation", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir la ruta.");
        }

        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }

    // =====================================================
    // AÑADIR NUEVA RUTA COMPLETA
    // =====================================================
    @PostMapping("/addNewRuta")
    public String addNewRutaToAvion(@RequestParam("avionId") Long avionId,
                                    @RequestParam("rutaAeropuertoOrigen") Aeropuerto origen,
                                    @RequestParam("rutaAeropuertoDestino") Aeropuerto destino,
                                    @RequestParam("rutaDuracion") int duracion,
                                    @RequestParam("rutaDistancia") int distancia,
                                    RedirectAttributes redirectAttributes,
                                    Locale locale) {

        logger.info("Añadiendo nueva ruta a avión {}", avionId);

        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);

            if (avionOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Ruta newRuta = new Ruta();

                newRuta.setAeropuertoOrigen(origen);
                newRuta.setAeropuertoDestino(destino);
                newRuta.setDuracion(duracion);
                newRuta.setDistancia(distancia);

                rutaRepository.save(newRuta);

                avion.getRutas().add(newRuta);
                avionRepository.save(avion);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al añadir la nueva ruta.");
        }

        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }

    // =====================================================
    // ELIMINAR RUTA DE AVION
    // =====================================================
    @PostMapping("/removeRuta")
    public String removeRutaFromAvion(@RequestParam("avionId") Long avionId,
                                      @RequestParam("rutaId") Long rutaId,
                                      RedirectAttributes redirectAttributes,
                                      Locale locale) {

        logger.info("Eliminando ruta {} del avión {}", rutaId, avionId);

        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);

            if (avionOpt.isPresent() && rutaOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Ruta ruta = rutaOpt.get();

                avion.getRutas().remove(ruta);
                avionRepository.save(avion);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la ruta.");
        }

        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }

    // =====================================================
    // UTILIDAD ORDENACIÓN
    // =====================================================
    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }

        return switch (sort) {
            case "modeloAsc" -> Sort.by("modelo").ascending();
            case "modeloDesc" -> Sort.by("modelo").descending();
            case "fabricanteAsc" -> Sort.by("fabricante").ascending();
            case "fabricanteDesc" -> Sort.by("fabricante").descending();
            case "capacidadAsc" -> Sort.by("capacidad").ascending();
            case "capacidadDesc" -> Sort.by("capacidad").descending();
            case "aeropuertoAsc" -> Sort.by("aeropuerto").ascending();
            case "aeropuertoDesc" -> Sort.by("aeropuerto").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}*/
