package br.edu.ifsp.repository;

import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.ItemPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemPlanoRepository extends JpaRepository<ItemPlano, Long> {


    List<ItemPlano> findByPlanoDiarioId(Long planoDiarioId);

    @Query("""
            SELECT new br.edu.ifsp.model.InformacaoNutricional(
                COALESCE(SUM(i.totalNutricional.calorias), 0),
                COALESCE(SUM(i.totalNutricional.proteina), 0),
                COALESCE(SUM(i.totalNutricional.carboidrato), 0),
                COALESCE(SUM(i.totalNutricional.gordura), 0)
            )
            FROM ItemPlano i
            WHERE i.planoDiario.id = :planoId
            """)
    InformacaoNutricional somarResumoPorPlano(@Param ("planoId") Long planoId);
}
