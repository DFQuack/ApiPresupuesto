package sv.edu.udb.Entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingreso")
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIngreso;

    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

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

    // Constructores
    public Ingreso() {}

    public Ingreso(Long idUsuario, String nombre, BigDecimal sueldo, Boolean ingresoFormal) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.sueldo = sueldo;
        this.ingresoFormal = ingresoFormal;
        this.calcularRetenciones();
    }

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

    // Getters y Setters
    public Long getIdIngreso() { return idIngreso; }
    public void setIdIngreso(Long idIngreso) { this.idIngreso = idIngreso; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getSueldo() { return sueldo; }
    public void setSueldo(BigDecimal sueldo) { this.sueldo = sueldo; }

    public Boolean getIngresoFormal() { return ingresoFormal; }
    public void setIngresoFormal(Boolean ingresoFormal) { this.ingresoFormal = ingresoFormal; }

    public BigDecimal getRetencionAFP() { return retencionAFP; }
    public void setRetencionAFP(BigDecimal retencionAFP) { this.retencionAFP = retencionAFP; }

    public BigDecimal getRetencionISSS() { return retencionISSS; }
    public void setRetencionISSS(BigDecimal retencionISSS) { this.retencionISSS = retencionISSS; }

    public BigDecimal getRetencionRenta() { return retencionRenta; }
    public void setRetencionRenta(BigDecimal retencionRenta) { this.retencionRenta = retencionRenta; }

    public BigDecimal getSueldoNeto() { return sueldoNeto; }
    public void setSueldoNeto(BigDecimal sueldoNeto) { this.sueldoNeto = sueldoNeto; }
}