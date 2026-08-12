package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.repository.AlunoDao;

import java.util.List;

public class AlunoService {
    private final CalculoNutricaoService calculoNutricaoService;
    private final AlunoDao alunoDao;

    public AlunoService() {
        this.calculoNutricaoService = new CalculoNutricaoService();
        this.alunoDao = new AlunoDao();
    }

    public void cadastrar(Aluno novoAluno) {
        if(novoAluno == null){throw new RegraDeNegocioException("O aluno cadastrado não pode ser nulo");}

        double metaCaloricaEstimada =  calculoNutricaoService.calculaMetaCalorica(novoAluno);
        novoAluno.setMetaCaloricaEstimada(metaCaloricaEstimada);

        if(novoAluno.getMetaCaloricaDefinida() <=0){
            novoAluno.setMetaCaloricaDefinida(metaCaloricaEstimada);
        }

        alunoDao.inserir(novoAluno);
    }

    public List<Aluno> listar()  {
        return alunoDao.listarTodos();
    }

    public Aluno buscarPorId(Long idBuscado) {
        return alunoDao.buscarPorId(idBuscado)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado. ID: " + idBuscado));
    }


    public void alter(Aluno alunoAlterar) {
       if(alunoAlterar == null){throw new RegraDeNegocioException("O aluno pra alterar não pode ser nulo");}

        double metaCaloricaEstimada = calculoNutricaoService.calculaMetaCalorica(alunoAlterar);
        alunoAlterar.setMetaCaloricaEstimada(metaCaloricaEstimada);

        int linhasAfetadas = alunoDao.alterar(alunoAlterar);

        if(linhasAfetadas == 0){
            throw  new EntidadeNaoEncontradaException("Falha na alteração. Aluno com id: " + alunoAlterar.getId()
                    +  " não foi encontrado.");
        }
    }

    public void deletar(Long idRemover){
        int linhasAfetadas = alunoDao.remover(idRemover);
        if(linhasAfetadas == 0){
            throw  new EntidadeNaoEncontradaException("Falha na remoção. Aluno com id: " + idRemover
                    + " não foi   encontrado");
        }
    }


}
