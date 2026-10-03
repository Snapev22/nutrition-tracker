package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.DataInvalidaException;
import br.edu.ifsp.exceptions.MetaCaloricaExcedidaException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.PlanoDiario;
import br.edu.ifsp.repository.PlanoDiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoDiarioServiceTest {

    @Mock
    private PlanoDiarioRepository planoDiarioRepository;

    @InjectMocks
    private PlanoDiarioService planoDiarioService;

    private Aluno criarAluno(double metaDefinida) {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setMetaCaloricaDefinida(metaDefinida);
        return aluno;
    }

    @Test
    void buscarOuCriarPlanoDoDia_deveLancarExcecaoQuandoDataPassada() {
        Aluno aluno = criarAluno(2000.0);
        LocalDate dataPassada = LocalDate.now().minusDays(1);

        assertThrows(DataInvalidaException.class,
                () -> planoDiarioService.buscarOuCriarPlanoDoDia(aluno, dataPassada));

        verify(planoDiarioRepository, never()).save(any());
    }

    @Test
    void buscarOuCriarPlanoDoDia_deveRetornarPlanoExistenteSemCriarNovo() {
        Aluno aluno = criarAluno(2000.0);
        LocalDate hoje = LocalDate.now();

        PlanoDiario planoExistente = new PlanoDiario();
        planoExistente.setAluno(aluno);
        planoExistente.setData(hoje);

        when(planoDiarioRepository.findByAlunoIdAndDataComItens(aluno.getId(), hoje))
                .thenReturn(Optional.of(planoExistente));

        PlanoDiario resultado = planoDiarioService.buscarOuCriarPlanoDoDia(aluno, hoje);

        assertEquals(planoExistente, resultado);
        verify(planoDiarioRepository, never()).save(any());
    }

    @Test
    void adicionarItem_deveLancarExcecaoQuandoExcedeMetaEIgnorarLimiteFalso() {
        Aluno aluno = criarAluno(1500.0); // meta baixa, fácil de estourar

        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(aluno);

        InformacaoNutricional info = new InformacaoNutricional(2000.0, 10.0, 50.0, 5.0);
        Alimento alimento = new Alimento();
        alimento.setInfoNutricional(info);

        assertThrows(MetaCaloricaExcedidaException.class,
                () -> planoDiarioService.adicionarItem(plano, alimento, 1.0, false));

        assertTrue(plano.getItens().isEmpty());
    }

    @Test
    void adicionarItem_deveAdicionarMesmoExcedendoQuandoIgnorarLimiteTrue() {
        Aluno aluno = criarAluno(1500.0);

        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(aluno);

        InformacaoNutricional info = new InformacaoNutricional(2000.0, 10.0, 50.0, 5.0);
        Alimento alimento = new Alimento();
        alimento.setInfoNutricional(info);

        assertDoesNotThrow(() ->
                planoDiarioService.adicionarItem(plano, alimento, 1.0, true));

        assertEquals(1, plano.getItens().size());
    }

    @Test
    void adicionarItem_deveAdicionarNormalmenteQuandoDentroDaMeta() {
        Aluno aluno = criarAluno(3000.0); // meta folgada

        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(aluno);

        InformacaoNutricional info = new InformacaoNutricional(500.0, 10.0, 50.0, 5.0);
        Alimento alimento = new Alimento();
        alimento.setInfoNutricional(info);

        assertDoesNotThrow(() ->
                planoDiarioService.adicionarItem(plano, alimento, 1.0, false));

        assertEquals(1, plano.getItens().size());
    }
}
