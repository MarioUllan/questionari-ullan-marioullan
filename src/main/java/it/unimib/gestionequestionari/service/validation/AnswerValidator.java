package it.unimib.gestionequestionari.service.validation;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;

public interface AnswerValidator {
    QuestionType supportedType();
    void validate(Question question, String value);
}
