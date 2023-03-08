package engine.models.keys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AnswerKey implements Serializable {
    private Integer id;
    private Integer quizz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerKey answerKey = (AnswerKey) o;
        return Objects.equals(id, answerKey.id) &&
                Objects.equals(quizz, answerKey.quizz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizz);
    }
}