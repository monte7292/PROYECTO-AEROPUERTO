package org.iesalixar.daw2.gonzalomariomontero.aeropuerto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "ticket") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.ticket.asiento.notEmpty}")
    @Size(max = 3, message = "{msg.ticket.asiento.size}")
    @Column(name = "asiento", nullable = false, length = 3)
    private String asiento;

    @NotEmpty(message = "{msg.ticket.precio.notEmpty}")
    @Column(name = "precio", nullable = false, precision = 5, scale = 2)
    private BigDecimal precio;

    @NotEmpty(message = "{msg.ticket.fechaCompra.notEmpty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fechaCompra", nullable = false)
    private Date fechaCompra;

    //RELACION MUCHO A UNO
    @NotNull(message = "{msg.ticket.ruta.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @NotNull(message = "{msg.ticket.pasajero.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajero", nullable = false)
    private Pasajero pasajero;

    public Ticket(String asiento, BigDecimal precio, Date fechaCompra) {
        this.asiento = asiento;
        this.precio = precio;
        this.fechaCompra = fechaCompra;
    }
}
