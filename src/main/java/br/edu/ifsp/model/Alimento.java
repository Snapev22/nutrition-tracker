package br.edu.ifsp.model;

public class Alimento {
    private Long id;
    private String nome;
    private double proteina;
    private double gordura;
    private double carboidrato;
    private double calorias;

    public Alimento() {
    }

    public Alimento(Long id, String nome, double proteina, double gordura, double carboidrato, double calorias) {
        this.id = id;
        this.nome = nome;
        this.proteina = proteina;
        this.gordura = gordura;
        this.carboidrato = carboidrato;
        this.calorias = calorias;
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

    public double getProteina() {
        return proteina;
    }

    public void setProteina(double proteina) {
        this.proteina = proteina;
    }

    public double getGordura() {
        return gordura;
    }

    public void setGordura(double gordura) {
        this.gordura = gordura;
    }

    public double getCarboidrato() {
        return carboidrato;
    }

    public void setCarboidrato(double carboidrato) {
        this.carboidrato = carboidrato;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }
}
