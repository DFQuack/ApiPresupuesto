package sv.edu.udb.repository.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;

@Entity
@Table(name = "gasto_mensual")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGasto;

    @Column(nullable = false)
    private Month mes;

    @Column(precision = 10, scale = 2)
    private BigDecimal gastosBasicos;

    @Column(precision = 10, scale = 2)
    private BigDecimal deudas;

    @Column(precision = 10, scale = 2)
    private BigDecimal otrosGastos;

    @Column(precision = 10, scale = 2)
    private BigDecimal ahorro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}