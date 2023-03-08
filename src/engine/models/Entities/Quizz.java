package engine.models.Entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

import java.util.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quizz {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizz_id", nullable = false)
    private Integer id;
    @NonNull
    @Column(name = "title")
    private String title;
    @NonNull
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Answer> options = new ArrayList<>();

    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<QuizzAnswering> answeredUser = new HashSet<>();


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Integer> getAnswer() {
        return options.stream()
                .filter(Answer::isCorrect)
                .map(Answer::getId)
                .toList();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Quizz quizz = (Quizz) o;
        return id != null && Objects.equals(id, quizz.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public List<String> getOptions() {
        return options.stream().map(Answer::getOption).toList();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Answer> getOptionsObject() {

        return options;
    }

    public void setOptions(List<Answer> options) {
        this.options = options;
        this.options.forEach(option -> {
            option.setQuizz(this);
            option.setId(this.options.indexOf(option));
        });

    }

}
