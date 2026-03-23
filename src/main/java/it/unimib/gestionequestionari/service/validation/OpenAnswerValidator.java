package it.unimib.gestionequestionari.service.validation;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class OpenAnswerValidator implements AnswerValidator {

    private static final int MAX_LENGTH = 2000;

    @Override
    public QuestionType supportedType() {
        return QuestionType.OPEN;
    }

    @Override
    public void validate(Question question, String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Text answer too long");
        }
    }
}
