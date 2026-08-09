package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.UnidadeMedida;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPlano {
    @EqualsAndHashCode.Include
    private Long id;

    private Alimento  alimento;
    private double quantidade ;
    private InformacaoNutricional totalNutricional;

    public InformacaoNutricional calcularTotalNutricional(){
       double fator = (alimento.getUnidadeMedida() == UnidadeMedida.UNIDADE)
               ? quantidade
               :quantidade / 100;

       totalNutricional = alimento.getInfoNutricional().multiplicarPor(fator);
       return totalNutricional;
    }

    private  double calcularValorProporcional(double valorBase){
        if(alimento.getUnidadeMedida() == UnidadeMedida.UNIDADE){
            return quantidade * valorBase;
        }
        return (quantidade / 100) * valorBase;
    }

    public double getCaloriasTotais() {
        return totalNutricional == null ? 0 : totalNutricional.getCalorias();
    }
}
