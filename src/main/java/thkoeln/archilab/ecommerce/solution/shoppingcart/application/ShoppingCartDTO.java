package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thkoeln.archilab.ecommerce.domainprimitives.MailAddress;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO {
    private UUID id;
    private MailAddress mailAddressString;
    private int totalQuantity;
}
