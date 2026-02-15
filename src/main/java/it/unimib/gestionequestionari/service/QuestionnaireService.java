package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.Questionnaire;
import it.unimib.gestionequestionari.repository.AnswerRepository;
import it.unimib.gestionequestionari.repository.QuestionnaireRepository;
import it.unimib.gestionequestionari.repository.QuestionnaireSubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository repo;
    private final QuestionnaireSubmissionRepository submissionRepo;
    private final AnswerRepository answerRepo;

    public QuestionnaireService(QuestionnaireRepository repo,
                                QuestionnaireSubmissionRepository submissionRepo,
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

    public Questionnaire save(Questionnaire q) {
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