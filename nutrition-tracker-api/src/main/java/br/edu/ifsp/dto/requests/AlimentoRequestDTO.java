package br.edu.ifsp.dto.requests;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.enums.UnidadeMedida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlimentoRequestDTO(
        @NotBlank
        String nome,

        @NotNull
        @Valid
        InformacaoNutricional informacaoNutricional,

        @NotNull
        UnidadeMedida unidadeMedida
) {
    public Alimento toEntity(){
        Alimento alimento = new Alimento();
        alimento.setNome(nome);
        alimento.setInfoNutricional(informacaoNutricional);
        alimento.setUnidadeMedida(unidadeMedida);

        return alimento;
    }
}
