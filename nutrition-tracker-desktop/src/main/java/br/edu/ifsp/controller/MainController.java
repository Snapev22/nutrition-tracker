package br.edu.ifsp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainController {

    private final ApplicationContext applicationContext;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML
    private StackPane paneCentral;

    @FXML
    private Button btnNavAlunos;

    @FXML
    private Button btnNavAlimentos;

    @FXML
    private Button btnNavPlano;

    @FXML
    private Button btnNavDashboard;

    @FXML
    public void initialize() {
        carregarTela("/br/edu/ifsp/view/aluno/aluno.fxml");
        marcarBotaoAtivo(btnNavAlunos);
    }

    @FXML
    private void onNavAlunos() {
        carregarTela("/br/edu/ifsp/view/aluno/aluno.fxml");
        marcarBotaoAtivo(btnNavAlunos);
    }

    @FXML
    private void onNavAlimentos() {
        carregarTela("/br/edu/ifsp/view/alimento/alimento.fxml");
        marcarBotaoAtivo(btnNavAlimentos);
    }

    @FXML
    private void onNavPlano() {
        carregarTela("/br/edu/ifsp/view/plano/plano.fxml");
        marcarBotaoAtivo(btnNavPlano);
    }

    @FXML
    private void onNavDashboard() {
        carregarTela("/br/edu/ifsp/view/dashboard/dashboard.fxml");
        marcarBotaoAtivo(btnNavDashboard);
    }

    private void carregarTela(String caminhoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
            loader.setControllerFactory(applicationContext::getBean);
            Node tela = loader.load();
            paneCentral.getChildren().setAll(tela);
        } catch (Exception e) {
            logger.error("Erro ao carregar tela", e);

            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Erro ao carregar a tela:\n\n"
                            + caminhoFxml
                            + "\n\n"
                            + e
            );

            alert.showAndWait();
        }
    }

    private void marcarBotaoAtivo(Button botaoAtivo) {
        List.of(btnNavAlunos, btnNavAlimentos, btnNavPlano, btnNavDashboard)
                .forEach(botao -> botao.getStyleClass().remove("active"));
        botaoAtivo.getStyleClass().add("active");
    }
}
