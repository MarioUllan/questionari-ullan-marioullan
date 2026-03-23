package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.Questionnaire;
import it.unimib.gestionequestionari.model.QuestionnaireStatus;
import it.unimib.gestionequestionari.repository.AnswerRepository;
import it.unimib.gestionequestionari.repository.QuestionnaireRepository;
import it.unimib.gestionequestionari.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireServiceTest {

    @Mock
    private QuestionnaireRepository questionnaireRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionnaireService questionnaireService;

    @Test
    void finalizeQuestionnaire_shouldAssignAccessCodeAndStatus() {
        Questionnaire questionnaire = new Questionnaire("Survey");
        questionnaire.setQuestions(List.of(new Question()));

        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(questionnaire));
        when(questionnaireRepository.save(any(Questionnaire.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Questionnaire result = questionnaireService.finalizeQuestionnaire(1L);

        assertEquals(QuestionnaireStatus.FINAL, result.getStatus());
        assertNotNull(result.getAccessCode());
        assertNotNull(result.getFinalizedAt());
    }

    @Test
    void finalizeQuestionnaire_withoutQuestions_shouldFail() {
        Questionnaire questionnaire = new Questionnaire("Survey");
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(questionnaire));

        assertThrows(IllegalStateException.class, () -> questionnaireService.finalizeQuestionnaire(1L));
    }

    @Test
    void updateQuestions_forFinalQuestionnaire_shouldFail() {
        Questionnaire questionnaire = new Questionnaire("Survey");
        questionnaire.setStatus(QuestionnaireStatus.FINAL);
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(questionnaire));

        assertThrows(IllegalStateException.class, () -> questionnaireService.updateQuestions(1L, List.of(new Question())));
    }

    @Test
    void updateQuestions_shouldRemoveDuplicatesAndSave() {
        Questionnaire questionnaire = new Questionnaire("Survey");
        Question question = new Question();
        when(questionnaireRepository.findById(1L)).thenReturn(Optional.of(questionnaire));
        when(questionnaireRepository.save(any(Questionnaire.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Questionnaire result = questionnaireService.updateQuestions(1L, List.of(question, question));

        assertEquals(1, result.getQuestions().size());
        verify(questionnaireRepository).save(questionnaire);
    }
}
