package br.edu.ifsp.util;

import javafx.util.StringConverter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ConverterUtils {
    private ConverterUtils() {
    }

    public static <T>StringConverter<T> converterPorFuncao(Function<T, String> exibicao){
        return new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object == null? "": exibicao.apply(object);
            }

            @Override
            public T fromString(String string) {
                throw new UnsupportedOperationException(
                        "Conversão de texto para objeto não é suportada neste ComboBox");
            }
        };
    }
}
