package br.edu.ifsp.model;

public class ItemPlano {
    private Long id;
    private Alimento  alimento;
    private double quantidade ;
    private double caloriasTotais;

    public ItemPlano() {
    }

    public ItemPlano(Long id, Alimento alimento, double quantidade, double caloriasTotais) {
        this.id = id;
        this.alimento = alimento;
        this.quantidade = quantidade;
        this.caloriasTotais = caloriasTotais;
    }

    public double calcularCaloriasTotais(){
        caloriasTotais = quantidade * alimento.getCalorias();
        return caloriasTotais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getCaloriasTotais() {
        return caloriasTotais;
    }

    public void setCaloriasTotais(double caloriasTotais) {
        this.caloriasTotais = caloriasTotais;
    }
}
