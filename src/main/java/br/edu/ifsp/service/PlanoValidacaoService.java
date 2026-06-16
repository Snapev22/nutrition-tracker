package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.LimiteCaloricoExcedidoException;
import br.edu.ifsp.model.ItemPlano;
import br.edu.ifsp.model.PlanoDiario;

import java.util.Locale;

public class PlanoValidacaoService {

    public void validarAdicaoItem(PlanoDiario plano, ItemPlano novoItem){
        double metaCalorias = plano.getAluno().getMetaCalorias();
        double consumido = plano.calcularTotalCaloriasConsumidas();

        
        if(consumido +  novoItem.getCaloriasTotais() > metaCalorias){
            String mensagem = String.format(new Locale("pt", "BR"),
                    "Limite calórico excedido. Meta: %.2f kcal | Consumido: %.2f kcal | Tentativa: %.2f kcal",
                    metaCalorias, consumido, novoItem.getCaloriasTotais()
            );
            throw  new LimiteCaloricoExcedidoException(mensagem);
        }
    }
}
