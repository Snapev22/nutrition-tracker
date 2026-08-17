package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private double metaCaloricaEstimada;
    private double metaCaloricaDefinida;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    private FatorAtividade fatorAtividade;

    @Enumerated(EnumType.STRING)
    private Objetivo objetivo;


}
