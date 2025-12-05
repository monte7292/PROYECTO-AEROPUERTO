package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "aeropuerto") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aeropuerto {

    // Campo que almacena el identificador único del aeropuerto.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre del aeropuerto, como "Aeropuerto sevilla"
    @NotEmpty(message = "{msg.aeropuerto.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.aeropuerto.nombre.size}")
    @Column(name = "nombre", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String nombre;

    // Campo que almacena la ciudad del aeropuerto, como "Sevilla"
    @NotEmpty(message = "{msg.aeropuerto.ciudad.notEmpty}")
    @Size(max = 100, message = "{msg.aeropuerto.ciudad.size}")
    @Column(name = "ciudad", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String ciudad;

    // Campo que almacena el país del aeropuerto, como "España"
    @NotEmpty(message = "{msg.aeropuerto.pais.notEmpty}")
    @Size(max = 100, message = "{msg.aeropuerto.pais.size}")
    @Column(name = "pais", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String pais;

    // Campo que almacena el país del aeropuerto, como "España"
    @NotEmpty(message = "{msg.aeropuerto.codIata.notEmpty}")
    @Size(max = 3, message = "{msg.aeropuerto.codIata.size}")
    @Column(name = "codIata", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String codIata;

    // Relación con la entidad `Director`, representando el director del Aeropuerto. NO SÉ SI ESTÁ BIEN!!!
    @NotNull(message = "{msg.province.region.notNull}")
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos aeropuertos a un director.
    @JoinColumn(name = "id_director", nullable = false) // Clave foránea en la tabla Aeropuerto que referencia a la tabla Director.
    private Director director;

    // Relación con entidad 'Avión'
    @OneToMany(mappedBy = "aeropuerto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Avion> aviones;



    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Aeropuerto` cuando no es necesario o no
     * se conoce el `id` del aeropuerto
     * `id` es autogenerado.
     *
     * @param nombre Nombre del aeropuerto.
     * @param ciudad Ciudad del aeropuerto.
     * @param pais Pais del aeropuerto.
     * @param codIata Código IATA del aeropuerto.
     */
    public Aeropuerto(String nombre, String ciudad, String pais, String codIata) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.codIata = codIata;
    }
}