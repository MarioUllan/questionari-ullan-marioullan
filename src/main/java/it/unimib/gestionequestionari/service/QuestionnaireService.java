package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.model.Questionnaire;
import it.unimib.gestionequestionari.model.QuestionnaireStatus;
import it.unimib.gestionequestionari.repository.AnswerRepository;
import it.unimib.gestionequestionari.repository.QuestionnaireRepository;
import it.unimib.gestionequestionari.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository repo;
    private final SubmissionRepository submissionRepo;
    private final AnswerRepository answerRepo;

    public QuestionnaireService(QuestionnaireRepository repo,
                                SubmissionRepository submissionRepo,
                                AnswerRepository answerRepo) {
        this.repo = repo;
        this.submissionRepo = submissionRepo;
        this.answerRepo = answerRepo;
    }

    public List<Questionnaire> findAll() {
        return repo.findAll();
    }

    public Questionnaire findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Questionnaire not found"));
    }

    public Questionnaire findByAccessCode(String accessCode) {
        return repo.findByAccessCode(accessCode)
                .orElseThrow(() -> new IllegalArgumentException("Questionnaire not found for access code"));
    }

    public Questionnaire save(Questionnaire q) {
        return repo.save(q);
    }


    @Transactional
    public Questionnaire updateQuestions(Long questionnaireId, List<Question> selectedQuestions) {
        Questionnaire q = findById(questionnaireId);

        if (q.getStatus() == QuestionnaireStatus.FINAL) {
            throw new IllegalStateException("Cannot modify a FINAL questionnaire");
        }

        List<Question> questions = selectedQuestions == null ? List.of() : new ArrayList<>(new LinkedHashSet<>(selectedQuestions));
        q.setQuestions(questions);
        return repo.save(q);
    }
    /**
     * Finalizes a questionnaire: assigns an access code and switches status to FINAL.
     */
    @Transactional
    public Questionnaire finalizeQuestionnaire(Long questionnaireId) {
        Questionnaire q = findById(questionnaireId);

        if (q.getQuestions() == null || q.getQuestions().isEmpty()) {
            throw new IllegalStateException("Cannot finalize a questionnaire without questions");
        }

        if (q.getStatus() == QuestionnaireStatus.FINAL) {
            return q;
        }

        q.setStatus(QuestionnaireStatus.FINAL);
        q.setFinalizedAt(LocalDateTime.now());

        if (q.getAccessCode() == null || q.getAccessCode().isBlank()) {
            q.setAccessCode(UUID.randomUUID().toString());
        }

        return repo.save(q);
    }

    @Transactional
    public void deleteById(Long id) {
        Questionnaire q = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Questionnaire not found"));

        var submissions = submissionRepo.findAllByQuestionnaireId(id);
        for (var s : submissions) {
            answerRepo.deleteBySubmissionId(s.getId());
            submissionRepo.delete(s);
        }

        q.getQuestions().clear();
        repo.save(q);

        repo.delete(q);
    }
}
