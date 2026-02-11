package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.Questionnaire;
import it.unimib.gestionequestionari.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionnaireService {

    private final QuestionnaireRepository repo;

    public QuestionnaireService(QuestionnaireRepository repo) {
        this.repo = repo;
    }

    public List<Questionnaire> findAll() {
        return repo.findAll();
    }

    public Questionnaire findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Questionnaire not found"));
    }

    public Questionnaire save(Questionnaire q) {
        return repo.save(q);
    }
}
