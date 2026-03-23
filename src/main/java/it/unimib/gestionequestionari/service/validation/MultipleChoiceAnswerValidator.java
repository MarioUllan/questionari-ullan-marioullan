package it.unimib.gestionequestionari.service.validation;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class MultipleChoiceAnswerValidator implements AnswerValidator {

    @Override
    public QuestionType supportedType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public void validate(Question question, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Choice answer cannot be empty");
        }
    }
}
