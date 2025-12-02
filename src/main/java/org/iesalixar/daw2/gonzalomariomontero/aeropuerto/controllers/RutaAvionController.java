package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AeropuertoRepository;
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

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/rutas")
public class RutaAvionController {

    private static final Logger logger = LoggerFactory.getLogger(RutaAvionController.class);

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    @GetMapping
    public String listRutas(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String search,
                            @RequestParam(required = false) String sort,
                            Model model,
                            Locale locale) {
        logger.info("Listando rutas");
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Ruta> rutas = rutaRepository.findAll(pageable);
        int totalPages = (int) Math.ceil((double) rutaRepository.count() / 5);
        model.addAttribute("listRutas", rutas.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/ruta/ruta";
    }

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        model.addAttribute("ruta", new Ruta());
        model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
        return "pages/ruta/ruta-form";
    }

    @PostMapping("/insert")
    public String insertRuta(@ModelAttribute("ruta") Ruta ruta,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Locale locale,
                             Model model) {
        logger.info("Insertando ruta");
        if (result.hasErrors()) {
            model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
            return "pages/ruta/ruta-form";
        }
        rutaRepository.save(ruta);
        return "redirect:/rutas";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model,
                               Locale locale) {
        Optional<Ruta> ruta = rutaRepository.findById(id);
        if (ruta.isEmpty()) {
            return "redirect:/rutas";
        }
        model.addAttribute("ruta", ruta.get());
        model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
        return "pages/ruta/ruta-form";
    }

    @PostMapping("/update")
    public String updateRuta(@ModelAttribute("ruta") Ruta ruta,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Locale locale,
                             Model model) {
        logger.info("Actualizando ruta {}", ruta.getId());
        if (result.hasErrors()) {
            model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
            return "pages/ruta/ruta-form";
        }
        rutaRepository.save(ruta);
        return "redirect:/rutas";
    }

    @PostMapping("/delete")
    public String deleteRuta(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Eliminando ruta {}", id);
        rutaRepository.deleteById(id);
        return "redirect:/rutas";
    }

    @GetMapping("/detail")
    public String showRutaDetail(@RequestParam("id") Long id,
                                 Model model,
                                 Locale locale) {
        Optional<Ruta> rutaOptional = rutaRepository.findById(id);
        if (rutaOptional.isEmpty()) {
            return "redirect:/rutas";
        }
        Ruta ruta = rutaOptional.get();
        model.addAttribute("ruta", ruta);
        model.addAttribute("aviones", ruta.getAviones());
        model.addAttribute("listAeropuertos", aeropuertoRepository.findAll());
        return "pages/ruta/ruta-detail";
    }

    @PostMapping("/addExistingAvion")
    public String searchAvion(@RequestParam("avionSearch") String avionSearch,
                              @RequestParam("rutaId") Long rutaId,
                              Model model,
                              Locale locale) {
        List<Avion> searchResults = avionRepository.findByModeloContainingIgnoreCase(avionSearch);
        Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);
        if (rutaOpt.isEmpty()) {
            return "redirect:/rutas";
        }
        model.addAttribute("ruta", rutaOpt.get());
        model.addAttribute("aviones", rutaOpt.get().getAviones());
        model.addAttribute("searchResults", searchResults);
        return "pages/ruta/ruta-detail";
    }

    @PostMapping("/addAvion")
    public String addAvionToRuta(@RequestParam("rutaId") Long rutaId,
                                 @RequestParam("avionId") Long avionId,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);
        Optional<Avion> avionOpt = avionRepository.findById(avionId);
        if (rutaOpt.isPresent() && avionOpt.isPresent()) {
            Avion avion = avionOpt.get();
            Ruta ruta = rutaOpt.get();
            if (!avion.getRutas().contains(ruta)) {
                avion.getRutas().add(ruta);
            }
            avionRepository.save(avion);
        }
        return "redirect:/rutas/detail?id=" + rutaId;
    }

    @PostMapping("/removeAvion")
    public String removeAvionFromRuta(@RequestParam("rutaId") Long rutaId,
                                      @RequestParam("avionId") Long avionId,
                                      RedirectAttributes redirectAttributes,
                                      Locale locale) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);
        Optional<Avion> avionOpt = avionRepository.findById(avionId);
        if (rutaOpt.isPresent() && avionOpt.isPresent()) {
            Avion avion = avionOpt.get();
            Ruta ruta = rutaOpt.get();
            avion.getRutas().remove(ruta);
            avionRepository.save(avion);
        }
        return "redirect:/rutas/detail?id=" + rutaId;
    }

    @PostMapping("/addNewAvion")
    public String addNewAvionToRuta(@RequestParam("rutaId") Long rutaId,
                                    @RequestParam("avionModelo") String modelo,
                                    @RequestParam("avionFabricante") String fabricante,
                                    @RequestParam("avionCapacidad") int capacidad,
                                    @RequestParam("avionEstado") String estado,
                                    @RequestParam("avionAeropuertoId") Long aeropuertoId,
                                    RedirectAttributes redirectAttributes,
                                    Locale locale) {
        Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);
        Optional<Aeropuerto> aeropuertoOpt = aeropuertoRepository.findById(aeropuertoId);
        if (rutaOpt.isEmpty() || aeropuertoOpt.isEmpty()) {
            return "redirect:/rutas";
        }
        Ruta ruta = rutaOpt.get();
        Aeropuerto aeropuerto = aeropuertoOpt.get();

        Avion newAvion = new Avion();
        newAvion.setModelo(modelo);
        newAvion.setFabricante(fabricante);
        newAvion.setCapacidad(capacidad);
        newAvion.setEstado(estado);
        newAvion.setAeropuerto(aeropuerto);

        avionRepository.save(newAvion);

        newAvion.getRutas().add(ruta);
        avionRepository.save(newAvion);

        return "redirect:/rutas/detail?id=" + rutaId;
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
