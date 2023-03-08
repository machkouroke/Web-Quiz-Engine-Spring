package engine.models.keys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@RequiredArgsConstructor
public class QuizzAnsweringKey implements Serializable {

    private static final long serialVersionUID = 8689547075517699380L;
    @Column(name = "quiz_id")
    @NonNull
    @JsonProperty("id")
    private Integer quizId;

    @Column(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull
    private String userId;
    @Column(name = "completedAt")
    @NonNull
    private Date completedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizzAnsweringKey answerKey = (QuizzAnsweringKey) o;
        return Objects.equals(quizId, answerKey.quizId) &&
                Objects.equals(userId, answerKey.userId) &&
                Objects.equals(completedAt, answerKey.completedAt);
    }
}
