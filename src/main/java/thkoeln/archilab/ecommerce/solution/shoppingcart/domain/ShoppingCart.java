package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private ShoppingCartStatus shoppingCartStatus;

    @ElementCollection(targetClass = ShoppingCartPart.class, fetch = FetchType.LAZY)
    private List<ShoppingCartPart> shoppingCartParts = new ArrayList<>();

    @OneToOne
    private AbstractClient abstractClient;

    public ShoppingCart() {
        this.shoppingCartStatus = ShoppingCartStatus.EMPTY;
    }

    public void addOrderPart(ShoppingCartPart shoppingCartPart) {
        shoppingCartParts.add(shoppingCartPart);
        if (shoppingCartStatus == ShoppingCartStatus.EMPTY) {
            setShoppingCartStatus(ShoppingCartStatus.FILLED);
        }
    }

    public void removeOrderPart(ShoppingCartPart shoppingCartPart) {
        shoppingCartParts.remove(shoppingCartPart);
        if (shoppingCartParts.isEmpty()) {
            setShoppingCartStatus(ShoppingCartStatus.EMPTY);
        }
    }

    public void emptyShoppingCart() {
        shoppingCartParts.clear();
        setShoppingCartStatus(ShoppingCartStatus.EMPTY);
    }

    public boolean contains(UUID itemId) {
        for (ShoppingCartPart shoppingCartPart : shoppingCartParts
        ) {
            if (shoppingCartPart.getAbstractItem().uuid().equals(itemId)) {
                return true;
            }
        }
        return false;
    }
    public ShoppingCartPart getPart(UUID itemId){
        for (ShoppingCartPart shoppingCartPart : shoppingCartParts
        ) {
            if (shoppingCartPart.getAbstractItem().uuid().equals(itemId)) {
                return shoppingCartPart;
            }
        }
        return null;
    }
    public int getItemTotal(){
        var total =0;
        for (ShoppingCartPart shoppingCartPart : shoppingCartParts
        ) {
            total+=shoppingCartPart.getQuantity();
        }
        return total;
    }

    public void changeStatusToFilled() {
        this.shoppingCartStatus = ShoppingCartStatus.FILLED;
    }

    public void changeStatusToPaymentAuthorized() {
        this.shoppingCartStatus = ShoppingCartStatus.PAYMENT_AUTHORIZED;
    }

    public void changeStatusToDeliveryTriggered() {
        this.shoppingCartStatus = ShoppingCartStatus.DELIVERY_TRIGGERED;
    }
}
