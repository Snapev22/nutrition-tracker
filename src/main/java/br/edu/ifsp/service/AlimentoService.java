package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.repository.AlimentoDao;

import java.util.List;

public class AlimentoService {
    private final AlimentoDao alimentoDao;

    public AlimentoService() {
        this.alimentoDao = new AlimentoDao();
    }

    public void cadastrar(Alimento novoAliemnto){
        if(novoAliemnto == null){
            throw new RegraDeNegocioException("O alimento cadastrado não pode ser nulo.");
        }
        alimentoDao.inserir(novoAliemnto);
    }

    public List<Alimento> listar(){
        return alimentoDao.listarTodosAlimentos();
    }

    public void alterar(Alimento alimentoAlterar) {
        if (alimentoAlterar == null) {
            throw new RegraDeNegocioException("O alimento para alterar não pode ser nulo.");
        }
        int linhasAfetadas = alimentoDao.alterarAlimento(alimentoAlterar);
        if (linhasAfetadas == 0) {
            throw new EntidadeNaoEncontradaException(
                    "Falha na alteração. Alimento com id: " + alimentoAlterar.getId() + " não foi encontrado."
            );
        }
    }

    public void deletar(Long idRemover){
        int linhasAfetadas = alimentoDao.removerAlimento(idRemover);
        if(linhasAfetadas == 0){
            throw new EntidadeNaoEncontradaException(
                    "Falha na remoção. Alimento com id: " + idRemover + " não foi encontrado."
            );
        }
    }
}
