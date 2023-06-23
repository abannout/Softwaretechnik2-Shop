package thkoeln.archilab.ecommerce.solution.item.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractItem;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Item extends AbstractItem {

    @Id
    private UUID uuid;
    private String name;
    private String description;
    private Float size;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="moneyAmount", column= @Column(name="purchaseAmount")),
            @AttributeOverride(name="currency", column= @Column(name="purchaseCurrency"))
    })
    private Money purchasePrice;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="moneyAmount", column= @Column(name="sellPrice")),
            @AttributeOverride(name="currency", column= @Column(name="sellCurrency"))
    })
    private Money sellPrice;
    private boolean existInOrder;
    private int reservedQuantity;
    private int quantity;

    public Item(UUID uuid, String name, String description, Float size, Money purchasePrice,
                Money sellPrice) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.size = size;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.reservedQuantity = 0;
        this.quantity=0;
        this.existInOrder =false;
    }


    public void reserveQuantity(int quantity) {
        if (quantity <= this.quantity) {
            this.reservedQuantity += quantity;
            this.quantity -= quantity;
        } else throw new ShopException("quantity to be reserved is more than what is in storage");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public void setPurchasePrice(Money purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSellPrice(Money sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public Money sellPrice() {
        return sellPrice;
    }

    @Override
    public boolean existInOrder() {
        return existInOrder;
    }

    @Override
    public int reservedQuantity() {
        return reservedQuantity;
    }

    @Override
    public int quantity() {
        return quantity;
    }

    @Override
    public int getAvalibleQuantity() {
        return quantity - reservedQuantity;
    }

    @Override
    public void setExistInOrder(boolean b) {
       this.existInOrder = b ;
    }
    @Override
    public  void setQuantity(int quantity){
        this.quantity=quantity;
    }
    @Override
    public  void setReservedQuantity(int quantity){
        this.reservedQuantity=quantity;
    }



}
