package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "ruta") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena la duración del vuelo en minutos.
    @NotEmpty(message = "{msg.ruta.duracion.notEmpty}")
    @Size(max = 5, message = "{msg.pasajero.nombre.size}")
    @Column(name = "duracion", nullable = false, length = 5)
    private int duracion;

    @NotEmpty(message = "{msg.ruta.distancia.notEmpty}")
    @Size(max = 5, message = "{msg.pasajero.nombre.size}")
    @Column(name = "distancia", nullable = false, length = 5)
    private int distancia;

    /*
    @NotNull(message = "{msg.avion.aeropuerto.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAeropuertoOrigen", nullable = false)
    private Aeropuerto aeropuertoOrigen;

    @NotNull(message = "{msg.avion.aeropuerto.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAeropuertoDestino", nullable = false)
    private Aeropuerto aeropuertoDestino;

    //Está será ña relación manytomany con Avion
    // Es la lista de objetos de rutan que se crean en Avion - linea 55
    @ManyToMany(mappedBy = "rutas")
    private List<Avion> aviones;

    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAeropuertoOrigen", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Aeropuerto aeropuertoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAeropuertoDestino", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Aeropuerto aeropuertoDestino;

    @ManyToMany(mappedBy = "rutas")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Avion> aviones;


    public Ruta(Aeropuerto aeropuertoOrigen, Aeropuerto aeropuertoDestino, int duracion, int distancia) {
        this.aeropuertoOrigen = aeropuertoOrigen;
        this.aeropuertoDestino = aeropuertoDestino;
        this.duracion = duracion;
        this.distancia = distancia;
    }
}


