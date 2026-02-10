package it.unimib.gestionequestionari.repository;

import it.unimib.gestionequestionari.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
