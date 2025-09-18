package sv.edu.udb.controller.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class IngresoRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal sueldo;

    @NotBlank
    private Boolean ingresoFormal;

    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal retencionAFP;

    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal retencionISSS;

    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal retencionRenta;

    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal sueldoNeto;
}
