package it.unimib.gestionequestionari.repository;

import it.unimib.gestionequestionari.model.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    java.util.Optional<Questionnaire> findByAccessCode(String accessCode);

}
