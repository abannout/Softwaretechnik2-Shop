package thkoeln.archilab.ecommerce.solution.client.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.domainprimitives.HomeAddress;
import thkoeln.archilab.ecommerce.domainprimitives.MailAddress;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractClient;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractOrder;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Client extends AbstractClient implements ClientType {
    @Id
    private UUID uuid;
    @Embedded
    private MailAddress email;
    private String name;
    @Embedded
    private HomeAddress address;


    @OneToMany
    private List<Order> orders = new ArrayList<>();

    public Client(MailAddress email, String name, HomeAddress address) {
        this.uuid=UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.address = address;
    }

    @Override
    public void setEmail(MailAddress email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(HomeAddress address) {
        this.address = address;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public MailAddress mail() {
        return email;
    }

    @Override
    public List<AbstractOrder> orders() {
        return orders.stream().map(order -> (AbstractOrder) order).collect(Collectors.toList());
    }

    @Override
    public void addOrder(AbstractOrder order) {
        orders.add((Order) order);
    }

    @Override
    public MailAddressType getMailAddress() {
        return this.email;
    }

    @Override
    public HomeAddressType getHomeAddress() {
        return address;
    }
    @Override
    public String getName() {
        return name;
    }


}
