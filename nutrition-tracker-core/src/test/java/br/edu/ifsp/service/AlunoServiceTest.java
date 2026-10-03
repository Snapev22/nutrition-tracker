package br.edu.ifsp.service;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {
    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CalculoNutricaoService calculoNutricaoService;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void cadastrar_deveCalcularMetaEstimadaEUsarComoDefinidaQuandoNaoInformada() {
        // dado
        Aluno aluno = new Aluno();
        aluno.setPeso(70.0);
        aluno.setAltura(175.0);
        aluno.setIdade(25);
        aluno.setMetaCaloricaDefinida(0.0); // não informada

        when(calculoNutricaoService.calculaMetaCalorica(aluno)).thenReturn(2000.0);

        // quando
        alunoService.cadastrar(aluno);

        // então
        assertEquals(2000.0, aluno.getMetaCaloricaEstimada());
        assertEquals(2000.0, aluno.getMetaCaloricaDefinida());
        verify(alunoRepository).save(aluno);
    }

    @Test
    void cadastrar_naoDeveSobrescreverMetaDefinidaQuandoInformadaPeloUsuario() {
        Aluno aluno = new Aluno();
        aluno.setPeso(70.0);
        aluno.setAltura(175.0);
        aluno.setIdade(25);
        aluno.setMetaCaloricaDefinida(1800.0); // nutricionista definiu manualmente

        when(calculoNutricaoService.calculaMetaCalorica(aluno)).thenReturn(2000.0);

        alunoService.cadastrar(aluno);

        assertEquals(2000.0, aluno.getMetaCaloricaEstimada()); // estimada muda
        assertEquals(1800.0, aluno.getMetaCaloricaDefinida());  // definida NÃO muda
    }

    @Test
    void deletar_deveDesativarAlunoEmVezDeExcluir() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setAtivo(true);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        alunoService.deletar(1L);

        assertFalse(aluno.isAtivo());
        verify(alunoRepository).save(aluno);
        verify(alunoRepository, never()).deleteById(any());
    }

    @Test
    void deletar_deveLancarExcecaoQuandoAlunoNaoExiste() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class,
                () -> alunoService.deletar(99L));

        verify(alunoRepository, never()).save(any());
    }


    @Test
    void alter_deveRecalcularMetaEstimadaESalvar() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setPeso(68.0);

        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(calculoNutricaoService.calculaMetaCalorica(aluno)).thenReturn(1950.0);

        alunoService.alter(aluno);

        assertEquals(1950.0, aluno.getMetaCaloricaEstimada());
        verify(alunoRepository).save(aluno);
    }

    @Test
    void alter_deveLancarExcecaoQuandoAlunoNaoExiste() {
        Aluno aluno = new Aluno();
        aluno.setId(99L);

        when(alunoRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntidadeNaoEncontradaException.class,
                () -> alunoService.alter(aluno));

        verify(alunoRepository, never()).save(any());
    }
}
