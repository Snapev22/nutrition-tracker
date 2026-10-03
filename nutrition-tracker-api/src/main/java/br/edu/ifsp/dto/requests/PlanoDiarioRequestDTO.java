package br.edu.ifsp.dto.requests;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.PlanoDiario;

import java.time.LocalDate;

public record PlanoDiarioRequestDTO(
        LocalDate data
) {
    public PlanoDiario toEntity(Aluno aluno){
        PlanoDiario planoDiario = new PlanoDiario();
        planoDiario.setAluno(aluno);
        planoDiario.setData(data);

        return planoDiario;
    }
}
