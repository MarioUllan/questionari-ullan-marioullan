package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.*;
import it.unimib.gestionequestionari.repository.SubmissionRepository;
import it.unimib.gestionequestionari.service.validation.AnswerValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Comparator;

@Service
public class SubmissionService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private final SubmissionRepository submissionRepo;
    private final QuestionnaireService questionnaireService;
    private final AnswerValidationService answerValidationService;

    public SubmissionService(SubmissionRepository submissionRepo,
                             QuestionnaireService questionnaireService,
                             AnswerValidationService answerValidationService) {
        this.submissionRepo = submissionRepo;
        this.questionnaireService = questionnaireService;
        this.answerValidationService = answerValidationService;
    }


    @Transactional
    public Submission startOrResume(String questionnaireAccessCode, String email) {
        if (questionnaireAccessCode == null || questionnaireAccessCode.isBlank()) {
            throw new IllegalArgumentException("Access code is required");
        }
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email");
        }

        Questionnaire questionnaire = questionnaireService.findByAccessCode(questionnaireAccessCode.trim());
        if (questionnaire.getStatus() != QuestionnaireStatus.FINAL) {
            throw new IllegalStateException("Questionnaire is not FINAL");
        }

        return submissionRepo
                .findByQuestionnaireIdAndRespondentEmailAndStatus(questionnaire.getId(), email.trim(), SubmissionStatus.IN_PROGRESS)
                .orElseGet(() -> {
                    Submission s = new Submission(email.trim(), questionnaire);
                    questionnaire.getQuestions().stream()
                            .sorted(Comparator.comparing(Question::getId))
                            .forEach(q -> s.getAnswers().add(new Answer(s, q)));
                    return submissionRepo.save(s);
                });
    }


    public java.util.List<Submission> listByQuestionnaireId(Long questionnaireId) {
        return submissionRepo.findAllByQuestionnaireId(questionnaireId);
    }

    public Submission findById(Long id) {
        return submissionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
    }

    @Transactional
    public void saveDraft(Long submissionId, Map<Long, String> values) {
        Submission submission = findById(submissionId);

        if (submission.getStatus() == SubmissionStatus.SUBMITTED) {
            throw new IllegalStateException("Submission already submitted");
        }

        applyValues(submission, values, false);
        submissionRepo.save(submission);
    }

    @Transactional
    public void submit(Long submissionId, Map<Long, String> values) {
        Submission submission = findById(submissionId);

        if (submission.getStatus() == SubmissionStatus.SUBMITTED) {
            return;
        }

        applyValues(submission, values, true);

        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());
        submissionRepo.save(submission);
    }

    private void applyValues(Submission submission, Map<Long, String> values, boolean enforceRequired) {
        Map<Long, String> safeValues = (values == null) ? new HashMap<>() : values;

        for (Answer a : submission.getAnswers()) {
            Long qId = a.getQuestion().getId();
            String v = safeValues.get(qId);

            if (enforceRequired && a.getQuestion().isRequired()) {
                if (v == null || v.isBlank()) {
                    throw new IllegalArgumentException("Missing required answer for question id=" + qId);
                }
            }

            if (v != null && !v.isBlank()) {
                answerValidationService.validate(a.getQuestion(), v);
            }

            if (a.getQuestion().getType() == QuestionType.OPEN) {
                a.setTextAnswer(v);
                a.setChoiceAnswer(null);
            } else {
                a.setChoiceAnswer(v);
                a.setTextAnswer(null);
            }
        }
    }


}
