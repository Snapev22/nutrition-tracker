package br.edu.ifsp.dto.responses;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;

public record AlunoResponseDTO (
        Long id,
        String nome,
        int idade,
        double peso,
        double altura,
        Sexo sexo,
        FatorAtividade fatorAtividade,
        Objetivo objetivo,
        double metaCaloricaEstimada,
        double metaCaloricaDefinida
) {
    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getPeso(),
                aluno.getAltura(),
                aluno.getSexo(),
                aluno.getFatorAtividade(),
                aluno.getObjetivo(),
                aluno.getMetaCaloricaEstimada(),
                aluno.getMetaCaloricaDefinida()
        );
    }
}

