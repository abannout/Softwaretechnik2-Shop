package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class ShoppingCartPart {
    @ManyToOne
    private AbstractItem abstractItem;
    private int quantity;
    private String comment = "";

    public ShoppingCartPart(AbstractItem abstractItem, int quantity) {
        this.abstractItem = abstractItem;
        this.quantity = quantity;
    }
}
