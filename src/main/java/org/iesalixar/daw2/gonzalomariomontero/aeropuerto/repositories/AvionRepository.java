package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Avion;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Pasajero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvionRepository extends JpaRepository<Avion, Long> {
    Page<Avion> findAll(Pageable pageable);

    List<Avion> findAvionByModelo(String modelo);

    @Query("SELECT COUNT(p) > 0 FROM Avion p WHERE p.modelo = :modelo")
    boolean existsByModelo(@Param("modelo") String modelo);
}
