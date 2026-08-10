package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.DataInvalidaException;
import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.model.*;
import br.edu.ifsp.repository.ItemPlanoDao;
import br.edu.ifsp.repository.PlanoDiarioDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PlanoDiarioService {
    private final PlanoValidacaoService planoValidacao;
    private final PlanoDiarioDao planoDiarioDao;
    private final ItemPlanoDao itemPlanoDao;

    public PlanoDiarioService() {
        this.planoValidacao = new PlanoValidacaoService();
        this.planoDiarioDao = new PlanoDiarioDao();
        this.itemPlanoDao = new ItemPlanoDao();
    }

    public PlanoDiario buscarOuCriarPlanoDoDia(Aluno aluno, LocalDate data) {
        return planoDiarioDao.buscarPorAlunoEData(aluno.getId(), data)
                .map(plano -> {
                    plano.setAluno(aluno);
                    var itens =  new ArrayList<>(itemPlanoDao.buscarPorPlano(plano.getId()));
                    plano.setItens(itens);
                    return plano;
                }).orElseGet(() -> {
                    if (data.isBefore(LocalDate.now())) {
                        throw new DataInvalidaException(
                                "Não é possível criar um novo plano para uma data passada (" + data
                                        + "). Planos antigos só podem ser consultados se já existirem.");
                    }
                    PlanoDiario novoPlano = new PlanoDiario();
                    novoPlano.setAluno(aluno);
                    novoPlano.setData(data);
                    novoPlano.setItens(new ArrayList<>());
                    planoDiarioDao.inserirPlanoDiario(novoPlano);
                    return novoPlano;
                });
    }

    public void adicionarItem(PlanoDiario plano, Alimento alimento, double quantidade){
        Optional<ItemPlano> itemExistente = plano.getItens().stream()
                .filter(i -> i.getAlimento().getId().equals(alimento.getId()))
                .findFirst();

        if(itemExistente.isPresent()){
            ItemPlano item = itemExistente.get();

            double novoQuantidade = item.getQuantidade() + quantidade;
            item.setQuantidade(novoQuantidade);
            item.calcularTotalNutricional();

            planoValidacao.validarAdicaoItem(plano, item);
            itemPlanoDao.atualizarQuantidade(item.getId(), item.getQuantidade(), item.getCaloriasTotais());
        }else{
            ItemPlano novoItem = new ItemPlano();
            novoItem.setAlimento(alimento);
            novoItem.setQuantidade(quantidade);
            novoItem.calcularTotalNutricional();

            planoValidacao.validarAdicaoItem(plano, novoItem);

            itemPlanoDao.inserirItem(novoItem, plano.getId());

            plano.getItens().add(novoItem);
        }

    }

    public void removerItem(PlanoDiario plano, Long idRemover){
        int linhasAfetadas = itemPlanoDao.removerItem(idRemover);
        if(linhasAfetadas == 0){
            throw new EntidadeNaoEncontradaException("Erro na remoção: item com id: "
                    + idRemover + " não encontrado.");
        }

        plano.getItens().removeIf(i ->
                i.getId().equals(idRemover));
    }

    public InformacaoNutricional calcularResumoNutricional(PlanoDiario plano){
        return itemPlanoDao.somarResumoPorPlano(plano.getId());
    }
}
