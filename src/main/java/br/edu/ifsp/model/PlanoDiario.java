package br.edu.ifsp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanoDiario {
    private Long id;
    private Aluno aluno;
    private LocalDate data;
    private List<ItemPlano> itens =  new ArrayList<>();

    public PlanoDiario() {
    }

    public PlanoDiario(Long id, Aluno aluno, LocalDate data, List<ItemPlano> itens) {
        this.id = id;
        this.aluno = aluno;
        this.data = data;
        this.itens = itens;
    }

    public double calcularTotalCaloriasConsumidas(){
        double totalCalorias = 0;
        for (ItemPlano item : itens){
            totalCalorias += item.calcularCaloriasTotais();
        }

        return totalCalorias;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemPlano> getItens() {
        return itens;
    }

    public void setItens(List<ItemPlano> itens) {
        this.itens = itens;
    }
}

