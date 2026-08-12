package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.MetaCaloricaExcedidaException;
import br.edu.ifsp.model.ItemPlano;
import br.edu.ifsp.model.PlanoDiario;

import java.util.Locale;

public class PlanoValidacaoService {

    public void validarAdicaoItem(PlanoDiario plano, ItemPlano novoItem){
        double metaCalorias = plano.getAluno().getMetaCaloricaDefinida();
        double consumido = plano.calcularTotalCaloriasConsumidas();

        
        if(consumido +  novoItem.getCaloriasTotais() > metaCalorias){
            String mensagem = String.format(new Locale("pt", "BR"),
                    "Você está ultrapassando a meta calórica que definiu para este paciente. Meta: %.2f kcal " +
                            "| Consumido: %.2f kcal | Tentativa: %.2f kcal",
                    metaCalorias, consumido, novoItem.getCaloriasTotais()
            );
            throw  new MetaCaloricaExcedidaException(mensagem);
        }
    }
}
