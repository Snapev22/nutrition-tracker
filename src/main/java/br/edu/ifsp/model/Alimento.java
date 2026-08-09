package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.UnidadeMedida;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Alimento {
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;
    private InformacaoNutricional infoNutricional;
    private UnidadeMedida unidadeMedida;

}
