package accesa.eu.jokeapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Joke {

    private String type;

    private String setup;

    private String punchline;

    private Integer id;

    private Long rating;
}
