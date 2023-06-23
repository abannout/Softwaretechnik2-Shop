package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import lombok.Getter;
import lombok.Setter;
import thkoeln.archilab.ecommerce.domainprimitives.Money;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
@Setter
@Getter
@Entity

public abstract class AbstractItem {
    @Id
    private UUID uuid;

    public AbstractItem() {
        this.uuid = uuid();
    }
    public abstract UUID uuid();
    public abstract Money sellPrice();
    public abstract boolean existInOrder();
    public abstract int reservedQuantity();
    public abstract int quantity();
    public abstract int getAvalibleQuantity();

    public abstract void setExistInOrder(boolean b);
    public abstract void setQuantity(int quantity);
    public abstract void setReservedQuantity(int quantity);





}
