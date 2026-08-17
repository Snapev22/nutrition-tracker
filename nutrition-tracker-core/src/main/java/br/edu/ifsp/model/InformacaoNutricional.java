package br.edu.ifsp.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class InformacaoNutricional {
    private  double calorias;
    private  double proteina;
    private  double carboidrato;
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
