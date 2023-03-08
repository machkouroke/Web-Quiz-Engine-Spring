package engine.models.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.models.keys.QuizzAnsweringKey;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class QuizzAnswering {
    @EmbeddedId
    @Id
    private QuizzAnsweringKey id;

    @ManyToOne
    @MapsId("userId")
    @NonNull
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    User user;

    @ManyToOne
    @MapsId("quizId")
    @NonNull
    @JoinColumn(name = "quiz_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Quizz quizz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizzAnswering answerKey = (QuizzAnswering) o;
        return Objects.equals(id, answerKey.id);
    }

}
