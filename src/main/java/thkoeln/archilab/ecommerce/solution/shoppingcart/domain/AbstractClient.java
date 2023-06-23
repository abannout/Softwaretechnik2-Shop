package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import lombok.Getter;
import lombok.Setter;
import thkoeln.archilab.ecommerce.domainprimitives.MailAddress;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public abstract class AbstractClient {
    @Id
    private UUID uuid;
    private MailAddress email;

    public AbstractClient() {
        this.uuid=UUID.randomUUID();
        this.email = mail();
    }
    public abstract MailAddress mail();
    public abstract List<AbstractOrder> orders();
    public abstract void addOrder(AbstractOrder order);

}
