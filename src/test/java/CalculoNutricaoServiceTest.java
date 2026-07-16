import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;
import br.edu.ifsp.service.CalculoNutricaoService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculoNutricaoServiceTest {

    @Test
    void deveCalcularTmbMasculino(){

        Aluno aluno = new Aluno();

        aluno.setPeso(80);
        aluno.setAltura(180);
        aluno.setIdade(24);
        aluno.setSexo(Sexo.MASCULINO);

        CalculoNutricaoService service = new CalculoNutricaoService();

        double resultado = service.calculartmb(aluno);

        assertEquals(1887.56, resultado, 0.01);
    }


    @Test
    void deveCalcularTmbFeminino(){

        Aluno aluno = new Aluno();

        aluno.setPeso(60);
        aluno.setAltura(165);
        aluno.setIdade(30);
        aluno.setSexo(Sexo.FEMININO);

        CalculoNutricaoService service = new CalculoNutricaoService();

        double resultado = service.calculartmb(aluno);

        assertEquals(1382.1, resultado, 0.01);
    }

    @Test
    void testeGETD() {
        Aluno aluno = new Aluno();
        aluno.setPeso(80);
        aluno.setAltura(180);
        aluno.setIdade(24);
        aluno.setSexo(Sexo.MASCULINO);
        aluno.setFatorAtividade(FatorAtividade.MODERADAMENTE_ATIVO);

        CalculoNutricaoService service = new CalculoNutricaoService();
        double resultado = service.calcularGastoEnergeticoDiario(aluno);

        // Valor exato calculado: 1887.56 * 1.55 = 2925.718
        assertEquals(2925.718, resultado, 0.01);
    }

    @Test
    void testeMetaCaloricaEmagrecer() {
        Aluno aluno = new Aluno();
        aluno.setPeso(80);
        aluno.setAltura(180);
        aluno.setIdade(24);
        aluno.setSexo(Sexo.MASCULINO);
        aluno.setFatorAtividade(FatorAtividade.MODERADAMENTE_ATIVO);
        aluno.setObjetivo(Objetivo.EMAGRECER);

        CalculoNutricaoService service = new CalculoNutricaoService();
        double resultado = service.calculaMetaCalorica(aluno);

        assertEquals(2340.57, resultado, 0.01);
    }

    @Test
    void testeMetaCaloricaGanharMassa() {
        Aluno aluno = new Aluno();
        aluno.setPeso(80);
        aluno.setAltura(180);
        aluno.setIdade(24);
        aluno.setSexo(Sexo.MASCULINO);
        aluno.setFatorAtividade(FatorAtividade.MODERADAMENTE_ATIVO);
        aluno.setObjetivo(Objetivo.HIPERTROFIA);

        CalculoNutricaoService service = new CalculoNutricaoService();
        double resultado = service.calculaMetaCalorica(aluno);
    
        assertEquals(3364.575, resultado, 0.01);
    }
}
