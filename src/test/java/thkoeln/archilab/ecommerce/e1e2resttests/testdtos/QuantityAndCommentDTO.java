package thkoeln.archilab.ecommerce.e1e2resttests.testdtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

/**
 * A DTO containing just a quantity, used in testing.
 */


@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuantityAndCommentDTO {
    private UUID itemId;
    private Integer quantity;
    private String comment;

    public QuantityAndCommentDTO( UUID itemId, Integer quantity, String comment ) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.comment = comment;
    }
}

