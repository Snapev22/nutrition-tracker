package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.DataInvalidaException;
import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.model.*;
import br.edu.ifsp.repository.ItemPlanoDao;
import br.edu.ifsp.repository.ItemPlanoRepository;
import br.edu.ifsp.repository.PlanoDiarioDao;
import br.edu.ifsp.repository.PlanoDiarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanoDiarioService {
    private final PlanoValidacaoService planoValidacao;
    private final PlanoDiarioRepository planoDiarioRepository;
    private final ItemPlanoRepository itemPlanoRepository;

    public PlanoDiario buscarOuCriarPlanoDoDia(Aluno aluno, LocalDate data) {
        return planoDiarioRepository.findByAlunoAndData(aluno.getId(), data)
                .orElseGet(() -> {
                    if (data.isBefore(LocalDate.now())) {
                        throw new DataInvalidaException(
                                "Não é possível criar um novo plano para uma data passada (" + data
                                        + "). Planos antigos só podem ser consultados se já existirem.");
                    }
                    PlanoDiario novoPlano = new PlanoDiario();
                    novoPlano.setAluno(aluno);
                    novoPlano.setData(data);
                    return planoDiarioRepository.save(novoPlano);
                });
    }

    public void adicionarItem(PlanoDiario plano, Alimento alimento, double quantidadeAdcional,   boolean ignorarLimiteCalorico){
        ItemPlano itemParaValidar = new ItemPlano();
        itemParaValidar.setAlimento(alimento);
        itemParaValidar.setQuantidade(quantidadeAdcional);
        itemParaValidar.calcularTotalNutricional();

        if (!ignorarLimiteCalorico) {
            planoValidacao.validarAdicaoItem(plano, itemParaValidar);
        }


        Optional<ItemPlano> itemExistente = plano.getItens().stream()
                .filter(i -> i.getAlimento().getId().equals(alimento.getId()))
                .findFirst();

        if(itemExistente.isPresent()){
            ItemPlano item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidadeAdcional);
            item.calcularTotalNutricional();
            itemPlanoRepository.save(item);
        }else{
            itemParaValidar.setPlanoDiario(plano);
            itemPlanoRepository.save(itemParaValidar);
            plano.getItens().add(itemParaValidar);
        }

    }
    // Overload — mantém a assinatura antiga funcionando (chamadas existentes não quebram),
    // sempre validando (comportamento padrão = não ignora o limite).
    public void adicionarItem(PlanoDiario plano, Alimento alimento, double quantidadeAdicional) {
        adicionarItem(plano, alimento, quantidadeAdicional, false);
    }

    public void removerItem(PlanoDiario plano, Long idRemover){
        if(!itemPlanoRepository.existsById(idRemover)){
            throw new EntidadeNaoEncontradaException("Erro na remoção: item com id: "
                    + idRemover + " não encontrado.");
        }

        itemPlanoRepository.deleteById(idRemover);
        plano.getItens().removeIf(i ->
                i.getId().equals(idRemover));
    }

    public InformacaoNutricional calcularResumoNutricional(PlanoDiario plano){
        return itemPlanoRepository.somarResumoPorPlano(plano.getId());
    }
}
