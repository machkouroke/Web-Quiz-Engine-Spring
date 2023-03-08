package engine.models.Entities.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.models.Entities.Answer;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class QuizzDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotNull(message = "Title is required")
    @NonNull
    @NotEmpty(message = "Title must not be empty")
    private String title;
    @NotNull(message = "Text is required")
    @NonNull
    @NotEmpty(message = "Text must not be empty")
    private String text;

    @Size(min = 2, message = "Options must contain at least 2 elements")
    private List<String> options = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer = new ArrayList<>();
}
