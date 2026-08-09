package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.model.*;
import br.edu.ifsp.repository.ItemPlanoDao;
import br.edu.ifsp.repository.PlanoDiarioDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class PlanoDiarioService {
    private final PlanoValidacaoService planoValidacao;
    private final PlanoDiarioDao planoDiarioDao;
    private final ItemPlanoDao itemPlanoDao;

    public PlanoDiarioService() {
        this.planoValidacao = new PlanoValidacaoService();
        this.planoDiarioDao = new PlanoDiarioDao();
        this.itemPlanoDao = new ItemPlanoDao();
    }

    public PlanoDiario buscarOuCriarPlanoDoDia(Aluno aluno, LocalDate data){
        return planoDiarioDao.buscarPorAlunoEData(aluno.getId(), data)
                .map(plano -> {
                    plano.setAluno(aluno);
                    var itens =  new ArrayList<>(itemPlanoDao.buscarPorPlano(plano.getId()));
                    plano.setItens(itens);
                    return plano;
                }).orElseGet(() -> {
                    PlanoDiario novoPlano = new PlanoDiario();
                    novoPlano.setAluno(aluno);
                    novoPlano.setData(data);
                    novoPlano.setItens(new ArrayList<>());
                    planoDiarioDao.inserirPlanoDiario(novoPlano);
                    return novoPlano;
                });
    }

    public void adicionarItem(PlanoDiario plano, Alimento alimento, double quantidade){
        ItemPlano item = new ItemPlano();
        item.setAlimento(alimento);
        item.setQuantidade(quantidade);
        item.calcularTotalNutricional();

        planoValidacao.validarAdicaoItem(plano, item);
        itemPlanoDao.inserirItem(item, plano.getId());
        plano.getItens().add(item);
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
