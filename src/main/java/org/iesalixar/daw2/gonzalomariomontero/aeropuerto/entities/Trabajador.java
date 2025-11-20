package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "trabajador") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {

    // Campo que almacena el identificador único del trabajador.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre del aeropuerto, como "Samuel"
    @NotEmpty(message = "{msg.trabajador.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.trabajador.nombre.size}")
    @Column(name = "nombre", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String nombre;

    // Campo que almacena los apellidos del trabajador, como "Pérez Ramírez"
    @NotEmpty(message = "{msg.trabajador.apellidos.notEmpty}")
    @Size(max = 100, message = "{msg.trabajador.apellidos.size}")
    @Column(name = "apellidos", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String apellidos;

    // Campo que almacena el cargo del trabajador, como "Azafato"
    @Size(max = 100, message = "{msg.trabajador.cargo.size}")
    @Column(name = "cargo", length = 100) // Define la columna correspondiente en la tabla.
    private String cargo;

    // Campo que almacena la fecha de contratación de un trabajador, como "11/11/2011"
    @NotEmpty(message = "{msg.trabajador.fechaContratacion.notEmpty}")
    @Column(name = "fechaContratacion", nullable = false)  // Define la columna correspondiente en la tabla.
    private Date fechaContratacion;

    // Relación con la entidad `Avion`, representando el avión en el que trabaja el trabajador.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos trabajadores a un avión.
    @JoinColumn(name = "id_avion", nullable = false)
    // Clave foránea en la tabla provinces que referencia a la tabla avion.
    private Avion avion;


    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Trabajador` cuando no es necesario o no
     * se conoce el `id` del trabajador
     * `id` es autogenerado.
     *
     * @param nombre Nombre del trabajador.
     * @param apellidos Apellidos del trabajador.
     * @param cargo Cargo del trabajador
     * @param fechaContratacion Fecha de contratacion del trabajador..
     */
    public Trabajador(String nombre, String apellidos, String cargo, Date fechaContratacion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.fechaContratacion = fechaContratacion;
    }

    public Trabajador(String nombre, String apellidos, Date fechaContratacion){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaContratacion = fechaContratacion;
    }
}
