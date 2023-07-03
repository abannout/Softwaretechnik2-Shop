package thkoeln.archilab.ecommerce.solution.client.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.client.domain.ClientRepository;
import thkoeln.archilab.ecommerce.solution.order.application.ClientOrderServiceInterface;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientOrderService implements ClientOrderServiceInterface {
    private ClientRepository clientRepository;
    @Autowired
    public ClientOrderService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Order> getOrders(String mailAddress) {
        var client = clientRepository.findByEmailMailAddressString(mailAddress);
        List<Order> orders = new ArrayList<>();
        if (!client.isEmpty()){

            for (Order order:client.get(0).getOrders()
                 ) {
                orders.add(order);

            }
        }
        return orders;
    }

    @Override
    public Order getLatestOrder(String mailAddress) {
        var client = clientRepository.findByEmailMailAddressString(mailAddress);
        if (!client.isEmpty()){return client.get(0).getOrders().get(client.get(0).getOrders().size()-1);}
        return null;
    }
}
