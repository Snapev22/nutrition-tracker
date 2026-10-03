package br.edu.ifsp.dto.responses;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.enums.UnidadeMedida;

public record AlimentoResponseDTO (
    Long id,
    String nome,
    InformacaoNutricional infoNutricional,
    UnidadeMedida unidadeMedida
){
    public static AlimentoResponseDTO  fromEntity(Alimento alimento){
        return new AlimentoResponseDTO(
                alimento.getId(),
                alimento.getNome(),
                alimento.getInfoNutricional(),
                alimento.getUnidadeMedida()
        );
    }

}
