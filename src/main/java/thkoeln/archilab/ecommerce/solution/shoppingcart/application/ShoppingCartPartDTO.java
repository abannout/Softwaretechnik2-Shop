package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartPartDTO {
    private UUID itemId;
    private int quantity;
    private String comment = "";
}
