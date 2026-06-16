package br.edu.ifsp.service;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.Sexo;

public class CalculoNutricaoService {

    public double calculartmb(Aluno aluno){
        double tbm = 0;
        if (aluno.getSexo().equals(Sexo.MASCULINO)){
            tbm = 88.36 + (13.4 * aluno.getPeso()) + (4.8 * aluno.getAltura()) - (5.7 * aluno.getIdade());
        }else {
            tbm = 447.6 + (9.2 * aluno.getPeso()) + (3.1 * aluno.getAltura()) - (4.3 * aluno.getIdade());
        }

        return tbm;
    }

    public double calcularGastoEnergeticoDiario(Aluno aluno){
        return calculartmb(aluno) * aluno.getFatorAtividade().getValor();
    }

    public double calculaMetaCalorica(Aluno aluno){
        double metaCalorica = calcularGastoEnergeticoDiario(aluno);

        return switch (aluno.getObjetivo()){
            case MANTER -> metaCalorica;
            case EMAGRECER -> metaCalorica *  0.8;
            case HIPERTROFIA -> metaCalorica * 1.15;
        };
    }
}
