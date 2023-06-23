package thkoeln.archilab.ecommerce.solution.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thkoeln.archilab.ecommerce.solution.item.domain.Item;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPart  {

    @ManyToOne
    private Item item;
    private int quantity;
    private String comment = "";


}
