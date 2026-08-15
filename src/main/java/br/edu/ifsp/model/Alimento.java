package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.UnidadeMedida;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "Alimento")
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alimento")
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    @Embedded
    private InformacaoNutricional infoNutricional;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

}
