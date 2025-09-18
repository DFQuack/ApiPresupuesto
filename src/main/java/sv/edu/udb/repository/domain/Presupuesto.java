package sv.edu.udb.repository.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "presupuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal gastosBasicos;

    @Column(precision = 10, scale = 2)
    private BigDecimal deudas;

    @Column(precision = 10, scale = 2)
    private BigDecimal otrosGastos;

    @Column(precision = 10, scale = 2)
    private BigDecimal ahorro;

    @OneToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingreso> ingresos;
}