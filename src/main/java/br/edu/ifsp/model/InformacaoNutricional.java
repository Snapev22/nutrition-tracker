package br.edu.ifsp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InformacaoNutricional {
    private final double calorias;
    private final double proteina;
    private final double carboidrato;
    private final double gordura;

    public InformacaoNutricional multiplicarPor(double fator) {
        if (Double.isNaN(fator) || Double.isInfinite(fator) || fator < 0) {
            throw new IllegalArgumentException("O fator deve ser um número finito e não negativo.");
        }

        return new  InformacaoNutricional(
                calorias * fator,
                proteina * fator,
                carboidrato * fator,
                gordura * fator
        );
    }

    public static InformacaoNutricional zero() {
        return new InformacaoNutricional(0, 0, 0, 0);
    }
}
