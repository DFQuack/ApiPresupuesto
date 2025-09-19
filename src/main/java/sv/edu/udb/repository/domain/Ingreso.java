package sv.edu.udb.repository.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ingreso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sueldo;

    @Column(nullable = false)
    private Boolean ingresoFormal;

    @Column(precision = 10, scale = 2)
    private BigDecimal retencionAFP;

    @Column(precision = 10, scale = 2)
    private BigDecimal retencionISSS;

    @Column(precision = 10, scale = 2)
    private BigDecimal retencionRenta;

    @Column(precision = 10, scale = 2)
    private BigDecimal sueldoNeto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPresupuesto")
    @JsonBackReference
    private Presupuesto presupuesto;

    public void calcularRetenciones() {
        if (Boolean.TRUE.equals(ingresoFormal)) {
            this.retencionAFP = this.sueldo.multiply(new BigDecimal("0.0725"));

            BigDecimal retencionISSSCalculada = this.sueldo.multiply(new BigDecimal("0.03"));
            this.retencionISSS = retencionISSSCalculada.compareTo(new BigDecimal("30")) > 0 ?
                    new BigDecimal("30") : retencionISSSCalculada;

            this.retencionRenta = calcularRenta(this.sueldo);

            this.sueldoNeto = this.sueldo
                    .subtract(this.retencionAFP)
                    .subtract(this.retencionISSS)
                    .subtract(this.retencionRenta);
        } else {
            this.retencionAFP = BigDecimal.ZERO;
            this.retencionISSS = BigDecimal.ZERO;
            this.retencionRenta = BigDecimal.ZERO;
            this.sueldoNeto = this.sueldo;
        }
    }

    private BigDecimal calcularRenta(BigDecimal sueldo) {
        if (sueldo.compareTo(new BigDecimal("472.00")) <= 0) {
            return BigDecimal.ZERO;
        } else if (sueldo.compareTo(new BigDecimal("895.24")) <= 0) {
            return sueldo.subtract(new BigDecimal("472.00"))
                    .multiply(new BigDecimal("0.10"))
                    .add(new BigDecimal("17.67"));
        } else if (sueldo.compareTo(new BigDecimal("2038.10")) <= 0) {
            return sueldo.subtract(new BigDecimal("895.24"))
                    .multiply(new BigDecimal("0.20"))
                    .add(new BigDecimal("60.00"));
        } else {
            return sueldo.subtract(new BigDecimal("2038.10"))
                    .multiply(new BigDecimal("0.30"))
                    .add(new BigDecimal("288.57"));
        }
    }

}