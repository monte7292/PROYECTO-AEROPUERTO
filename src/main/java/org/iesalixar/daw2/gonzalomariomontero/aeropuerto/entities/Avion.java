package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import java.util.List;

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "avion") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.avion.modelo.notEmpty}")
    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @NotEmpty(message = "{msg.avion.fabricante.notEmpty}")
    @Column(name = "fabricante", nullable = false, length = 100)
    private String fabricante;

    //El numero total de persona que pueden entrar, ejemplo: 1238291234
    @NotEmpty(message = "{msg.avion.capacidad.notEmpty}")
    @Size(max = 3, message = "{msg.avion.capacidad.size}")
    @Column(name = "capacidad", nullable = false)
    private int capacidad;

    @NotEmpty(message = "{msg.avion.estado.notEmpty}")
    @Column(name = "estado", nullable = false)
    private String estado;

    /*
    @NotNull(message = "{msg.avion.aeropuerto.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeropuerto", nullable = false)
    private Aeropuerto aeropuerto;

    //Relacion one to many
    //el mapeado es el nombre de la tabla
    @OneToMany(mappedBy = "avion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trabajador> trabajadores;

    @ManyToMany
    @JoinTable(
            name = "ruta_avion",
            joinColumns = @JoinColumn(name = "id_ruta"),
            inverseJoinColumns = @JoinColumn(name = "id_avion")
    )
    private List<Ruta> rutas;

    @ManyToMany
    @JoinTable(
            name = "pasajero_avion",
            joinColumns = @JoinColumn(name = "id_pasajero"),
            inverseJoinColumns = @JoinColumn(name = "id_avion")
    )
    private List<Pasajero> pasajeros;
*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeropuerto", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Aeropuerto aeropuerto;

    @OneToMany(mappedBy = "avion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Trabajador> trabajadores;

    @ManyToMany
    @JoinTable(
            name = "ruta_avion", // nombre de la tabla en BD
            joinColumns = @JoinColumn(name = "id_avion"), // columna que apunta a Avion
            inverseJoinColumns = @JoinColumn(name = "id_ruta") // columna que apunta a Ruta
    )
    private List<Ruta> rutas;


    @ManyToMany
    @JoinTable(
            name = "pasajero_avion",
            joinColumns = @JoinColumn(name = "id_avion"),
            inverseJoinColumns = @JoinColumn(name = "id_pasajero")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Pasajero> pasajeros;


    public Avion(String estado, int capacidad, String fabricante, String modelo) {
        this.estado = estado;
        this.capacidad = capacidad;
        this.fabricante = fabricante;
        this.modelo = modelo;
    }
}


