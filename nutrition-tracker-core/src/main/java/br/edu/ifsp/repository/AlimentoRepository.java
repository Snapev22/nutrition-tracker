package br.edu.ifsp.repository;

import br.edu.ifsp.model.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {
    List<Alimento> findAllByOrderByNomeAsc();
}
