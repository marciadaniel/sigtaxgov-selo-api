package marcia.daniel.sigtaxgov_selo_api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;

import java.time.LocalDate;

public record SeloResponseDto(
                              @NotBlank @NotNull
                              String codigo,
                              @JsonProperty("tipo_produto")  @NotBlank @NotNull
                              String tipoProduto,
                              @NotBlank @NotNull
                              String fabricante,
                              @NotBlank @NotNull
                              LocalDate dataEmissao,
                              @NotBlank @NotNull
                              Status status) {
}
