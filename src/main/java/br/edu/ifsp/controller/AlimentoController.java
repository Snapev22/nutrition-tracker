package br.edu.ifsp.controller;

import br.edu.ifsp.model.Alimento;
import br.edu.ifsp.model.enums.UnidadeMedida;
import br.edu.ifsp.service.AlimentoService;
import br.edu.ifsp.util.ConverterUtils;
import br.edu.ifsp.util.DialogoUtils;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AlimentoController {

    @FXML private TextField tfNome;
    @FXML private TextField tfProteina;
    @FXML private TextField tfCarboidrato;
    @FXML private TextField tfGordura;
    @FXML private TextField tfCalorias;
    @FXML private ComboBox<UnidadeMedida> cbUnidadeMedida;

    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Alimento> tableAlimentos;
    @FXML private TableColumn<Alimento, String> colNome;
    @FXML private TableColumn<Alimento, Double> colProteina;
    @FXML private TableColumn<Alimento, Double> colCarboidrato;
    @FXML private TableColumn<Alimento, Double> colGordura;
    @FXML private TableColumn<Alimento, Double> colCalorias;
    @FXML private TableColumn<Alimento, UnidadeMedida> colUnidadeMedida;

    private final AlimentoService alimentoService = new AlimentoService();
    private Alimento alimentoSelecionado = null;

    @FXML
    public void initialize() {
        cbUnidadeMedida.setItems(FXCollections.observableArrayList(UnidadeMedida.values()));
        cbUnidadeMedida.setConverter(ConverterUtils.converterPorFuncao(this::descricaoUnidadeMedida));

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colProteina.setCellValueFactory(new PropertyValueFactory<>("proteina"));
        colCarboidrato.setCellValueFactory(new PropertyValueFactory<>("carboidrato"));
        colGordura.setCellValueFactory(new PropertyValueFactory<>("gordura"));
        colCalorias.setCellValueFactory(new PropertyValueFactory<>("calorias"));
        colUnidadeMedida.setCellValueFactory(new PropertyValueFactory<>("unidadeMedida"));

        carregarTabela();

        tableAlimentos.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                preencherFormulario(newItem);
            }
        });
    }
    private String descricaoUnidadeMedida(UnidadeMedida unidade) {
        return switch (unidade) {
            case GRAMAS -> "Gramas (por 100g)";
            case MILILITROS -> "Mililitros (por 100ml)";
            case UNIDADE -> "Unidade";
        };
    }

    private void carregarTabela() {
        Task<List<Alimento>> task = new Task<>() {
            @Override
            protected List<Alimento> call() {
                return alimentoService.listar();
            }
        };

        task.setOnSucceeded(event -> tableAlimentos.setItems(FXCollections.observableArrayList(task.getValue())));

        task.setOnFailed(event -> {
            Throwable erro = task.getException();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar alimentos: " + erro.getMessage());
        });

        new Thread(task).start();
    }

    private void preencherFormulario(Alimento alimento) {
        tfNome.setText(alimento.getNome());
        tfProteina.setText(String.valueOf(alimento.getProteina()));
        tfCarboidrato.setText(String.valueOf(alimento.getCarboidrato()));
        tfGordura.setText(String.valueOf(alimento.getGordura()));
        tfCalorias.setText(String.valueOf(alimento.getCalorias()));
        cbUnidadeMedida.setValue(alimento.getUnidadeMedida());

        alimentoSelecionado = alimento;
    }

    @FXML
    private void onSalvar() {
        if (tfNome.getText().isBlank()
                || tfProteina.getText().isBlank()
                || tfCarboidrato.getText().isBlank()
                || tfGordura.getText().isBlank()
                || tfCalorias.getText().isBlank()
                || cbUnidadeMedida.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Preencha todos os campos");
            return;
        }

        double proteina;
        double carboidrato;
        double gordura;
        double calorias;
        try {
            proteina = Double.parseDouble(tfProteina.getText());
            carboidrato = Double.parseDouble(tfCarboidrato.getText());
            gordura = Double.parseDouble(tfGordura.getText());
            calorias = Double.parseDouble(tfCalorias.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Valores numéricos inválidos");
            return;
        }

        boolean isNovoCadastro = (alimentoSelecionado == null);
        Alimento alimentoParaSalvar = isNovoCadastro ? new Alimento() : alimentoSelecionado;

        alimentoParaSalvar.setNome(tfNome.getText());
        alimentoParaSalvar.setProteina(proteina);
        alimentoParaSalvar.setCarboidrato(carboidrato);
        alimentoParaSalvar.setGordura(gordura);
        alimentoParaSalvar.setCalorias(calorias);
        alimentoParaSalvar.setUnidadeMedida(cbUnidadeMedida.getValue());

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                if(isNovoCadastro){
                    alimentoService.cadastrar(alimentoParaSalvar);
                }else{
                    alimentoService.alterar(alimentoParaSalvar);
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            carregarTabela();
            limparFormulario();
        });

        task.setOnFailed(event -> {
            Throwable erro = task.getException();
            mostrarAlerta(Alert.AlertType.ERROR, erro.getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private void onLimpar() {
        limparFormulario();
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfProteina.setText("");
        tfCarboidrato.setText("");
        tfGordura.setText("");
        tfCalorias.setText("");
        cbUnidadeMedida.setValue(null);

        alimentoSelecionado = null;
        tableAlimentos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onExcluir() {
        if (alimentoSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um alimento na tabela");
            return;
        }

        if(!DialogoUtils.confirmarExclusao(alimentoSelecionado.getNome())){
            return;
        }

        Long idParaExcluir = alimentoSelecionado.getId();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                alimentoService.deletar(idParaExcluir);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            carregarTabela();
            limparFormulario();
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
