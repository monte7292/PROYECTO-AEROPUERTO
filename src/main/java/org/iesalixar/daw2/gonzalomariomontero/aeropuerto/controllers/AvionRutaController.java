package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.AvionRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
//AL SER MUHCOS A MUCHOS AL GENERAR LA ENTIDAD MUCHOS A MUCHOS LA RUTA SERÍA TAL QUE ASI:
@RequestMapping("/aviones/rutas")
public class AvionRutaController {

    private static final Logger logger =
            LoggerFactory.getLogger(AvionRutaController.class);

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los aviones disponibles y los muestra en la vista.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf que muestra los aviones.
     */
    @GetMapping
    public String listAviones(Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los aviones...");
        List<Avion> listAviones = null;
        try {
            listAviones = avionRepository.findAll();
            logger.info("Se han cargado {} aviones.", listAviones.size());
        } catch (Exception e) {
            logger.error("Error al listar los aviones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los aviones.");
        }
        model.addAttribute("listAviones", listAviones);
        return "avion";
    }

    /**
     * Muestra el formulario para crear un nuevo avión.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario de avión.
     */
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo avion.");
        List<Ruta> listRutas = rutaRepository.findAll();
        model.addAttribute("avion", new Avion());
        model.addAttribute("listRutas", listRutas);
        // model.addAttribute("listLocations", listLocations);
        return "avion-form.html";
    }

    /**
     * Inserta un nuevo avión en la base de datos.
     *
     * @param avion              Avión a insertar.
     * @param result             Resultado de la validación del avión.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param locale             Localización para mensajes de error.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de aviones si se inserta con éxito, o
    vuelve al formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insertAvion(@Valid @ModelAttribute("avion") Avion avion,
                               BindingResult result,
                               RedirectAttributes redirectAttributes, Locale
                                       locale, Model model) {
        logger.info("Insertando nuevo avión con id {}", avion.getId());
        try {
            if (result.hasErrors()) {
                List<Ruta> listRutas = rutaRepository.findAll();
                model.addAttribute("listRutas", listRutas);
                return "avion-form.html";
            }
            avionRepository.save(avion);
            logger.info("Avión insertado con éxito.");
        } catch (Exception e) {
            logger.error("Error al insertar el avión: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.avion-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/aviones/rutas";
    }

    /**
     * Muestra el formulario para editar un avión existente.
     *
     * @param id    ID del avión a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario de avión.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el avión con ID {}", id);
        Optional<Avion> avion = avionRepository.findById(id);
        if (avion.isEmpty()) {
            logger.warn("No se encontró el avión con ID {}", id);
            return "redirect:/aviones/rutas";
        }
        List<Ruta> listRutas = rutaRepository.findAll();
        model.addAttribute("avion", avion.get());
        model.addAttribute("listRutas", listRutas);
        return "avion-form.html";
    }

    /**
     * Actualiza un avión existente en la base de datos.
     *
     * @param avion             Avión a actualizar.
     * @param result             Resultado de la validación del avión.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param locale             Localización para mensajes de error.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de aviones si se actualiza con éxito, o
    vuelve al formulario si hay errores.
     */
    @PostMapping("/update")
    public String updateAvion(@Valid @ModelAttribute("avion") Avion avion,
                               BindingResult result,
                               RedirectAttributes redirectAttributes, Locale
                                       locale, Model model) {
        logger.info("Actualizando avión con ID {}", avion.getId());
        try {
            if (result.hasErrors()) {
                List<Ruta> listRutas = rutaRepository.findAll();
                model.addAttribute("listRutas", listRutas);
                return "avion-form.html";
            }
            avionRepository.save(avion);
            logger.info("Avión con ID {} actualizado con éxito.", avion.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar el avión con ID {}: {}", avion.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.avion-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/aviones/rutas";
    }

    /**
     * Elimina un avión de la base de datos.
     *
     * @param id                 ID del avión a eliminar.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la lista de aviones.
     */
    @PostMapping("/delete")
    public String deleteAvion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando avión con ID {}", id);
        try {
            avionRepository.deleteById(id);
            logger.info("Avión con ID {} eliminado con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el avión con ID {}: {}", id,
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el avión.");
        }
        return "redirect:/aviones/rutas";
    }

    /**
     * Muestra los detalles de un avión.
     *
     * @param id    ID del avión a mostrar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para los detalles del avión.
     */
    @GetMapping("/detail")
    public String showAvionDetail(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando detalles para el avión con ID {}", id);
        Optional<Avion> avionOptional = avionRepository.findById(id);

        if (avionOptional.isEmpty()) {
            logger.warn("No se encontró el avión con ID {}", id);
            return "redirect:/aviones/rutas";
        }

        Avion avion = avionOptional.get();
        model.addAttribute("avion", avion);
        model.addAttribute("rutas", avion.getRutas());

        return "avion-detail.html";
    }


    /**
     * Busca productos que coincidan con el término ingresado y los muestra para
     añadir al avión.
     *
     * @param rutaSearch1 Aeropuerto origen de la ruta.
     * @param rutaSearch2 Aeropuerto destino de la ruta.
     * @param avionId        ID del avión.
     * @param model          Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para mostrar los resultados
    de la búsqueda.
     */
    @PostMapping("/addExistingRuta")
    public String searchRuta(@RequestParam("rutaSearch") Aeropuerto rutaSearch1, @RequestParam("rutaSearch") Aeropuerto rutaSearch2, @RequestParam("avionId") Long avionId, Model model, Locale locale) {
        logger.info("Buscando rutas que coincidan con '{}' '{}'", rutaSearch1, rutaSearch2);
        List<Ruta> searchResults = rutaRepository.findRutaByAeropuertoOrigenAndAeropuertoDestino(rutaSearch1, rutaSearch2);
        Optional<Avion> avionOpt = avionRepository.findById(avionId);

        if (avionOpt.isPresent()) {
            model.addAttribute("avion", avionOpt.get());
            model.addAttribute("rutas", avionOpt.get().getRutas());
        } else {
            model.addAttribute("errorMessage", "No se encontró el avión.");
            return "redirect:/aviones/rutas";
        }

        model.addAttribute("searchResults", searchResults);
        return "avion-detail";
    }

    /**
     * Añade una ruta existente al avión.
     *
     * @param avionId         ID del avión.
     * @param rutaId        ID de la ruta.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del avión.
     */
    @PostMapping("/addRuta")
    public String addRutaToAvion(@RequestParam("aviontId") Long avionId,
                                     @RequestParam("rutaId") Long rutaId, RedirectAttributes
                                             redirectAttributes, Locale locale) {
        logger.info("Añadiendo ruta con ID {} al avion con ID {}", rutaId, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Ruta> rutaOpt = rutaRepository.findById(rutaId);

            if (avionOpt.isPresent() && rutaOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Ruta ruta = rutaOpt.get();
                avion.getRutas().add(ruta);
                avionRepository.save(avion);
                logger.info("Ruta añadida con éxito.");
            } else {
                logger.warn("No se encontró la ruta o el avión");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir la ruta al avión.");
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Violación de integridad de datos al insertar la ruta: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.avion-controller.insert.integrity-violation", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/aviones/rutas/detail?id=" + avionId;
        } catch (Exception e) {
            logger.error("Error al añadir la ruta al avión: {}",
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir la ruta.");
        }
        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }


    /**
     * Añade un nueva ruta al avión.
     *
     * @param avionId          ID del avión.
     * @param rutaAeropuertoOrigen     Aeropuerto origen de la ruta.
     * @param rutaAeropuertoDestino     Aeropuerto destino de la ruta.
     * @param rutaDuracion Duración de la ruta.
     * @param rutaDistancia Distancia de la ruta.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles de la ruta.
     */
    @PostMapping("/addNewRuta")
    public String addNewRutaToAvion(@RequestParam("avionId") Long avionId, @RequestParam("rutaAeropuertoOrigen") Aeropuerto rutaAeropuertoOrigen,
                                        @RequestParam("rutaAeropuertoDestino") Aeropuerto rutaAeropuertoDestino,
                                        @RequestParam("rutaDuracion") int rutaDuracion,
                                        @RequestParam("rutaDistancia") int rutaDistancia, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Añadiendo nueva ruta '{}' con origen {}, destino {}, duración {}, distancia {} al avion con ID {}", rutaAeropuertoOrigen, rutaAeropuertoDestino, rutaDuracion, rutaDistancia, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);

            if (avionOpt.isPresent()) {
                Avion avion = avionOpt.get();

                // Verificar si ya existe una ruta con el mismo trayecto en el avión
                boolean rutaExists = avion.getRutas().stream().anyMatch(ruta -> ruta.getAeropuertoOrigen().equals(rutaAeropuertoOrigen));
                boolean rutaExists2 = avion.getRutas().stream().anyMatch(ruta -> ruta.getAeropuertoDestino().equals(rutaAeropuertoDestino));

                if (rutaExists && rutaExists2) {
                    logger.warn("La ruta con origen '{}' y destino '{}' ya existe en el avión con ID {}", rutaAeropuertoOrigen, rutaAeropuertoDestino, avionId);
                    redirectAttributes.addFlashAttribute("errorMessage", "La ruta con el origen especificado ya está asociado al avión.");
                } else {
                    Ruta newRuta = new Ruta();
                    newRuta.setAeropuertoOrigen(rutaAeropuertoOrigen);
                    newRuta.setAeropuertoDestino(rutaAeropuertoDestino);
                    newRuta.setDuracion(rutaDuracion);
                    newRuta.setDistancia(rutaDistancia);
                    rutaRepository.save(newRuta);

                    avion.getRutas().add(newRuta);
                    avionRepository.save(avion);

                    logger.info("Nueva ruta añadida con éxito.");
                }
            } else {
                logger.warn("No se encontró el avión.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir la ruta al avión.");
            }
        } catch (Exception e) {
            logger.error("Error al añadir la nueva ruta al avión: {}",
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir en el avión la nueva ruta.");
        }
        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }


    /**
     * Elimina una ruta asociada al avión.
     *
     * @param avionId         ID del avión.
     * @param rutaId        ID de la ruta.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del avión.
     */
    @PostMapping("/removeRuta")
    public String removeRutaFromAvion(@RequestParam("avionId") Long
                                                  avionId, @RequestParam("rutaId") Long rutaId, RedirectAttributes
                                                  redirectAttributes, Locale locale) {
        logger.info("Eliminando ruta con ID {} del avión con ID {}",
                rutaId, avionId);
        try {
            Optional<Avion> avionOpt = avionRepository.findById(avionId);
            Optional<Ruta> rutaOpt =
                    rutaRepository.findById(rutaId);

            if (avionOpt.isPresent() && rutaOpt.isPresent()) {
                Avion avion = avionOpt.get();
                Ruta ruta = rutaOpt.get();
                avion.getRutas().remove(ruta);
                avionRepository.save(avion);
                logger.info("Ruta eliminada con éxito.");
            } else {
                logger.warn("No se encontró el avión o la ruta.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar la ruta del avión.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar la ruta del avión: {}",
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage"," Error al eliminar la ruta.");
        }
        return "redirect:/aviones/rutas/detail?id=" + avionId;
    }
}
