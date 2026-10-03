package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return alimentoRepository.findAllByAtivoTrueOrderByNomeAsc();
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

    public Optional<Alimento> buscarPorId(Long id){
        return alimentoRepository.findById(id);
    }

    public void deletar(Long idRemover){
        Alimento alimento = alimentoRepository.findById(idRemover)
                .orElseThrow(() ->  new EntidadeNaoEncontradaException(
                    "Falha na remoção. Alimento com id: " + idRemover + " não foi encontrado."
            ));
        alimento.setAtivo(false);
        alimentoRepository.save(alimento);
    }
}
