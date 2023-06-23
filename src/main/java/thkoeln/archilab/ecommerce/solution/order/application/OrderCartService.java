package thkoeln.archilab.ecommerce.solution.order.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderRepository;
import thkoeln.archilab.ecommerce.solution.shoppingcart.application.OrderCartServiceInterface;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractOrder;

@Service
public class OrderCartService implements OrderCartServiceInterface  {
    private OrderRepository orderRepository;
    @Autowired
    public OrderCartService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(AbstractOrder order) {
        orderRepository.save((Order) order);
    }


    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    @Override
    public AbstractOrder creatNewOrder() {
        return new Order();
    }


}
