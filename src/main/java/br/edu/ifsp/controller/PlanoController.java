package br.edu.ifsp.controller;

import br.edu.ifsp.model.*;
import br.edu.ifsp.service.AlimentoService;
import br.edu.ifsp.service.AlunoService;
import br.edu.ifsp.service.PlanoDiarioService;

import br.edu.ifsp.util.ConverterUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class PlanoController {

    @FXML private ComboBox<Aluno> cbAluno;
    @FXML private DatePicker dpData;
    @FXML private Button btnCarregarPlano;

    @FXML private Label lblMeta;
    @FXML private Label lblConsumido;
    @FXML private Label lblRestante;
    @FXML private Label lblProteina;
    @FXML private Label lblCarboidrato;
    @FXML private Label lblGordura;

    @FXML private ComboBox<Alimento> cbAlimento;
    @FXML private TextField tfQuantidade;
    @FXML private Button btnAdicionarItem;

    @FXML private TableView<ItemPlano> tableItens;
    @FXML private TableColumn<ItemPlano, String> colAlimentoNome;
    @FXML private TableColumn<ItemPlano, Double> colQuantidade;
    @FXML private TableColumn<ItemPlano, Double> colCaloriasTotais;
    @FXML private TableColumn<ItemPlano, Double> colProteina;
    @FXML private TableColumn<ItemPlano, Double> colCarboidrato;
    @FXML private TableColumn<ItemPlano, Double> colGordura;

    @FXML private Button btnRemoverItem;

    private final AlunoService alunoService = new AlunoService();
    private final AlimentoService alimentoService = new AlimentoService();
    private final PlanoDiarioService planoDiarioService = new PlanoDiarioService();

    private PlanoDiario planoAtual = null;

    @FXML
    public void initialize() {
        cbAluno.setItems(FXCollections.observableArrayList(alunoService.listar()));
        cbAluno.setConverter(ConverterUtils.converterPorFuncao(Aluno::getNome));

        cbAlimento.setItems(FXCollections.observableArrayList(alimentoService.listar()));
        cbAlimento.setConverter(ConverterUtils.converterPorFuncao(Alimento::getNome));

        dpData.setValue(LocalDate.now());

        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colCaloriasTotais.setCellValueFactory(new PropertyValueFactory<>("caloriasTotais"));
        colAlimentoNome.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getAlimento().getNome()));
        colProteina.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalNutricional().getProteina()));
        colCarboidrato.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalNutricional().getCarboidrato()));
        colGordura.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalNutricional().getGordura()));
    }

    @FXML
    private void onCarregarPlano() {
        if (cbAluno.getValue() == null || dpData.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione aluno e data");
            return;
        }

        planoAtual = planoDiarioService.buscarOuCriarPlanoDoDia(cbAluno.getValue(), dpData.getValue());

        atualizarTabela();
        atualizarResumo();
    }

    private void atualizarTabela() {
        tableItens.setItems(FXCollections.observableArrayList(planoAtual.getItens()));
    }

    private void atualizarResumo() {
        double meta = planoAtual.getAluno().getMetaCalorias();
        InformacaoNutricional resumo = planoDiarioService.calcularResumoNutricional(planoAtual);
        double restante = meta - resumo.getCalorias();

        lblMeta.setText(String.format("Meta: %.2f kcal", meta));
        lblConsumido.setText(String.format("Planejado: %.2f kcal", resumo.getCalorias()));
        lblRestante.setText(String.format("Restante: %.2f kcal", restante));
        lblProteina.setText(String.format("Proteína: %.1f g", resumo.getProteina()));
        lblCarboidrato.setText(String.format("Carboidrato: %.1f g", resumo.getCarboidrato()));
        lblGordura.setText(String.format("Gordura: %.1f g", resumo.getGordura()));
    }

    @FXML
    private void onAdicionarItem() {
        if (planoAtual == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Carregue um plano primeiro");
            return;
        }

        if (cbAlimento.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um alimento");
            return;
        }

        double quantidade;
        try {
            quantidade = Double.parseDouble(tfQuantidade.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Quantidade inválida");
            return;
        }

        Alimento alimentoSelecionado = cbAlimento.getValue();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                planoDiarioService.adicionarItem(planoAtual, alimentoSelecionado, quantidade);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            atualizarTabela();
            atualizarResumo();
            tfQuantidade.clear();
        });

        task.setOnFailed(event -> {
            Throwable erro = task.getException();
            mostrarAlerta(Alert.AlertType.WARNING, erro.getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private void onRemoverItem() {
        ItemPlano selecionado = tableItens.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um item na tabela");
            return;
        }

        Long idParaRemover = selecionado.getId();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                planoDiarioService.removerItem(planoAtual, idParaRemover);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            atualizarTabela();
            atualizarResumo();
        });

        task.setOnFailed(event -> {
            Throwable erro = task.getException();
            mostrarAlerta(Alert.AlertType.ERROR, erro.getMessage());
        });

        new Thread(task).start();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo, mensagem);
        alert.showAndWait();
    }
}
