package it.unimib.gestionequestionari.service;

import it.unimib.gestionequestionari.model.Question;
import it.unimib.gestionequestionari.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repo;

    public QuestionService(QuestionRepository repo) {
        this.repo = repo;
    }

    public List<Question> findAll() {
        return repo.findAll();
    }

    public List<Question> findAllById(List<Long> ids) {
        return repo.findAllById(ids);
    }

    public Question save(Question q) {
        return repo.save(q);
    }
}
