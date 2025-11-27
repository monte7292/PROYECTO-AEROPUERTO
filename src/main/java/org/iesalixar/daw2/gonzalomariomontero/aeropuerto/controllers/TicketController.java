package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.controllers;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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

    /**
     * Lista todos los tickets y las pasa como atributo al modelo para que sean
     * accesibles en la vista `ticket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de tickets.
     */
    @GetMapping()
    public String listTickets(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String search, @RequestParam(required = false) String sort, Model model) {
        logger.info("Solicitando la lista de todos los tickets..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Ticket> tickets;
        int totalPages = 0;
        tickets = ticketRepository.findAll(pageable);
        totalPages = (int) Math.ceil((double) ticketRepository.count() / 5);
        logger.info("Se han cargado {} tickets.", tickets.toList().size());
        model.addAttribute("listTickets", tickets.toList()); // Pasar la lista de provincias al modelo
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "ticket"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear un nuevo ticket.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo ticket.");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("rutas", rutaRepository.findAll()); // Agregar lista de rutas para elegir
        model.addAttribute("pasajeros", pasajeroRepository.findAll()); // Agregar lista de rutas para elegir
        return "ticket-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el ticket con ID {}", id);
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            logger.warn("No se encontró el ticket con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró el ticket.");
        } else {
            model.addAttribute("ticket", ticketOpt);
        }
        model.addAttribute("rutas", rutaRepository.findAll()); // Agregar lista de rutas para elegir
        model.addAttribute("pasajeros", pasajeroRepository.findAll()); // Agregar lista de pasajeros para elegir
        return "ticket-form";
    }

    /**
     * Inserta un nuevo ticket en la base de datos.
     *
     * @param ticket Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de tickets.
     */
    @PostMapping("/insert")
    public String insertTicket(@ModelAttribute("ticket") Ticket ticket, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nuevo ticket con asiento {}", ticket.getAsiento());
       /* if (ticketRepository.existsTicketByCode(ticket.getAsiento())) {
            logger.warn("El código de la provincia {} ya existe.", province.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
            return "redirect:/provinces/new";
        }*/

        //Esta comprobación no tiene mucho sentido con Ticket

        ticketRepository.save(ticket);
        logger.info("Ticket {} insertada con éxito.", ticket.getAsiento());
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
    public String updateTicket(@ModelAttribute("ticket") Ticket ticket, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando ticket con ID {}", ticket.getId());
        /*
       if (provinceRepository.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) {
            logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
            return "redirect:/provinces/edit?id=" + province.getId();
        }*/

        // Comprobación con poco sentido. Podríamos comprobar si el asiento está ocupado o algo similar

        ticketRepository.save(ticket);
        logger.info("Ticket con ID {} actualizado con éxito.", ticket.getId());
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
    public String deleteTicket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ticket con ID {}", id);
        ticketRepository.deleteById(id);
        logger.info("Ticket con ID {} eliminado con éxito.", id);
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
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}

