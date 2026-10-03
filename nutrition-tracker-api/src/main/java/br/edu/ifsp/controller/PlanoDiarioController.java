package br.edu.ifsp.controller;

import br.edu.ifsp.dto.requests.ItemPlanoRequestDTO;
import br.edu.ifsp.dto.requests.PlanoDiarioRequestDTO;
import br.edu.ifsp.dto.responses.PlanoDiarioResponseDTO;
import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.PlanoDiario;
import br.edu.ifsp.service.AlimentoService;
import br.edu.ifsp.service.AlunoService;
import br.edu.ifsp.service.PlanoDiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/alunos/{alunoId}/planos-diarios")
@RequiredArgsConstructor
public class PlanoDiarioController {
    private final AlunoService alunoService;
    private final AlimentoService alimentoService;
    private final PlanoDiarioService planoDiarioService;

    @PostMapping
    public ResponseEntity<PlanoDiarioResponseDTO> cadastrar(@PathVariable Long alunoId,
                                                            @Valid @RequestBody PlanoDiarioRequestDTO dto){
        Aluno aluno = buscarAlunoOuFalhar(alunoId);
        PlanoDiario plano = planoDiarioService.buscarOuCriarPlanoDoDia(aluno, dto.data());

        return ResponseEntity
                .created(URI.create("/api/v1/alunos/" + alunoId + "/planos-diarios/" + plano.getData()))
                .body(montarResposta(plano));
    }


    @GetMapping("/{data}")
    public ResponseEntity<PlanoDiarioResponseDTO> buscarPorData(@PathVariable Long alunoId,
                                                                @PathVariable LocalDate data){
        Aluno aluno = buscarAlunoOuFalhar(alunoId);
        return planoDiarioService.buscarPlanoParaConsulta(aluno, data)
                .map(this::montarResposta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{data}/itens")
    public ResponseEntity<PlanoDiarioResponseDTO> adcionarItem(@PathVariable Long alunoId,
                                                               @PathVariable LocalDate data,
                                                               @RequestParam(defaultValue = "false") boolean ignorarLimite,
                                                               @Valid @RequestBody ItemPlanoRequestDTO dto){
        Aluno aluno = buscarAlunoOuFalhar(alunoId);
        PlanoDiario plano = planoDiarioService.buscarOuCriarPlanoDoDia(aluno, data);

        Alimento alimento = alimentoService.buscarPorId(dto.alimentoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Alimento com id: " + dto.alimentoId() + " não foi encontrado."));

        planoDiarioService.adicionarItem(plano, alimento, dto.quantidade(), ignorarLimite);

        return ResponseEntity.ok(montarResposta(plano));
    }

    @DeleteMapping("/{data}/itens/{itemId}")
    public ResponseEntity<PlanoDiarioResponseDTO> removerItem(@PathVariable Long alunoId,
                                                              @PathVariable LocalDate data,
                                                              @PathVariable Long itemId){
        Aluno aluno = buscarAlunoOuFalhar(alunoId);
        PlanoDiario plano = planoDiarioService.buscarPlanoParaConsulta(aluno, data)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Plano não encontrado para o aluno " + alunoId + " na data " + data));

        planoDiarioService.removerItem(plano, itemId);

        return ResponseEntity.ok(montarResposta(plano));
    }

    private PlanoDiarioResponseDTO montarResposta(PlanoDiario plano) {
        return PlanoDiarioResponseDTO.fromEntity(plano, planoDiarioService.calcularResumoNutricional(plano));
    }

    private Aluno buscarAlunoOuFalhar(Long alunoId){
        return alunoService.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Aluno com id: " + alunoId + " não foi encontrado."));
    }
}
