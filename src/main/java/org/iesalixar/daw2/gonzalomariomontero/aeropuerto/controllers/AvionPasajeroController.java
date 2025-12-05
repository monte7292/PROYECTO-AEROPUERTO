package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.*;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/aviones")
public class AvionPasajeroController {

    private static final Logger logger =
            LoggerFactory.getLogger(AvionPasajeroController.class);

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los aviones disponibles y los muestra en la vista.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf que muestra los aviones.
     */
    // LISTA DE AVIONES
    @GetMapping
    public String listAviones(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los aviones..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Avion> aviones;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            aviones = avionRepository.findByModeloContainingIgnoreCaseOrFabricanteContainingIgnoreCase(search, search, pageable);
            totalPages = (int) Math.ceil((double) avionRepository.countByModeloContainingIgnoreCaseOrFabricanteContainingIgnoreCase(search, search) / 5);
        } else {
            aviones = avionRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) avionRepository.count() / 5);
        }
        logger.info("Se han cargado {} aviones.", aviones.toList().size());
        model.addAttribute("listAviones", aviones.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/avion/avion";
    }

    // FORMULARIO NUEVO
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo avión.");
        List<Aeropuerto> listAeropuertos = aeropuertoRepository.findAll();
        model.addAttribute("avion", new Avion());
        model.addAttribute("listAeropuertos", listAeropuertos);
        return "pages/avion/avion-form";
    }

    // INSERTAR AVION
    @PostMapping("/insert")
    public String insertAvion(@Valid @ModelAttribute("avion") Avion avion,
                              BindingResult result,
                              RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nuevo avion con id {}", avion.getId());
        if (result.hasErrors()) {
            List<Aeropuerto> listAerpuertos = aeropuertoRepository.findAll();
            model.addAttribute("listAerpuertos", listAerpuertos);
            return "pages/avion/avion-form";
        }
        avionRepository.save(avion);
        logger.info("Avión insertado con éxito.");
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.avion.insert.success", null, locale));
        return "redirect:/aviones";
    }

    // FORMULARIO EDITAR
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el avión con ID {}", id);
        Optional<Avion> avion = avionRepository.findById(id);
        if (avion.isEmpty()) {
            logger.warn("No se encontró el avión con ID {}", id);
            return "redirect:/aviones";
        }
        List<Aeropuerto> listAeropuertos = aeropuertoRepository.findAll();
        model.addAttribute("avion", avion.get());
        model.addAttribute("listAeropuertos", listAeropuertos);
        return "pages/avion/avion-form";
    }

    // UPDATE AVION
    @PostMapping("/update")
    public String updateAvion(@Valid @ModelAttribute("avion") Avion avion,
                              BindingResult result,
                              RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Actualizando avión con ID {}", avion.getId());
            if (result.hasErrors()) {
                List<Aeropuerto> listAeropuertos = aeropuertoRepository.findAll();
                model.addAttribute("listAeropuertos", listAeropuertos);
                return "pages/avion/avion-form";
            }
            avionRepository.save(avion);
            logger.info("Avión con ID {} actualizado con éxito.", avion.getId());
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.avion.update.success", null, locale));

        return "redirect:/aviones";
    }

    @PostMapping("/delete")
    public String deleteAvion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando avion con ID {}", id);
            avionRepository.deleteById(id);
            logger.info("Avión con ID {} eliminado con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("msg.avion.delete.success", null, locale));

        return "redirect:/aviones";
    }

    // DETALLE AVION
    @GetMapping("/detail")
    public String showAvionDetail(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando detalles para el avión con ID {}", id);
        Optional<Avion> avionOptional = avionRepository.findById(id);

        if (avionOptional.isEmpty()) {
            logger.warn("No se encontró el avión con ID {}", id);
            return "redirect:/aviones";
        }

        Avion avion = avionOptional.get();
        model.addAttribute("avion", avion);
        model.addAttribute("pasajeros", avion.getPasajeros());
        model.addAttribute("allPasajeros",
                pasajeroRepository.findAll(Sort.by("nombre").ascending().and(Sort.by("apellidos").ascending())));

        return "pages/avion/avion-detail";
    }

    // BUSCAR PASAJERO PARA AÑADIRLO
    @PostMapping("/addExistingPasajero")
    public String searchPasajero(@RequestParam("pasajeroSearch") String pasajeroSearch,
                                 @RequestParam("avionId") Long avionId,
                                 Model model, Locale locale) {

        logger.info("Buscando pasajeros que coincidan con '{}'", pasajeroSearch);
        List<Pasajero> searchResults = pasajeroRepository.findPasajeroByDocumento(pasajeroSearch);
        Optional<Avion> avionOpt = avionRepository.findById(avionId);

        if (avionOpt.isPresent()) {
            model.addAttribute("avion", avionOpt.get());
            model.addAttribute("pasajeros", avionOpt.get().getPasajeros());
        } else {
            model.addAttribute("errorMessage", "No se encontró el avión.");
            return "redirect:/aviones";
        }

        model.addAttribute("searchResults", searchResults);
        return "pages/avion/avion-detail";
    }


    /**
     * Añade un pasajero existente al avión.
     *
     * @param avionId            ID del avión.
     * @param pasajeroId         ID del pasajero.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del avión.
     */
    @PostMapping("/addPasajero")
    public String addPasajeroToAvion(@RequestParam("avionId") Long avionId, @RequestParam("pasajeroId") Long pasajeroId, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Añadiendo pasajero con ID {} al avión con ID {}", pasajeroId, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Pasajero> pasajeroOpt = pasajeroRepository.findById(pasajeroId);

            if (avionOpt.isPresent() && pasajeroOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Pasajero pasajero = pasajeroOpt.get();
                avion.getPasajeros().add(pasajero);
                avionRepository.save(avion);
                logger.info("Pasajero añadido con éxito.");
            } else {
                logger.warn("No se encontró el avión o el pasajero.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir el pasajero al avión.");
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Violación de integridad de datos al insertar el avión: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.avion-controller.insert.integrity-violation", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/aviones/detail?id=" + avionId;
        } catch (Exception e) {
            logger.error("Error al añadir el pasajero al avión: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir el pasajero.");
        }
        return "redirect:/aviones/detail?id=" + avionId;
    }


    /**
     * Añade un nuevo pasajero al avión.
     *
     * @param avionId            ID del avión.
     * @param pasajeroNombre     Nombre del pasajero.
     * @param pasajeroApellidos  Apellidos del pasajero.
     * @param pasajeroDocumento  Documento del pasajero.
     * @param pasajeroEmail      Email del pasajero.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del avión.
     */
    @PostMapping("/addNewPasajero")
    public String addNewPasajeroToAvion(@RequestParam("avionId") Long
                                                avionId, @RequestParam("pasajeroNombre") String pasajeroNombre,
                                        @RequestParam("pasajeroApellidos")
                                        String pasajeroApellidos, @RequestParam("pasajeroDocumento") String pasajeroDocumento,
                                        @RequestParam("pasajeroEmail") String pasajeroEmail, RedirectAttributes redirectAttributes) {
        logger.info("Añadiendo nuevo pasajero '{}' con nombre {}, apellidos {}, documento {}, email {} al avion con ID {}", pasajeroNombre, pasajeroApellidos, pasajeroDocumento, pasajeroEmail, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);

            if (avionOpt.isPresent()) {
                Avion avion = avionOpt.get();

                // Verificar si ya existe un pasajero con el mismo nombre en el avión
                boolean pasajeroExists = avion.getPasajeros().stream().anyMatch(pasajero -> pasajero.getNombre().equalsIgnoreCase(pasajeroNombre));
                boolean pasajeroExists2 = avion.getPasajeros().stream().anyMatch(pasajero -> pasajero.getApellidos().equalsIgnoreCase(pasajeroApellidos));

                // Podría mejorarse la lógica
                if (pasajeroExists && pasajeroExists2) {
                    logger.warn("El pasajero con nombre '{}' y apellidos '{}' ya existe en el avión con ID {}", pasajeroNombre, pasajeroApellidos, avionId);
                    redirectAttributes.addFlashAttribute("errorMessage", "El pasajero con el nombre especificado ya está asociado al avión.");
                } else {
                    Pasajero newPasajero = new Pasajero();
                    newPasajero.setNombre(pasajeroNombre);
                    newPasajero.setApellidos(pasajeroApellidos);
                    newPasajero.setDocumento(pasajeroDocumento);
                    newPasajero.setEmail(pasajeroEmail);
                    pasajeroRepository.save(newPasajero);

                    avion.getPasajeros().add(newPasajero);
                    avionRepository.save(avion);

                    logger.info("Nuevo pasajero añadido con éxito.");
                }
            } else {
                logger.warn("No se encontró el avión.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir el pasajero al avión.");
            }
        } catch (Exception e) {
            logger.error("Error al añadir el nuevo pasajero al avión: {}",
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir en el avión el nuevo pasajero.");
        }
        return "redirect:/aviones/detail?id=" + avionId;
    }


    /**
     * Elimina un pasajero asociado al avión.
     *
     * @param avionId            ID del avión.
     * @param pasajeroId         ID del pasajero.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del avión.
     */
    @PostMapping("/removePasajero")
    public String removePasajeroFromAvion(@RequestParam("avionId") Long
                                                  avionId, @RequestParam("pasajeroId") Long pasajeroId, RedirectAttributes
                                                  redirectAttributes, Locale locale) {
        logger.info("Eliminando pasajero con ID {} del avión con ID {}",
                pasajeroId, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Pasajero> pasajeroOpt =
                    pasajeroRepository.findById(pasajeroId);

            if (avionOpt.isPresent() && pasajeroOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Pasajero pasajero = pasajeroOpt.get();
                avion.getPasajeros().remove(pasajero);
                avionRepository.save(avion);
                logger.info("Pasajero eliminado con éxito.");
            } else {
                logger.warn("No se encontró el avión o el pasajero.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el pasajero del avión.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el pasajero del avión: {}",
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", " Error al eliminar el pasajero.");
        }
        return "redirect:/aviones/detail?id=" + avionId;
    }


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
}
