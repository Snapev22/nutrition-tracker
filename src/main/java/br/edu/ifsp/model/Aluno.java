package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private Sexo sexo;
    private FatorAtividade fatorAtividade;
    private Objetivo objetivo;
    private double metaCaloricaEstimada;
    private double metaCaloricaDefinida;
}
