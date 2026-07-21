package br.edu.ifsp.controller;

import br.edu.ifsp.model.Aluno;
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
import javafx.util.StringConverter;

import java.time.LocalDate;

public class DashboardController {

    @FXML private ComboBox<Aluno> cbAlunoDashboard;
    @FXML private DatePicker dpDataDashboard;
    @FXML private Button btnAtualizarDashboard;

    @FXML private Label lblMetaDashboard;
    @FXML private Label lblConsumidoDashboard;
    @FXML private Label lblRestanteDashboard;

    @FXML private ProgressBar progressBarConsumo;
    @FXML private Label lblPercentual;

    private final AlunoService alunoService = new AlunoService();
    private final PlanoDiarioService planoDiarioService = new PlanoDiarioService();

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

        PlanoDiario plano = planoDiarioService.buscarOuCriarPlanoDoDia(alunoSelecionado, data);

        double meta = alunoSelecionado.getMetaCalorias();
        double consumido = planoDiarioService.calcularTotalConsumido(plano);
        double restante = meta - consumido;

        lblMetaDashboard.setText(String.format("Meta diária: %.2f kcal", meta));
        lblConsumidoDashboard.setText(String.format("Consumido: %.2f kcal", consumido));
        lblRestanteDashboard.setText(String.format("Restante: %.2f kcal", restante));

        double percentual = (meta == 0) ? 0 : consumido / meta;

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
