package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
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
@RequestMapping("/rutas")
public class RutaAvionController {

    private static final Logger logger =
            LoggerFactory.getLogger(RutaAvionController.class);

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos las rutas disponibles y las muestra en la vista.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf que muestra las rutas.
     */
    // LISTA DE RUTAS
    @GetMapping
    public String listRutas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort, Model model, Locale locale)
    {
        logger.info("Solicitando la lista de todos las rutas..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Ruta> rutas;
        int totalPages = 0;
        rutas = rutaRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) rutaRepository.count() / 5);
        logger.info("Se han cargado {} rutas.", rutas.toList().size());
        model.addAttribute("listRutas", rutas.toList()); // Pasar la lista de rutas al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/ruta/ruta"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    // FORMULARIO NUEVO
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nueva ruta...");
        // Cambiado a 'province' para coincidir con la plantilla Thymeleaf
        model.addAttribute("ruta", new Ruta());
        return "pages/ruta/ruta-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    // INSERTAR AVION
    @PostMapping("/insert")
    public String insertRuta(@ModelAttribute("ruta") Ruta ruta, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva ruta con código {}", ruta.getId());
        rutaRepository.save(ruta);
        logger.info("Aeropuerto {} insertado con éxito.", ruta.getId());

        return "redirect:/rutas"; // Redirigir a la lista de rutas
    }

    // FORMULARIO EDITAR
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para la ruta con ID {}", id);
        Ruta ruta = null;
        Optional<Ruta> rutaOpt = rutaRepository.findById(id);
        if (ruta == null) {
            logger.warn("No se encontró la ruta con ID {}", id);
        }
        // Cambiado a 'route' para coincidir con la plantilla Thymeleaf
        model.addAttribute("ruta", rutaOpt);

        return "pages/ruta/ruta-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    // UPDATE AVION
    @PostMapping("/update")
    public String updateRuta(@ModelAttribute("ruta") Ruta ruta, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando ruta con ID {}", ruta.getId());
        rutaRepository.save(ruta);
        logger.info("Ruta con ID {} actualizada con éxito.", ruta.getId());
        return "redirect:/rutas"; // Redirigir a la lista de rutas
    }

    @PostMapping("/delete")
    public String deleteRuta(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando ruta con ID {}", id);
        rutaRepository.deleteById(id);
        logger.info("Ruta con ID {} eliminada con éxito.", id);
        return "redirect:/rutas"; // Redirigir a la lista de rutas
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
        model.addAttribute("rutas", avion.getRutas());

        return "pages/avion/avion-detail";
    }

    // BUSCAR AVION PARA AÑADIRLO
    @PostMapping("/addExistingAvion")
    public String searchAvion(@RequestParam("avionSearch") String avionSearch,
                                 @RequestParam("rutaId") Long rutaId,
                                 Model model, Locale locale) {

        logger.info("Buscando aviones que coincidan con '{}'", avionSearch);
        List<Avion> searchResults = avionRepository.findAvionByModelo(avionSearch);
        Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);

        if (rutaOpt.isPresent()) {
            model.addAttribute("ruta", rutaOpt.get());
            model.addAttribute("aviones", rutaOpt.get().getAviones());
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
     * @param avionId         ID del avión.
     * @param pasajeroId        ID del pasajero.
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
     * @param avionId          ID del avión.
     * @param pasajeroNombre       Nombre del pasajero.
     * @param pasajeroApellidos     Apellidos del pasajero.
     * @param pasajeroDocumento Documento del pasajero.
     * @param pasajeroEmail Email del pasajero.
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
     * @param avionId         ID del avión.
     * @param pasajeroId        ID del pasajero.
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
            redirectAttributes.addFlashAttribute("errorMessage"," Error al eliminar el pasajero.");
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
