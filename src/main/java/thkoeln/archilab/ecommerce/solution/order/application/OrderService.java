package thkoeln.archilab.ecommerce.solution.order.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;

import java.util.List;

@Service
public class OrderService {
    private ClientOrderServiceInterface clientOrderServiceInterface;

    @Autowired
    public OrderService( ClientOrderServiceInterface clientOrderServiceInterface) {
        this.clientOrderServiceInterface = clientOrderServiceInterface;
    }
    public List<Order> getOrders(String mailAddress){
       return clientOrderServiceInterface.getOrders(mailAddress);

    }

    public Order getLatestOrder(String mailAddress){
        return clientOrderServiceInterface.getLatestOrder(mailAddress);
    }
}
