package thkoeln.archilab.ecommerce.solution.delivery.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.solution.client.domain.ClientRepository;
import thkoeln.archilab.ecommerce.solution.delivery.domain.Delivery;
import thkoeln.archilab.ecommerce.solution.delivery.domain.DeliveryRepository;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.DeliveryUseCases;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DeliveryService implements DeliveryUseCases {
    private DeliveryRepository deliveryRepository;

    private ClientRepository clientRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, ClientRepository clientRepository) {
        this.deliveryRepository = deliveryRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public UUID triggerDelivery(ClientType deliveryRecipient, Map<UUID, Integer> deliveryContent) {
        if (deliveryRecipient == null) {
            throw new ShopException("deliveryRecipient is null");
        }

        var client = clientRepository.findByEmailMailAddressString(deliveryRecipient.getMailAddress().toString()).get(0);
        Delivery delivery = new Delivery(deliveryContent, client);
        deliveryRepository.save(delivery);
        return delivery.getUuid();
    }

    @Override
    public Map<UUID, Integer> getDeliveryHistory(MailAddressType clientMailAddress) {
        HashMap<UUID, Integer> deliveryHistory = new HashMap<>();
        var deliveryList = deliveryRepository.getAllByClientEmailMailAddressString(clientMailAddress.toString());
        for (Delivery delivery : deliveryList) {
            delivery.getDeliveryContent().forEach((uuid, integer) -> {
                var oldQuantity = deliveryHistory.getOrDefault(uuid, 0);
                deliveryHistory.put(uuid,oldQuantity+integer);
            });
        }
        return deliveryHistory;
    }

    @Override
    public void deleteDeliveryHistory() {
    deliveryRepository.deleteAll();
    }


}
