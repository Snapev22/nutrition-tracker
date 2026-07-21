package br.edu.ifsp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogoUtils {
    public static boolean confirmarExclusao(String nomeEntidade){
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja excluir \"" + nomeEntidade + "\"? Essa ação não pode ser desfeita."
        );
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}
