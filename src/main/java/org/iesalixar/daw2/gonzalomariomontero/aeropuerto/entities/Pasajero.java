package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `pasajero` representa una entidad que modela un pasajero dentro de la
 base de datos.
 * Contiene cinco campos: `id`, `nombre` y `apellidos`, `documento`y `email` donde `id`
 * es el identificador único del pasajero,
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar
 automáticamente.
 */

@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "pasajero") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pasajero {
    // Campo que almacena el identificador único del pasajero.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre del pasajero.
    @NotEmpty(message = "{msg.pasajero.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.pasajero.nombre.size}")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Campo que almacena los apellidos del pasajero.
    @NotEmpty(message = "{msg.pasajero.apellidos.notEmpty}")
    @Size(max = 100, message = "{msg.pasajero.apellidos.size}")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    // Campo que almacena el Dni del pasajero.
    @NotEmpty(message = "{msg.pasajero.documento.notEmpty}")
    @Size(max = 100, message = "{msg.pasajero.documento.size}")
    @Column(name = "documento", nullable = false, length = 100)
    private String documento;

    // Campo que almacena el email del pasajero.
    @NotEmpty(message = "{msg.pasajero.email.notEmpty}")
    @Size(max = 100, message = "{msg.pasajero.email.size}")
    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @ManyToMany(mappedBy = "pasajeros")
    private List<Avion> aviones;
    /**
     * @param nombre Nombre del pasajero.
     * @param apellidos Apellidos del pasajero.
     * @param documento Dni del pasajero.
     * @param email Email del pasajero.
     */

    public Pasajero(String nombre, String apellidos, String documento, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.documento = documento;
        this.email = email;
    }
}