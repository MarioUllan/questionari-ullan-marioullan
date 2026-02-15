package it.unimib.gestionequestionari.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questionnaire_submissions")
public class QuestionnaireSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String accessCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubmissionStatus status = SubmissionStatus.DRAFT;

    @Column(nullable = false, length = 200)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public QuestionnaireSubmission() {}

    public QuestionnaireSubmission(String accessCode, String email, Questionnaire questionnaire) {
        this.accessCode = accessCode;
        this.email = email;
        this.questionnaire = questionnaire;
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Questionnaire getQuestionnaire() { return questionnaire; }
    public void setQuestionnaire(Questionnaire questionnaire) { this.questionnaire = questionnaire; }

    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}