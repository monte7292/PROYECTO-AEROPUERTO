package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Aeropuerto;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Page<Ruta> findAll(Pageable pageable);

    List<Ruta> findRutaByAeropuertoOrigenAndAeropuertoDestino(Aeropuerto AeropuertoOrigen, Aeropuerto AeropuertoDestino);

    @Query("SELECT COUNT(r) > 0 FROM Ruta r WHERE r.aeropuertoOrigen = :AeropuertoOrigen AND r.aeropuertoDestino = :AeropuertoDestino")
    boolean existsByAeropuertoOrigenAndAeropuertoDestino(
            @Param("AeropuertoOrigen") String AeropuertoOrigen,
            @Param("AeropuertoDestino") String AeropuertoDestino
    );

    Page<Ruta> findByAeropuertoOrigen_NombreContainingIgnoreCaseOrAeropuertoDestino_NombreContainingIgnoreCase(String origenNombre, String destinoNombre, Pageable pageable);

    long countByAeropuertoOrigen_NombreContainingIgnoreCaseOrAeropuertoDestino_NombreContainingIgnoreCase(String origenNombre, String destinoNombre);
}
