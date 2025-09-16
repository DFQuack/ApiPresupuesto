package sv.edu.udb.Entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String contraseña;

    @ElementCollection
    @CollectionTable(name = "usuario_ingresos", joinColumns = @JoinColumn(name = "idUsuario"))
    @Column(name = "idIngreso")
    private List<Long> idIngresos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "usuario_presupuestos", joinColumns = @JoinColumn(name = "idUsuario"))
    @Column(name = "idPresupuesto")
    private List<Long> idPresupuestos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "usuario_gastos_mensuales", joinColumns = @JoinColumn(name = "idUsuario"))
    @Column(name = "idGasto")
    private List<Long> idGastosMensuales = new ArrayList<>();

    // Constructores
    public Usuario() {}

    public Usuario(String username, String contraseña) {
        this.username = username;
        this.contraseña = contraseña;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public List<Long> getIdIngresos() { return idIngresos; }
    public void setIdIngresos(List<Long> idIngresos) { this.idIngresos = idIngresos; }

    public List<Long> getIdPresupuestos() { return idPresupuestos; }
    public void setIdPresupuestos(List<Long> idPresupuestos) { this.idPresupuestos = idPresupuestos; }

    public List<Long> getIdGastosMensuales() { return idGastosMensuales; }
    public void setIdGastosMensuales(List<Long> idGastosMensuales) { this.idGastosMensuales = idGastosMensuales; }
}