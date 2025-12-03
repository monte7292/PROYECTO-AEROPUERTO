package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvionRepository extends JpaRepository<Avion, Long> {
    Page<Avion> findAll(Pageable pageable);
    List<Avion> findByModeloContainingIgnoreCase(String modelo);
    /* Para mostrar en el /aeropuertos/edit?id=1 los aviones que hay */
    List<Avion> findByAeropuertoId(Long aeropuertoId);
}
