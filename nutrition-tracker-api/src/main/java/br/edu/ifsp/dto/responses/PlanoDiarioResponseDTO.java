package br.edu.ifsp.dto.responses;

import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.PlanoDiario;

import java.time.LocalDate;
import java.util.List;

public record PlanoDiarioResponseDTO(
        Long id,
        Long alunoId,
        LocalDate data,
        Long version,
        List<ItemPlanoResponseDTO> itens,
        InformacaoNutricional resumoNutricional
) {
    public static PlanoDiarioResponseDTO fromEntity(PlanoDiario planoDiario, InformacaoNutricional resumo){
        return new PlanoDiarioResponseDTO(
                planoDiario.getId(),
                planoDiario.getAluno().getId(),
                planoDiario.getData(),
                planoDiario.getVersion(),
                planoDiario.getItens().stream().map(ItemPlanoResponseDTO::fromEntity).toList(),
                resumo
        );
    }
}
