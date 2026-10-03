package br.edu.ifsp.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPlanoRequestDTO(
        @NotNull
        Long alimentoId,

        @Positive
        double quantidade
) {
}
