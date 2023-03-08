package engine.models.Entities;

import engine.models.keys.AnswerKey;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@IdClass(AnswerKey.class)
@Table(name = "answers")
public class Answer {
    @Id
    @Column(name = "options_id", nullable = false)
    private Integer id;

    @NonNull
    @NotNull(message = "Options is required")
    @Column(name = "option")
    private String option;
    @NonNull
    @NotNull(message = "isCorrect is required")
    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "quizz_id", nullable = false)
    @Id
    private Quizz quizz;

    public Answer(Integer id, Quizz quizz, String option, boolean isCorrect) {
        this.id = Objects.requireNonNull(id, "Answer ID cannot be null");
        this.quizz = Objects.requireNonNull(quizz, "Answer Quizz cannot be null");
        this.option = Objects.requireNonNull(option, "Answer option cannot be null");
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) &&
                Objects.equals(quizz, answer.quizz);
    }

    @Override

    public int hashCode() {
        return getClass().hashCode();
    }
}
