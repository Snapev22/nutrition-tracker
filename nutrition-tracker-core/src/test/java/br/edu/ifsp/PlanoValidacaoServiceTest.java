package br.edu.ifsp;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.ItemPlano;
import br.edu.ifsp.model.PlanoDiario;
import br.edu.ifsp.service.PlanoValidacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanoValidacaoServiceTest {

    private PlanoValidacaoService service;
    private Aluno aluno;
    private PlanoDiario plano;
    private List<ItemPlano> itensDoPlano;

    @BeforeEach
    void setUp() {
        service = new PlanoValidacaoService();

        aluno = new Aluno();
        aluno.setMetaCaloricaEstimada(2000.0);

        itensDoPlano = new ArrayList<>();

        plano = new PlanoDiario();
        plano.setAluno(aluno);
        plano.setItens(itensDoPlano);
    }

    /*
    @Test
    void devePermitirAdicionarItemQuandoNaoExcederLimite() {

        Alimento cafe = new Alimento();
        cafe.setCalorias(400.0);

        ItemPlano itemExistente = new ItemPlano(1L, cafe, 3.0, 0.0);
        itemExistente.calcularCaloriasTotais();
        itensDoPlano.add(itemExistente);

        ItemPlano novoItem = new ItemPlano(2L, cafe, 1.25, 0.0);
        novoItem.calcularCaloriasTotais();

        assertDoesNotThrow(() -> service.validarAdicaoItem(plano, novoItem));
    }

    @Test
    void deveLancarexcecaoQuandoExcederLimiteCalorico() {
        // Cenário:
        // 1. Criamos um alimento de 600 kcal
        Alimento almoco = new Alimento();
        almoco.setCalorias(600.0);

        // 2. Colocamos 3 porções dele no plano atual -> Total consumido: 1800 kcal
        ItemPlano itemExistente = new ItemPlano(1L, almoco, 3.0, 0.0);
        itemExistente.calcularCaloriasTotais();
        itensDoPlano.add(itemExistente);

        // 3. Novo item que tentaremos adicionar -> Tentativa: 300 kcal
        ItemPlano novoItem = new ItemPlano(2L, almoco, 0.5, 0.0);
        novoItem.calcularCaloriasTotais();

        // Execução e Validação:
        // Total (1800 + 300 = 2100) excede a Meta (2000). Deve estourar a exceção.
        LimiteCaloricoExcedidoException excecao = assertThrows(
                LimiteCaloricoExcedidoException.class,
                () -> service.validarAdicaoItem(plano, novoItem)
        );

        // Valida se a mensagem da exceção foi gerada com as formatações e valores corretos
        String mensagemEsperada = "Limite calórico excedido. Meta: 2000,00 kcal | Consumido: 1800,00 kcal | Tentativa: 300,00 kcal";
        assertEquals(mensagemEsperada, excecao.getMessage());
    }

     */
}
