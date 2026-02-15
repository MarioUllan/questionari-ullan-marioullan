package it.unimib.gestionequestionari.repository;

import it.unimib.gestionequestionari.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Modifying
    @Query("delete from Answer a where a.submission.id = :submissionId")
    void deleteBySubmissionId(@Param("submissionId") Long submissionId);
}