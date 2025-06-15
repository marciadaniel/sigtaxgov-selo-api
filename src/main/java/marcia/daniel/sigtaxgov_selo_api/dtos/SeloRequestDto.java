package marcia.daniel.sigtaxgov_selo_api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;


public record SeloRequestDto(

        @JsonProperty("tipo_produto") @NotBlank @NotNull
        String tipoProduto,
        @NotBlank @NotNull
        String fabricante,
        @NotNull
        Status status) {
}