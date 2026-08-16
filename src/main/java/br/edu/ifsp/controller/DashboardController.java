package br.edu.ifsp.controller;

import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.InformacaoNutricional;
import br.edu.ifsp.model.PlanoDiario;
import br.edu.ifsp.service.AlunoService;
import br.edu.ifsp.service.PlanoDiarioService;

import br.edu.ifsp.util.ConverterUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DashboardController {

    @FXML private ComboBox<Aluno> cbAlunoDashboard;
    @FXML private DatePicker dpDataDashboard;
    @FXML private Button btnAtualizarDashboard;

    @FXML private Label lblMetaDashboard;
    @FXML private Label lblConsumidoDashboard;
    @FXML private Label lblRestanteDashboard;
    @FXML private Label lblProteinaDashboard;
    @FXML private Label lblCarboidratoDashboard;
    @FXML private Label lblGorduraDashboard;

    @FXML private ProgressBar progressBarConsumo;
    @FXML private Label lblPercentual;

    private final AlunoService alunoService;
    private final PlanoDiarioService planoDiarioService;

    @FXML
    public void initialize() {
        cbAlunoDashboard.setItems(FXCollections.observableArrayList(alunoService.listar()));
        cbAlunoDashboard.setConverter(ConverterUtils.converterPorFuncao(Aluno::getNome));

        dpDataDashboard.setValue(LocalDate.now());
    }

    @FXML
    private void onAtualizarDashboard() {
        Aluno alunoSelecionado = cbAlunoDashboard.getValue();
        LocalDate data = dpDataDashboard.getValue();

        if (alunoSelecionado == null || data == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione aluno e data");
            return;
        }

         Optional<PlanoDiario> planoOpt = planoDiarioService.buscarPlanoParaConsulta(alunoSelecionado, data);

        double meta = alunoSelecionado.getMetaCaloricaDefinida();
        InformacaoNutricional resumo = planoOpt.isPresent()
                ? planoDiarioService.calcularResumoNutricional(planoOpt.get())
                : InformacaoNutricional.zero();
        double restante = meta - resumo.getCalorias();


        lblMetaDashboard.setText(String.format("Meta Definida: %.2f kcal", meta));
        lblConsumidoDashboard.setText(String.format("Adcionado: %.2f kcal", resumo.getCalorias()));
        lblRestanteDashboard.setText(String.format("Restante: %.2f kcal", restante));
        lblProteinaDashboard.setText(String.format("Proteína: %.1f g", resumo.getProteina()));
        lblCarboidratoDashboard.setText(String.format("Carboidrato: %.1f g", resumo.getCarboidrato()));
        lblGorduraDashboard.setText(String.format("Gordura: %.1f g", resumo.getGordura()));

        double percentual = (meta == 0) ? 0 : resumo.getCalorias() / meta;

        progressBarConsumo.setProgress(Math.min(percentual, 1.0));
        lblPercentual.setText(String.format("%.0f%% da meta utilizada", percentual * 100));

        progressBarConsumo.getStyleClass().removeAll("progress-ok", "progress-alerta", "progress-excedido");
        if (percentual <= 0.8) {
            progressBarConsumo.getStyleClass().add("progress-ok");
        } else if (percentual <= 1.0) {
            progressBarConsumo.getStyleClass().add("progress-alerta");
        } else {
            progressBarConsumo.getStyleClass().add("progress-excedido");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo, mensagem);
        alert.showAndWait();
    }
}
