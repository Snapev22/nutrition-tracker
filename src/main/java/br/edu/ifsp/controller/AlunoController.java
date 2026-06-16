package br.edu.ifsp.controller;

import br.edu.ifsp.exceptions.EntidadeNaoEncontradaException;
import br.edu.ifsp.exceptions.RegraDeNegocioException;
import br.edu.ifsp.model.Aluno;
import br.edu.ifsp.model.enums.FatorAtividade;
import br.edu.ifsp.model.enums.Objetivo;
import br.edu.ifsp.model.enums.Sexo;
import br.edu.ifsp.service.AlunoService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class AlunoController {

    @FXML private TextField tfNome;
    @FXML private TextField tfIdade;
    @FXML private TextField tfPeso;
    @FXML private TextField tfAltura;

    @FXML private ComboBox<Sexo> cbSexo;
    @FXML private ComboBox<FatorAtividade> cbFatorAtividade;
    @FXML private ComboBox<Objetivo> cbObjetivo;

    @FXML private Label lblMetaCalorica;

    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnExcluir;

    @FXML private TableView<Aluno> tableAlunos;
    @FXML private TableColumn<Aluno, String> colNome;
    @FXML private TableColumn<Aluno, Integer> colIdade;
    @FXML private TableColumn<Aluno, Double> colPeso;
    @FXML private TableColumn<Aluno, Double> colAltura;
    @FXML private TableColumn<Aluno, Sexo> colSexo;
    @FXML private TableColumn<Aluno, Objetivo> colObjetivo;
    @FXML private TableColumn<Aluno, Double> colMetaCalorica;

    private final AlunoService alunoService = new AlunoService();

    private Aluno alunoSelecionado = null;

    @FXML
    public void initialize() {
        cbSexo.setItems(FXCollections.observableArrayList(Sexo.values()));
        cbFatorAtividade.setItems(FXCollections.observableArrayList(FatorAtividade.values()));
        cbObjetivo.setItems(FXCollections.observableArrayList(Objetivo.values()));

        configurarConversores();
        configurarColunas();
        carregarTabela();

        tableAlunos.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                preencherFormulario(newItem);
            }
        });
    }

    private void configurarConversores() {
        cbSexo.setConverter(new StringConverter<Sexo>() {
            @Override
            public String toString(Sexo sexo) {
                if (sexo == null) {
                    return "";
                }
                return switch (sexo) {
                    case MASCULINO -> "Masculino";
                    case FEMININO -> "Feminino";
                };
            }

            @Override
            public Sexo fromString(String string) {
                return null;
            }
        });

        cbFatorAtividade.setConverter(new StringConverter<FatorAtividade>() {
            @Override
            public String toString(FatorAtividade fator) {
                if (fator == null) {
                    return "";
                }
                return switch (fator) {
                    case SEDENTARIO -> "Sedentário";
                    case LEVEMENTE_ATIVO -> "Levemente Ativo";
                    case MODERADAMENTE_ATIVO -> "Moderadamente Ativo";
                    case MUITO_ATIVO -> "Muito Ativo";
                    case EXTREMAMENTE_ATIVO -> "Extremamente Ativo";
                };
            }

            @Override
            public FatorAtividade fromString(String string) {
                return null;
            }
        });

        cbObjetivo.setConverter(new StringConverter<Objetivo>() {
            @Override
            public String toString(Objetivo objetivo) {
                if (objetivo == null) {
                    return "";
                }
                return switch (objetivo) {
                    case EMAGRECER -> "Emagrecer";
                    case MANTER -> "Manter Peso";
                    case HIPERTROFIA -> "Ganhar Massa";
                };
            }

            @Override
            public Objetivo fromString(String string) {
                return null;
            }
        });
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
        colMetaCalorica.setCellValueFactory(new PropertyValueFactory<>("metaCalorias"));
    }

    private void carregarTabela() {
        tableAlunos.setItems(FXCollections.observableArrayList(alunoService.listar()));

    }

    private void preencherFormulario(Aluno aluno) {
        tfNome.setText(aluno.getNome());
        tfIdade.setText(String.valueOf(aluno.getIdade()));
        tfPeso.setText(String.valueOf(aluno.getPeso()));
        tfAltura.setText(String.valueOf(aluno.getAltura()));

        cbSexo.setValue(aluno.getSexo());
        cbFatorAtividade.setValue(aluno.getFatorAtividade());
        cbObjetivo.setValue(aluno.getObjetivo());

        lblMetaCalorica.setText(String.format("Meta: %.2f kcal", aluno.getMetaCalorias()));

        alunoSelecionado = aluno;
    }

    @FXML
    private void onSalvar() {
        if (tfNome.getText().isBlank()
                || tfIdade.getText().isBlank()
                || tfPeso.getText().isBlank()
                || tfAltura.getText().isBlank()
                || cbSexo.getValue() == null
                || cbFatorAtividade.getValue() == null
                || cbObjetivo.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Preencha todos os campos");
            return;
        }

        int idade;
        double peso;
        double altura;
        try {
            idade = Integer.parseInt(tfIdade.getText());
            peso = Double.parseDouble(tfPeso.getText());
            altura = Double.parseDouble(tfAltura.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Valores numéricos inválidos");
            return;
        }

        if (alunoSelecionado == null) {
            Aluno novoAluno = new Aluno();
            novoAluno.setNome(tfNome.getText());
            novoAluno.setIdade(idade);
            novoAluno.setPeso(peso);
            novoAluno.setAltura(altura);
            novoAluno.setSexo(cbSexo.getValue());
            novoAluno.setFatorAtividade(cbFatorAtividade.getValue());
            novoAluno.setObjetivo(cbObjetivo.getValue());

            try {
                alunoService.cadastrar(novoAluno);
            } catch (RegraDeNegocioException e) {
                mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
                return;
            }

            lblMetaCalorica.setText(String.format("Meta: %.2f kcal", novoAluno.getMetaCalorias()));
        } else {
            alunoSelecionado.setNome(tfNome.getText());
            alunoSelecionado.setIdade(idade);
            alunoSelecionado.setPeso(peso);
            alunoSelecionado.setAltura(altura);
            alunoSelecionado.setSexo(cbSexo.getValue());
            alunoSelecionado.setFatorAtividade(cbFatorAtividade.getValue());
            alunoSelecionado.setObjetivo(cbObjetivo.getValue());

            try {
                alunoService.alter(alunoSelecionado);
            } catch (EntidadeNaoEncontradaException e) {
                mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
                return;
            }

            lblMetaCalorica.setText(String.format("Meta: %.2f kcal", alunoSelecionado.getMetaCalorias()));
        }

        carregarTabela();
        limparFormulario();
    }

    @FXML
    private void onLimpar() {
        limparFormulario();
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfIdade.setText("");
        tfPeso.setText("");
        tfAltura.setText("");

        cbSexo.setValue(null);
        cbFatorAtividade.setValue(null);
        cbObjetivo.setValue(null);

        lblMetaCalorica.setText("Meta: -- kcal");

        alunoSelecionado = null;
        tableAlunos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onExcluir() {
        if (alunoSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um aluno na tabela");
            return;
        }

        try {
            alunoService.deletar(alunoSelecionado.getId());
        } catch (EntidadeNaoEncontradaException e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
            return;
        }

        carregarTabela();
        limparFormulario();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo, mensagem);
        alert.showAndWait();
    }
}
