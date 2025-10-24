package sv.edu.udb.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import sv.edu.udb.repository.domain.Usuario;

import java.math.BigDecimal;
import java.time.Month;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class GastoRequest {
    @NotNull
    private Month mes;

    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal gastosBasicos;

    @PositiveOrZero
    @Digits(integer = 6, fraction = 2)
    private BigDecimal deudas;

    @PositiveOrZero
    @Digits(integer = 6, fraction = 2)
    private BigDecimal otrosGastos;

    @PositiveOrZero
    @Digits(integer = 6, fraction = 2)
    private BigDecimal ahorro;

    @NotNull
    private Usuario usuario;
}