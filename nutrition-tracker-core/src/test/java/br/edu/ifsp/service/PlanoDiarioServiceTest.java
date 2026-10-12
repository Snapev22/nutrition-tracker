package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.DataInvalidaException;
import br.edu.ifsp.exceptions.MetaCaloricaExcedidaException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.PlanoDiario;
import br.edu.ifsp.repository.ItemPlanoRepository;
import br.edu.ifsp.repository.PlanoDiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.edu.ifsp.model.ItemPlano;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanoDiarioServiceTest {

    @Mock
    private PlanoDiarioRepository planoDiarioRepository;

    @Mock
    private ItemPlanoRepository itemPlanoRepository;

    @Mock
    private PlanoValidacaoService planoValidacaoService;

    @InjectMocks
    private PlanoDiarioService planoDiarioService;

    private Aluno criarAluno(double metaDefinida) {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setMetaCaloricaDefinida(metaDefinida);
        return aluno;
    }

    private Alimento criarAlimento(Long id) {
        Alimento alimento = new Alimento();
        alimento.setId(id);
        alimento.setInfoNutricional(new InformacaoNutricional(500.0, 10.0, 50.0, 5.0));
        return alimento;
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
    void adicionarItem_naoDeveAdicionarQuandoValidacaoLancaExcecao() {
        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(criarAluno(1500.0));
        Alimento alimento = criarAlimento(1L);

        // instrui o "fiscal" a reclamar
        doThrow(MetaCaloricaExcedidaException.class)
                .when(planoValidacaoService).validarAdicaoItem(eq(plano), any(ItemPlano.class));

        assertThrows(MetaCaloricaExcedidaException.class,
                () -> planoDiarioService.adicionarItem(plano, alimento, 1.0, false));

        assertTrue(plano.getItens().isEmpty());
        verify(itemPlanoRepository, never()).save(any());
    }

    @Test
    void adicionarItem_deveValidarEAdicionarQuandoIgnorarLimiteFalso() {
        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(criarAluno(3000.0));
        Alimento alimento = criarAlimento(1L);

        planoDiarioService.adicionarItem(plano, alimento, 1.0, false);

        verify(planoValidacaoService).validarAdicaoItem(eq(plano), any(ItemPlano.class));
        verify(itemPlanoRepository).save(any(ItemPlano.class));
        assertEquals(1, plano.getItens().size());
    }

    @Test
    void adicionarItem_deveAdicionarSemValidarQuandoIgnorarLimiteTrue() {
        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(criarAluno(1500.0));
        Alimento alimento = criarAlimento(1L);

        planoDiarioService.adicionarItem(plano, alimento, 1.0, true);

        verify(planoValidacaoService, never()).validarAdicaoItem(any(), any());
        verify(itemPlanoRepository).save(any(ItemPlano.class));
        assertEquals(1, plano.getItens().size());
    }

    @Test
    void adicionarItem_deveMesclarQuantidadeQuandoAlimentoJaEstaNoPlano() {
        PlanoDiario plano = new PlanoDiario();
        plano.setAluno(criarAluno(3000.0));
        Alimento alimento = criarAlimento(1L);

        ItemPlano existente = new ItemPlano();
        existente.setAlimento(alimento);
        existente.setQuantidade(100.0);
        plano.getItens().add(existente);

        planoDiarioService.adicionarItem(plano, alimento, 50.0, true);

        assertEquals(1, plano.getItens().size());   // não duplicou
        assertEquals(150.0, existente.getQuantidade());
        verify(itemPlanoRepository).save(existente);
    }
}
