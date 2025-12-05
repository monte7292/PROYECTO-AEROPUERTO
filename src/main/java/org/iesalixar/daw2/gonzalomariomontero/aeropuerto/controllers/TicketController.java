package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;
import jakarta.validation.Valid;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ticket;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.TicketRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.RutaRepository;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories.PasajeroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Ticket`.
 */
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    // DAO para gestionar las operaciones de las provincias en la base de datos
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los tickets y las pasa como atributo al modelo para que sean
     * accesibles en la vista `ticket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de tickets.
     */
    @GetMapping()
    public String listTickets(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model, Locale locale) {
        logger.info("Solicitando la lista de todos los tickets..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Ticket> tickets;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            tickets = ticketRepository.findByAsientoContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) ticketRepository.countByAsientoContainingIgnoreCase(search) / 5);
        } else {
            tickets = ticketRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) ticketRepository.count() / 5);
        }
        logger.info("Se han cargado {} tickets.", tickets.toList().size());
        model.addAttribute("listTickets", tickets.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "pages/ticket/ticket";
    }

    /**
     * Muestra el formulario para crear un nuevo ticket.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo ticket.");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("rutas", rutaRepository.findAll()); // Agregar lista de rutas para elegir
        model.addAttribute("pasajeros", pasajeroRepository.findAll()); // Agregar lista de rutas para elegir
        return "pages/ticket/ticket-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el ticket con ID {}", id);
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (!ticketOpt.isPresent()) {
            logger.warn("No se encontró el ticket con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró el ticket.");
        } else {
            model.addAttribute("ticket", ticketOpt.get());
        }
        model.addAttribute("rutas", rutaRepository.findAll()); // Agregar lista de rutas para elegir
        model.addAttribute("pasajeros", pasajeroRepository.findAll()); // Agregar lista de pasajeros para elegir
        return "pages/ticket/ticket-form";
    }

    /**
     * Inserta un nuevo ticket en la base de datos.
     *
     * @param ticket Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de tickets.
     */
    @PostMapping("/insert")
    public String insertTicket(@Valid @ModelAttribute("ticket") Ticket ticket, Model model, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo ticket con asiento {}", ticket.getAsiento());
        if(result.hasErrors()){
            return "pages/ticket/ticket-form";
        }

        ticketRepository.save(ticket);
        logger.info("Ticket {} insertada con éxito.", ticket.getAsiento());
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("msg.ticket.insert.success", null, locale));
        return "redirect:/tickets"; // Redirigir a la lista de tickets
    }

    /**
     * Actualiza un ticket existente en la base de datos.
     *
     * @param ticket Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de tickets.
     */
    @PostMapping("/update")
    public String updateTicket(@Valid Model model, @ModelAttribute("ticket") Ticket ticket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando ticket con ID {}", ticket.getId());
        if (result.hasErrors()) {
            model.addAttribute("rutas", rutaRepository.findAll());
            return "pages/ticket/ticket-form";
        }
        ticketRepository.save(ticket);
        logger.info("Ticket {} insertada con éxito.", ticket.getAsiento());
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("msg.ticket.update.success", null, locale));
        return "redirect:/tickets"; // Redirigir a la lista de provincias
    }

    /**
     * Elimina un ticket de la base de datos.
     *
     * @param id ID del ticket a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de tickets.
     */
    @PostMapping("/delete")
    public String deleteTicket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando ticket con ID {}", id);
        ticketRepository.deleteById(id);
        logger.info("Ticket con ID {} eliminado con éxito.", id);
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("msg.ticket.delete.success", null, locale));
        return "redirect:/tickets"; // Redirigir a la lista de tickets
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "asientoAsc" -> Sort.by("asiento").ascending();
            case "asientoDesc" -> Sort.by("asiento").descending();
            case "precioAsc" -> Sort.by("precio").ascending();
            case "precioDesc" -> Sort.by("precio").descending();
            case "fechaCompraAsc" -> Sort.by("fechaCompra").ascending();
            case "fechaCompraDesc" -> Sort.by("fechaCompra").descending();
            case "rutaAsc" -> Sort.by("ruta").ascending();
            case "rutaDesc" -> Sort.by("ruta").descending();
            case "pasajeroAsc" -> Sort.by("pasajero").ascending();
            case "pasajeroDesc" -> Sort.by("pasajero").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}

