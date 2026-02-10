package it.unimib.gestionequestionari.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El texto de la pregunta es obligatorio")
    @Size(max = 500, message = "Máximo 500 caracteres")
    @Column(nullable = false, length = 500)
    private String text;

    @NotNull(message = "El tipo de pregunta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String category;

    public Question() {}

    public Question(String text, QuestionType type, String category) {
        this.text = text;
        this.type = type;
        this.category = category;
    }

    public Long getId() { return id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
