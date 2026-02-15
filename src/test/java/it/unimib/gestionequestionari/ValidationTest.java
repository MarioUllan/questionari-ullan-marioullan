package it.unimib.gestionequestionari;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.QuestionType;
import it.unimib.gestionequestionari.model.Questionnaire;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void questionnaire_withoutTitle_shouldFail() {
        Questionnaire q = new Questionnaire();
        q.setTitle("");

        assertFalse(validator.validate(q).isEmpty());
    }

    @Test
    void questionnaire_withTitle_shouldPass() {
        Questionnaire q = new Questionnaire();
        q.setTitle("Encuesta válida");

        assertTrue(validator.validate(q).isEmpty());
    }

    @Test
    void question_invalid_shouldFail() {
        Question q = new Question();
        q.setText("");
        q.setCategory("");
        q.setType(null);

        assertFalse(validator.validate(q).isEmpty());
    }

    @Test
    void question_valid_shouldPass() {
        Question q = new Question();
        q.setText("¿Te gusta Java?");
        q.setCategory("Tecnología");
        q.setType(QuestionType.OPEN);

        assertTrue(validator.validate(q).isEmpty());
    }
}
