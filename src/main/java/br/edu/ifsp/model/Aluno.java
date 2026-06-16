package br.edu.ifsp.model;

import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;

public class Aluno {
    private Long id;
    private String nome;
    private int idade;
    private double peso;
    private double altura;
    private Sexo sexo;
    private FatorAtividade fatorAtividade;
    private Objetivo objetivo;
    private double metaCalorias;

    public Aluno() {
    }

    public Aluno(Long id, String nome, int idade, double peso, double altura, Sexo sexo,
                 FatorAtividade fatorAtividade, Objetivo objetivo, double metaCalorias
                ){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
        this.fatorAtividade = fatorAtividade;
        this.objetivo = objetivo;
        this.metaCalorias = metaCalorias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public FatorAtividade getFatorAtividade() {
        return fatorAtividade;
    }

    public void setFatorAtividade(FatorAtividade fatorAtividade) {
        this.fatorAtividade = fatorAtividade;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public double getMetaCalorias() {
        return metaCalorias;
    }

    public void setMetaCalorias(double metaCalorias) {
        this.metaCalorias = metaCalorias;
    }
}
