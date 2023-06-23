package thkoeln.archilab.ecommerce.solution.payment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.client.domain.Client;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private Money payment;

    @ManyToOne
    private Client client;

    public Payment(Client client,Money amount) {
        this.client = client;
        this.payment = amount;
    }

    public void setPayment(Money payment) {
        this.payment = payment;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
