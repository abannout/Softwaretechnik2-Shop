package thkoeln.archilab.ecommerce.solution.order.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.solution.item.domain.Item;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractOrder;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.ShoppingCartPart;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "ClientOrder")
@NoArgsConstructor
public class Order extends AbstractOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ElementCollection(targetClass = OrderPart.class,fetch = FetchType.EAGER)
    private List<OrderPart> orderParts = new ArrayList<>();


    public List<OrderPart> orderPartsList() {
        return orderParts;
    }
    @Override
    public List<ShoppingCartPart> getOrderParts() {
        List<ShoppingCartPart> list = new ArrayList<> ();
        for (OrderPart orderPart: orderParts
             ) {
            list.add(new ShoppingCartPart(orderPart.getItem(), orderPart.getQuantity()));
        }

        return list;
    }
    @Override
    public  void setOrderParts(List<ShoppingCartPart> partList){

        for (ShoppingCartPart shoppingCartPart: partList
        ) {
            orderParts.add(new OrderPart((Item)shoppingCartPart.getAbstractItem(), shoppingCartPart.getQuantity(),shoppingCartPart.getComment()));
        }
    }

}
