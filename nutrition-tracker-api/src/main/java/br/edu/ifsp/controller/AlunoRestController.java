package br.edu.ifsp.controller;

import br.edu.ifsp.dto.requests.AlunoRequestDTO;
import br.edu.ifsp.dto.responses.AlunoResponseDTO;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alunos")
@RequiredArgsConstructor
public class AlunoRestController {

    private final AlunoService alunoService;

    @GetMapping
    public List<AlunoResponseDTO> listar(){
        return  alunoService.listar().stream()
                .map(AlunoResponseDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id){
        return alunoService.buscarPorId(id)
                .map(AlunoResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrar(@Valid @RequestBody AlunoRequestDTO alunoRequestDTO){
        Aluno aluno = alunoRequestDTO.toEntity();
        alunoService.cadastrar(aluno);
        return ResponseEntity
                .created(URI.create("/api/v1/alunos/" + aluno.getId()))
                .body(AlunoResponseDTO.fromEntity(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> alterar(@PathVariable Long id,
                                                    @Valid @RequestBody AlunoRequestDTO alunoRequestDTO){
        Aluno aluno = alunoRequestDTO.toEntity();
        aluno.setId(id);
        alunoService.alter(aluno);
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
