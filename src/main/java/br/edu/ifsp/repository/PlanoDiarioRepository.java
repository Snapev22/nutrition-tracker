package br.edu.ifsp.repository;

import br.edu.ifsp.model.PlanoDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PlanoDiarioRepository extends JpaRepository<PlanoDiario, Long> {


    @Query("""
            SELECT p FROM PlanoDiario p
            JOIN FETCH p.aluno
            LEFT JOIN FETCH p.itens i
            LEFT JOIN FETCH i.alimento
            WHERE p.aluno.id = :alunoId AND p.data = :data
            """)
    Optional<PlanoDiario> findByAlunoIdAndDataComItens(@Param("alunoId") Long alunoID, LocalDate data);
}
