package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.repositories;

import org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Page<Director> findAll(Pageable pageable);

    // Resto de métodos que vayamos considerando hacer

    Page<Director> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos, Pageable pageable);

    long countByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos);
}
