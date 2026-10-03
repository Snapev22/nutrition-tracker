package br.edu.ifsp.dto.responses;

import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.ItemPlano;

public record ItemPlanoResponseDTO(
        Long id,
        String nomeAlimento,
        double quantidade,
        InformacaoNutricional totNutricional
) {
    public static ItemPlanoResponseDTO fromEntity(ItemPlano item){
        return new ItemPlanoResponseDTO(
                item.getId(),
                item.getAlimento().getNome(),
                item.getQuantidade(),
                item.getTotalNutricional()
        );
    }
}
