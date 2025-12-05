package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Pasajero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    Page<Pasajero> findAll(Pageable pageable);

    Page<Pasajero> findByDocumento(String documento, Pageable pageable);

    List<Pasajero> findPasajeroByDocumento(String documento);

    @Query("SELECT COUNT(p) > 0 FROM Pasajero p WHERE p.documento = :documento")
    boolean existsByDocumento(@Param("documento") String documento);

    // Resto de métodos que vayamos considerando hacer

    Page<Pasajero> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos, Pageable pageable);

    long countByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos);
}
