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
    private double caloriasTotais;

    public double calcularCaloriasTotais(){
        caloriasTotais = calcularValorProporcional(alimento.getCalorias());
        return caloriasTotais;
    }

    private  double calcularValorProporcional(double valorBase){
        if(alimento.getUnidadeMedida() == UnidadeMedida.UNIDADE){
            return quantidade * valorBase;
        }
        return (quantidade / 100) * valorBase;
    }
}
