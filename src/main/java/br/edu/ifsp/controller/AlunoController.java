    package br.edu.ifsp.controller;

    import br.edu.ifsp.model.Aluno;
    import br.edu.ifsp.model.enums.FatorAtividade;
    import br.edu.ifsp.model.enums.Objetivo;
    import br.edu.ifsp.model.enums.Sexo;
    import br.edu.ifsp.service.AlunoService;

    import br.edu.ifsp.util.ConverterUtils;
    import br.edu.ifsp.util.DialogoUtils;
    import javafx.collections.FXCollections;
    import javafx.concurrent.Task;
    import javafx.fxml.FXML;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.Label;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.TextField;
    import javafx.scene.control.cell.PropertyValueFactory;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class AlunoController {

        @FXML private TextField tfNome;
        @FXML private TextField tfIdade;
        @FXML private TextField tfPeso;
        @FXML private TextField tfAltura;
        @FXML private TextField tfMetaCaloricaDefinida;

        @FXML private ComboBox<Sexo> cbSexo;
        @FXML private ComboBox<FatorAtividade> cbFatorAtividade;
        @FXML private ComboBox<Objetivo> cbObjetivo;

        @FXML private Label lblMetaCaloricaEstimada;

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
        @FXML private TableColumn<Aluno, Double> colMetaCaloricaEstimada;
        @FXML private TableColumn<Aluno, Double> colMetaDefinida;

        private final AlunoService alunoService;

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
           cbSexo.setConverter(ConverterUtils.converterPorFuncao(this::descricaoSexo));
           cbFatorAtividade.setConverter(ConverterUtils.converterPorFuncao(this::descricaoFatorAtividade));
           cbObjetivo.setConverter(ConverterUtils.converterPorFuncao(this::descricaoObjetivo));
        }

        private String descricaoSexo(Sexo sexo){
            return switch (sexo){
                case MASCULINO -> "Masculino";
                case FEMININO -> "Feminino";
            };
        }

        private String descricaoFatorAtividade(FatorAtividade fatorAtividade){
            return switch (fatorAtividade){
                case SEDENTARIO -> "Sedentario";
                case LEVEMENTE_ATIVO -> "Levemente Ativo";
                case MODERADAMENTE_ATIVO -> "Moderadamente Ativo";
                case MUITO_ATIVO -> "Muito Ativo";
                case EXTREMAMENTE_ATIVO -> "Extremamente Ativo";
            };
        }

        private String descricaoObjetivo(Objetivo objetivo){
            return switch (objetivo){
                case MANTER -> "Manter";
                case HIPERTROFIA -> "Hipertrofia";
                case EMAGRECER -> "Emagrecer";
            };
        }
        private void configurarColunas() {
            colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
            colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
            colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
            colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
            colObjetivo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
            colMetaCaloricaEstimada.setCellValueFactory(new PropertyValueFactory<>("metaCaloricaEstimada"));
            colMetaDefinida.setCellValueFactory(new PropertyValueFactory<>("metaCaloricaDefinida"));
        }

        private void carregarTabela() {
            Task<List<Aluno>> task = new Task<>() {
                @Override
                protected List<Aluno> call() throws Exception {
                    return alunoService.listar();
                }
            };

            task.setOnSucceeded(event -> {
                tableAlunos.setItems(FXCollections.observableArrayList(task.getValue()));
            });

            task.setOnFailed(event -> {
                Throwable erro = task.getException();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar alunos: " + erro.getMessage());
            });

            new Thread(task).start();
        }

        private void preencherFormulario(Aluno aluno) {
            tfNome.setText(aluno.getNome());
            tfIdade.setText(String.valueOf(aluno.getIdade()));
            tfPeso.setText(String.valueOf(aluno.getPeso()));
            tfAltura.setText(String.valueOf(aluno.getAltura()));

            cbSexo.setValue(aluno.getSexo());
            cbFatorAtividade.setValue(aluno.getFatorAtividade());
            cbObjetivo.setValue(aluno.getObjetivo());

            lblMetaCaloricaEstimada.setText(String.format("Meta Estimada (TMB): %.2f kcal", aluno.getMetaCaloricaEstimada()));
            tfMetaCaloricaDefinida.setText(String.format("%.2f", aluno.getMetaCaloricaDefinida()));

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

            boolean isNovoCadastro = (alunoSelecionado == null);
            Aluno alunoParaSalvar = isNovoCadastro ? new Aluno() : alunoSelecionado;

            alunoParaSalvar.setNome(tfNome.getText());
            alunoParaSalvar.setIdade(idade);
            alunoParaSalvar.setPeso(peso);
            alunoParaSalvar.setAltura(altura);
            alunoParaSalvar.setSexo(cbSexo.getValue());
            alunoParaSalvar.setFatorAtividade(cbFatorAtividade.getValue());
            alunoParaSalvar.setObjetivo(cbObjetivo.getValue());

            if (!tfMetaCaloricaDefinida.getText().isBlank()) {
                try {
                    alunoParaSalvar.setMetaCaloricaDefinida(Double.parseDouble(tfMetaCaloricaDefinida.getText()));
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Meta calórica definida inválida");
                    return;
                }
            }

           Task<Void> task = new Task<>() {
               @Override
               protected Void call() {
                   if(isNovoCadastro){
                       alunoService.cadastrar(alunoParaSalvar);
                   }else{
                       alunoService.alter(alunoParaSalvar);
                   }
                   return null;
               }
           };

           task.setOnSucceeded(event -> {
               lblMetaCaloricaEstimada.setText(String.format("Estimada (TMB): %.2f kcal", alunoParaSalvar.getMetaCaloricaEstimada()));
               tfMetaCaloricaDefinida.setText(String.format("%.2f", alunoParaSalvar.getMetaCaloricaDefinida()));
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
            tfIdade.setText("");
            tfPeso.setText("");
            tfAltura.setText("");

            cbSexo.setValue(null);
            cbFatorAtividade.setValue(null);
            cbObjetivo.setValue(null);

            lblMetaCaloricaEstimada.setText("Meta Estimada (TMB): -- kcal");
            tfMetaCaloricaDefinida.clear();

            alunoSelecionado = null;
            tableAlunos.getSelectionModel().clearSelection();
        }

        @FXML
        private void onExcluir() {
            if (alunoSelecionado == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Selecione um aluno na tabela");
                return;
            }

            if(!DialogoUtils.confirmarExclusao(alunoSelecionado.getNome())){
                return;
            }

            Long idParaExcluir = alunoSelecionado.getId();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    alunoService.deletar(idParaExcluir);
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
