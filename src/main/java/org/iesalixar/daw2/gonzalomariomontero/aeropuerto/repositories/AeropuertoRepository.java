package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long> {
    Page<Aeropuerto> findAll(Pageable pageable);

    Page<Aeropuerto> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    // Resto de métodos que vayamos considerando hacer
}
