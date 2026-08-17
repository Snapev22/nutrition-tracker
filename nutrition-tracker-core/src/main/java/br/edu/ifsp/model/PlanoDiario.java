package br.edu.ifsp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "plano_diario")
public class PlanoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    @EqualsAndHashCode.Include
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @OneToMany(mappedBy = "planoDiario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPlano> itens =  new ArrayList<>();

    private LocalDate data;

    public double calcularTotalCaloriasConsumidas(){
        double totalCalorias = 0;
        for (ItemPlano item : itens){
            totalCalorias += item.calcularTotalNutricional().getCalorias();
        }
        return totalCalorias;
    }
}

