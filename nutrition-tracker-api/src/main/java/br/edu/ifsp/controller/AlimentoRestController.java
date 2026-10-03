package br.edu.ifsp.controller;

import br.edu.ifsp.dto.requests.AlimentoRequestDTO;
import br.edu.ifsp.dto.responses.AlimentoResponseDTO;
import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.service.AlimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alimentos")
@RequiredArgsConstructor
public class AlimentoRestController {
    private final AlimentoService alimentoService;

    @GetMapping
    public List<AlimentoResponseDTO> listar(){
        return alimentoService.listar().stream()
                .map(AlimentoResponseDTO::fromEntity)
                .toList();
    }


    @PostMapping
    public ResponseEntity<AlimentoResponseDTO> cadastrar(@Valid @RequestBody AlimentoRequestDTO alimentoRequestDTO){
        Alimento alimento = alimentoRequestDTO.toEntity();
        alimentoService.cadastrar(alimento);

        return ResponseEntity
                .created(URI.create("/api/v1/alimentos/" + alimento.getId()))
                .body(AlimentoResponseDTO.fromEntity(alimento));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AlimentoResponseDTO> alterar(@PathVariable Long id,
                                                    @Valid @RequestBody AlimentoRequestDTO alimentoRequestDTO){
        Alimento alimento = alimentoRequestDTO.toEntity();
        alimento.setId(id);
        alimentoService.alterar(alimento);
        return ResponseEntity.ok(AlimentoResponseDTO.fromEntity(alimento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        alimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
