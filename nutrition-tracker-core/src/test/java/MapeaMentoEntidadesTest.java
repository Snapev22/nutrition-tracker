import br.edu.ifsp.TestApplication01;
import br.edu.ifsp.model.Alimento;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = TestApplication01.class)
@ActiveProfiles("test")
class MapeaMentoEntidadesTest {


    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void deveConsultarAlimentosSemErro() {
        List<Alimento> alimentos = entityManager
                .createQuery("SELECT a FROM Alimento a", Alimento.class)
                .getResultList();

        System.out.println("Alimentos encontrados: " + alimentos.size());
        assertNotNull(alimentos);
    }
}