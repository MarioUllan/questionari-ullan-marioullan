package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.*;
import it.unimib.gestionequestionari.repository.SubmissionRepository;
import it.unimib.gestionequestionari.service.validation.AnswerValidationService;
import it.unimib.gestionequestionari.service.validation.MultipleChoiceAnswerValidator;
import it.unimib.gestionequestionari.service.validation.OpenAnswerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private QuestionnaireService questionnaireService;

    @Spy
    private AnswerValidationService answerValidationService =
            new AnswerValidationService(List.of(new OpenAnswerValidator(), new MultipleChoiceAnswerValidator()));

    @InjectMocks
    private SubmissionService submissionService;

    private Questionnaire questionnaire;
    private Question requiredOpenQuestion;

    @BeforeEach
    void setUp() {
        questionnaire = new Questionnaire("Survey");
        questionnaire.setStatus(QuestionnaireStatus.FINAL);
        questionnaire.setAccessCode("CODE-123");
        requiredOpenQuestion = new Question();
        requiredOpenQuestion.setText("Name");
        requiredOpenQuestion.setType(QuestionType.OPEN);
        requiredOpenQuestion.setCategory("General");
        requiredOpenQuestion.setRequired(true);

        try {
            var field = Questionnaire.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(questionnaire, 10L);

            field = Question.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(requiredOpenQuestion, 100L);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        questionnaire.setQuestions(List.of(requiredOpenQuestion));
    }

    @Test
    void startOrResume_shouldCreateSubmissionForFinalQuestionnaire() {
        when(questionnaireService.findByAccessCode("CODE-123")).thenReturn(questionnaire);
        when(submissionRepository.findByQuestionnaireIdAndRespondentEmailAndStatus(10L, "mario@test.com", SubmissionStatus.IN_PROGRESS))
                .thenReturn(Optional.empty());
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Submission submission = submissionService.startOrResume("CODE-123", "mario@test.com");

        assertEquals("mario@test.com", submission.getRespondentEmail());
        assertEquals(1, submission.getAnswers().size());
    }

    @Test
    void startOrResume_withInvalidEmail_shouldFail() {
        assertThrows(IllegalArgumentException.class, () -> submissionService.startOrResume("CODE-123", "bad-email"));
    }

    @Test
    void submit_withoutRequiredAnswer_shouldFail() {
        Submission submission = buildSubmission();
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        assertThrows(IllegalArgumentException.class, () -> submissionService.submit(1L, Map.of()));
    }

    @Test
    void submit_withValidAnswer_shouldSetSubmittedStatus() {
        Submission submission = buildSubmission();
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        submissionService.submit(1L, Map.of(100L, "Mario"));

        assertEquals(SubmissionStatus.SUBMITTED, submission.getStatus());
        assertNotNull(submission.getSubmittedAt());
        assertEquals("Mario", submission.getAnswers().get(0).getTextAnswer());
    }

    @Test
    void saveDraft_forSubmittedSubmission_shouldFail() {
        Submission submission = buildSubmission();
        submission.setStatus(SubmissionStatus.SUBMITTED);
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        assertThrows(IllegalStateException.class, () -> submissionService.saveDraft(1L, Map.of(100L, "Mario")));
    }

    private Submission buildSubmission() {
        Submission submission = new Submission("mario@test.com", questionnaire);
        submission.getAnswers().add(new Answer(submission, requiredOpenQuestion));
        return submission;
    }
}
