package br.edu.ifsp.repository;

import br.edu.ifsp.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findAllByOrderByNomeAsc();
}
