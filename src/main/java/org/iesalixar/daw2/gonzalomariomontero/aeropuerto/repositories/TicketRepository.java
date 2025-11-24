package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findAll(Pageable pageable);

    Page<Ticket> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);


}

