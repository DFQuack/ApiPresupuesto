package sv.edu.udb.Entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "presupuesto")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPresupuesto;

    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @ElementCollection
    @CollectionTable(name = "presupuesto_ingresos", joinColumns = @JoinColumn(name = "idPresupuesto"))
    @Column(name = "idIngreso")
    private List<Long> idIngresos = new ArrayList<>();

    @Column(precision = 10, scale = 2)
    private BigDecimal gastosBasicos;

    @Column(precision = 10, scale = 2)
    private BigDecimal deudas;

    @Column(precision = 10, scale = 2)
    private BigDecimal otrosGastos;

    @Column(precision = 10, scale = 2)
    private BigDecimal ahorro;

    // Constructores
    public Presupuesto() {}

    public Presupuesto(Long idUsuario, List<Long> idIngresos, BigDecimal gastosBasicos,
                       BigDecimal deudas, BigDecimal otrosGastos, BigDecimal ahorro) {
        this.idUsuario = idUsuario;
        this.idIngresos = idIngresos;
        this.gastosBasicos = gastosBasicos;
        this.deudas = deudas;
        this.otrosGastos = otrosGastos;
        this.ahorro = ahorro;
    }

    // Getters y Setters
    public Long getIdPresupuesto() { return idPresupuesto; }
    public void setIdPresupuesto(Long idPresupuesto) { this.idPresupuesto = idPresupuesto; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public List<Long> getIdIngresos() { return idIngresos; }
    public void setIdIngresos(List<Long> idIngresos) { this.idIngresos = idIngresos; }

    public BigDecimal getGastosBasicos() { return gastosBasicos; }
    public void setGastosBasicos(BigDecimal gastosBasicos) { this.gastosBasicos = gastosBasicos; }

    public BigDecimal getDeudas() { return deudas; }
    public void setDeudas(BigDecimal deudas) { this.deudas = deudas; }

    public BigDecimal getOtrosGastos() { return otrosGastos; }
    public void setOtrosGastos(BigDecimal otrosGastos) { this.otrosGastos = otrosGastos; }

    public BigDecimal getAhorro() { return ahorro; }
    public void setAhorro(BigDecimal ahorro) { this.ahorro = ahorro; }
}