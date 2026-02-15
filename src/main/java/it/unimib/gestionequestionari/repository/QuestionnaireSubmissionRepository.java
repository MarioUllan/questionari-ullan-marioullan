package it.unimib.gestionequestionari.repository;

import it.unimib.gestionequestionari.model.QuestionnaireSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireSubmissionRepository extends JpaRepository<QuestionnaireSubmission, Long> {
    Optional<QuestionnaireSubmission> findByAccessCode(String accessCode);

    List<QuestionnaireSubmission> findAllByQuestionnaireId(Long questionnaireId);
}