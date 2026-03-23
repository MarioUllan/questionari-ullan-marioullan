package it.unimib.gestionequestionari.repository;

import it.unimib.gestionequestionari.model.Submission;
import it.unimib.gestionequestionari.model.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findAllByQuestionnaireId(Long questionnaireId);

    Optional<Submission> findByQuestionnaireIdAndRespondentEmailAndStatus(Long questionnaireId, String respondentEmail, SubmissionStatus status);
}
