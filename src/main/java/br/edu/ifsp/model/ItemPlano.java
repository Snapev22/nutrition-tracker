package br.edu.ifsp.model;

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
        caloriasTotais = quantidade * alimento.getCalorias();
        return caloriasTotais;
    }


}
