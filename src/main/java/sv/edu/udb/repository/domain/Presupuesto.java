package sv.edu.udb.repository.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "presupuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonBackReference(value = "usuario-presupuesto")
    private Usuario usuario;

    @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "presupuesto-ingresos")
    private List<Ingreso> ingresos;
}