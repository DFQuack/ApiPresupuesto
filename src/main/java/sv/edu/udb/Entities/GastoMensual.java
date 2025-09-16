package sv.edu.udb.Entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "gasto_mensual")
public class GastoMensual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGasto;

    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private String mes;

    @Column(precision = 10, scale = 2)
    private BigDecimal gastosBasicos;

    @Column(precision = 10, scale = 2)
    private BigDecimal deudas;

    @Column(precision = 10, scale = 2)
    private BigDecimal otrosGastos;

    @Column(precision = 10, scale = 2)
    private BigDecimal ahorro;

    // Constructores
    public GastoMensual() {}

    public GastoMensual(Long idUsuario, String mes, BigDecimal gastosBasicos,
                        BigDecimal deudas, BigDecimal otrosGastos, BigDecimal ahorro) {
        this.idUsuario = idUsuario;
        this.mes = mes;
        this.gastosBasicos = gastosBasicos;
        this.deudas = deudas;
        this.otrosGastos = otrosGastos;
        this.ahorro = ahorro;
    }

    // Getters y Setters
    public Long getIdGasto() { return idGasto; }
    public void setIdGasto(Long idGasto) { this.idGasto = idGasto; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }

    public BigDecimal getGastosBasicos() { return gastosBasicos; }
    public void setGastosBasicos(BigDecimal gastosBasicos) { this.gastosBasicos = gastosBasicos; }

    public BigDecimal getDeudas() { return deudas; }
    public void setDeudas(BigDecimal deudas) { this.deudas = deudas; }

    public BigDecimal getOtrosGastos() { return otrosGastos; }
    public void setOtrosGastos(BigDecimal otrosGastos) { this.otrosGastos = otrosGastos; }

    public BigDecimal getAhorro() { return ahorro; }
    public void setAhorro(BigDecimal ahorro) { this.ahorro = ahorro; }
}