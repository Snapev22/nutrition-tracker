package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final CalculoNutricaoService calculoNutricaoService;
    private final AlunoRepository alunoRepository;


    public void cadastrar(Aluno novoAluno) {
        if(novoAluno == null){throw new RegraDeNegocioException("O aluno cadastrado não pode ser nulo");}

        double metaCaloricaEstimada =  calculoNutricaoService.calculaMetaCalorica(novoAluno);
        novoAluno.setMetaCaloricaEstimada(metaCaloricaEstimada);

        if(novoAluno.getMetaCaloricaDefinida() <=0){
            novoAluno.setMetaCaloricaDefinida(metaCaloricaEstimada);
        }

        alunoRepository.save(novoAluno);
    }

    public List<Aluno> listar()  {

        return alunoRepository.findAllByOrderByNomeAsc();
    }

    public Optional<Aluno> buscarPorId(Long idBuscado) {
        return alunoRepository.findById(idBuscado);
    }


    public void alter(Aluno alunoAlterar) {
       if(alunoAlterar == null){throw new RegraDeNegocioException("O aluno pra alterar não pode ser nulo");}

       if(!alunoRepository.existsById(alunoAlterar.getId())){
           throw  new EntidadeNaoEncontradaException("Falha na alteração. Aluno com id: " + alunoAlterar.getId()
                   +  " não foi encontrado.");
       }

        double metaCaloricaEstimada = calculoNutricaoService.calculaMetaCalorica(alunoAlterar);
        alunoAlterar.setMetaCaloricaEstimada(metaCaloricaEstimada);

        alunoRepository.save(alunoAlterar);
    }

    public void deletar(Long idRemover){
        if(!alunoRepository.existsById(idRemover)){
            throw  new EntidadeNaoEncontradaException("Falha na remoção. Aluno com id: " + idRemover
                    + " não foi   encontrado");
        }
        alunoRepository.deleteById(idRemover);
    }


}
