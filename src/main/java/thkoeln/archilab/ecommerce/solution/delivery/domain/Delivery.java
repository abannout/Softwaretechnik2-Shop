package thkoeln.archilab.ecommerce.solution.delivery.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.solution.client.domain.Client;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ElementCollection
    private Map<UUID, Integer> deliveryContent;

    @ManyToOne
    private Client client;

    public void setDeliveryContent(Map<UUID, Integer> deliveryContent) {
        this.deliveryContent = deliveryContent;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Delivery(Map<UUID, Integer> deliveryContent, Client client) {
        this.deliveryContent = deliveryContent;
        this.client = client;
    }
}
