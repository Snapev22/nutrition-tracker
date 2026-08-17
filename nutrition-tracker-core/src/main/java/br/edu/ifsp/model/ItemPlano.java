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
@Table(name = "item_plano")
public class ItemPlano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_plano")
    @EqualsAndHashCode.Include
    private Long id;
    private double quantidade ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alimento", nullable = false)
    private Alimento  alimento;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "id_plano_diario")
    private PlanoDiario planoDiario;

    @Embedded
    private InformacaoNutricional totalNutricional;

    public InformacaoNutricional calcularTotalNutricional(){
       double fator = (alimento.getUnidadeMedida() == UnidadeMedida.UNIDADE)
               ? quantidade
               :quantidade / 100;

       totalNutricional = alimento.getInfoNutricional().multiplicarPor(fator);
       return totalNutricional;
    }

    public double getCaloriasTotais() {
        return totalNutricional == null ? 0 : totalNutricional.getCalorias();
    }
}
