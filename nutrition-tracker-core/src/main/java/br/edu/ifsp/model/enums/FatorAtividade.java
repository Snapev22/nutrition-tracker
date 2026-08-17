package br.edu.ifsp.model.enums;

public enum FatorAtividade {
    SEDENTARIO(1.2),
    LEVEMENTE_ATIVO(1.375),
    MODERADAMENTE_ATIVO(1.55),
    MUITO_ATIVO(1.725),
    EXTREMAMENTE_ATIVO(1.9);

    private final double valor;

    FatorAtividade(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
