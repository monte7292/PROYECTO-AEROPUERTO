package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "director") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {

    // Campo que almacena el identificador único del Director.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre del director, como "Manuel"
    @NotEmpty(message = "{msg.director.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.director.nombre.size}")
    @Column(name = "nombre", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String nombre;

    // Campo que almacena los apellidos del director, como "López Martín"
    @NotEmpty(message = "{msg.director.apellidos.notEmpty}")
    @Size(max = 100, message = "{msg.director.apellidos.size}")
    @Column(name = "apellidos", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String apellidos;


    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Aeropuerto> aeropuertos;


    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Director` cuando no es necesario o no
     * se conoce el `id` del director
     * `id` es autogenerado.
     *
     * @param nombre Nombre del Director.
     * @param apellidos Apellidos del Director.
     */
    public Director(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
}