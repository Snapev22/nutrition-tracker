package br.edu.ifsp.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlanoDiario {
    @EqualsAndHashCode.Include
    private Long id;

    private Aluno aluno;
    private LocalDate data;
    private List<ItemPlano> itens =  new ArrayList<>();


    public double calcularTotalCaloriasConsumidas(){
        double totalCalorias = 0;
        for (ItemPlano item : itens){
            totalCalorias += item.calcularCaloriasTotais();
        }

        return totalCalorias;
    }
}

