package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.*;
import it.unimib.gestionequestionari.repository.AnswerRepository;
import it.unimib.gestionequestionari.repository.QuestionnaireSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubmissionService {

    private final QuestionnaireSubmissionRepository submissionRepo;
    private final AnswerRepository answerRepo;
    private final QuestionnaireService questionnaireService;

    public SubmissionService(QuestionnaireSubmissionRepository submissionRepo,
                             AnswerRepository answerRepo,
                             QuestionnaireService questionnaireService) {
        this.submissionRepo = submissionRepo;
        this.answerRepo = answerRepo;
        this.questionnaireService = questionnaireService;
    }

    public QuestionnaireSubmission createDraft(Long questionnaireId, String email) {
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);

        String code = UUID.randomUUID().toString();
        QuestionnaireSubmission submission = new QuestionnaireSubmission(code, email, questionnaire);
        submission.setStatus(SubmissionStatus.DRAFT);

        for (Question q : questionnaire.getQuestions()) {
            Answer a = new Answer(submission, q);
            submission.getAnswers().add(a);
        }

        return submissionRepo.save(submission);
    }

    public QuestionnaireSubmission findByCode(String code) {
        return submissionRepo.findByAccessCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
    }

    public void saveDraft(String code, Map<Long, String> values) {
        QuestionnaireSubmission submission = findByCode(code);
        if (submission.getStatus() == SubmissionStatus.FINAL) {
            throw new IllegalStateException("Submission already final");
        }

        for (Answer a : submission.getAnswers()) {
            Long qId = a.getQuestion().getId();
            String v = values.get(qId);
            if (a.getQuestion().getType() == QuestionType.OPEN) {
                a.setTextAnswer(v);
            } else {
                a.setChoiceAnswer(v);
            }
        }

        submissionRepo.save(submission);
    }

    public void submitFinal(String code, Map<Long, String> values) {
        saveDraft(code, values);
        QuestionnaireSubmission submission = findByCode(code);
        submission.setStatus(SubmissionStatus.FINAL);
        submissionRepo.save(submission);
    }

    public void deleteByCode(String code) {
        QuestionnaireSubmission submission = findByCode(code);
        submissionRepo.delete(submission);
    }
}