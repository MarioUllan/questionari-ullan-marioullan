package it.unimib.gestionequestionari.service.validation;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerValidationService {

    private final Map<QuestionType, AnswerValidator> validators = new EnumMap<>(QuestionType.class);

    public AnswerValidationService(List<AnswerValidator> validators) {
        for (AnswerValidator validator : validators) {
            this.validators.put(validator.supportedType(), validator);
        }
    }

    public void validate(Question question, String value) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }

        AnswerValidator validator = validators.get(question.getType());
        if (validator == null) {
            throw new IllegalStateException("No validator configured for question type: " + question.getType());
        }

        validator.validate(question, value);
    }
}
