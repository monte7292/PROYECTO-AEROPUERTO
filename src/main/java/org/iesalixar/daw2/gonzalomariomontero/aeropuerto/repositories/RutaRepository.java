package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;
import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Ruta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    Page<Ruta> findAll(Pageable pageable);
}
