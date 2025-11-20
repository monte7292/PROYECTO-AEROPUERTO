package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int estado;

    @NotNull(message = "{msg.avion.aeropuerto.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeropuerto", nullable = false)
    private Aeropuerto aeropuerto;

    public Avion(int estado, int capacidad, String fabricante, String modelo) {
        this.estado = estado;
        this.capacidad = capacidad;
        this.fabricante = fabricante;
        this.modelo = modelo;
    }
}


