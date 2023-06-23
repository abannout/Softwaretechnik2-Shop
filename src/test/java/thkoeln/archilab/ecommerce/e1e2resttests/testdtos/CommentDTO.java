package thkoeln.archilab.ecommerce.e1e2resttests.testdtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

/**
 * A DTO containing just a comment, used in testing.
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDTO {
    private UUID itemId;
    private String comment;
}

