package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public abstract class AbstractOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    public abstract void setOrderParts(List<ShoppingCartPart> partList);

    public abstract List<ShoppingCartPart> getOrderParts();


}
