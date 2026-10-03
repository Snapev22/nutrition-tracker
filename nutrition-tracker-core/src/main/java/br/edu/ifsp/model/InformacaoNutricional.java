package br.edu.ifsp.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class InformacaoNutricional {
    @PositiveOrZero
    private  double calorias;

    @PositiveOrZero
    private  double proteina;

    @PositiveOrZero
    private  double carboidrato;

    @PositiveOrZero
    private  double gordura;

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
