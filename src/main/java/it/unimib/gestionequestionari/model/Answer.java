package it.unimib.gestionequestionari.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"submission_id", "question_id"}))
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "submission_id")
    private QuestionnaireSubmission submission;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(length = 2000)
    private String textAnswer; // OPEN

    @Column(length = 2000)
    private String choiceAnswer; // MULTIPLE_CHOICE (mínimo viable)

    public Answer() {}

    public Answer(QuestionnaireSubmission submission, Question question) {
        this.submission = submission;
        this.question = question;
    }

    public Long getId() { return id; }

    public QuestionnaireSubmission getSubmission() { return submission; }
    public void setSubmission(QuestionnaireSubmission submission) { this.submission = submission; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public String getTextAnswer() { return textAnswer; }
    public void setTextAnswer(String textAnswer) { this.textAnswer = textAnswer; }

    public String getChoiceAnswer() { return choiceAnswer; }
    public void setChoiceAnswer(String choiceAnswer) { this.choiceAnswer = choiceAnswer; }
}