package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlimentoService {
    private final AlimentoRepository alimentoRepository;


    public void cadastrar(Alimento novoAliemnto){
        if(novoAliemnto == null){
            throw new RegraDeNegocioException("O alimento cadastrado não pode ser nulo.");
        }
        alimentoRepository.save(novoAliemnto);
    }

    public List<Alimento> listar(){
        return alimentoRepository.findAll();
    }

    public void alterar(Alimento alimentoAlterar) {
        if (alimentoAlterar == null) {
            throw new RegraDeNegocioException("O alimento para alterar não pode ser nulo.");
        }

        if (!alimentoRepository.existsById(alimentoAlterar.getId())) {
            throw new EntidadeNaoEncontradaException(
                    "Falha na alteração. Alimento com id: " + alimentoAlterar.getId() + " não foi encontrado."
            );
        }

        alimentoRepository.save(alimentoAlterar);
    }

    public void deletar(Long idRemover){
        if(!alimentoRepository.existsById(idRemover)){
            throw new EntidadeNaoEncontradaException(
                    "Falha na remoção. Alimento com id: " + idRemover + " não foi encontrado."
            );
        }
        alimentoRepository.deleteById(idRemover);
    }
}
