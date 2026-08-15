package br.edu.ifsp.repository;

import br.edu.ifsp.model.PlanoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PlanoDiarioRepository extends JpaRepository<PlanoDiario, Long> {


    Optional<PlanoDiario> findByAlunoIdAndData(Long alunoID, LocalDate data);
}
