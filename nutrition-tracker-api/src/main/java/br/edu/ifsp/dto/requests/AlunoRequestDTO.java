package br.edu.ifsp.dto.requests;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record AlunoRequestDTO (
        @NotBlank String nome,
        @Positive int idade,
        @Positive double peso,
        @Positive double altura,
        @NotNull Sexo sexo,
        @NotNull FatorAtividade fatorAtividade,
        @NotNull Objetivo objetivo,
        @PositiveOrZero Double metaCaloricaDefinida
){
    public Aluno toEntity() {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setIdade(idade);
        aluno.setPeso(peso);
        aluno.setAltura(altura);
        aluno.setSexo(sexo);
        aluno.setFatorAtividade(fatorAtividade);
        aluno.setObjetivo(objetivo);
        if (metaCaloricaDefinida != null) {
            aluno.setMetaCaloricaDefinida(metaCaloricaDefinida);
        }
        return aluno;
    }
}

