package br.edu.ifsp.repository;

import br.edu.ifsp.model.ItemPlano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPlanoRepository extends JpaRepository<ItemPlano, Long> {


    List<ItemPlano> findByPlanoDiarioId(Long planoDiarioId);
}
