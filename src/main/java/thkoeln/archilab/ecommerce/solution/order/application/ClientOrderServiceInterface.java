package thkoeln.archilab.ecommerce.solution.order.application;

import thkoeln.archilab.ecommerce.solution.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

public interface ClientOrderServiceInterface {
    public List<Order> getOrders(String mailAddress);

    public Order getLatestOrder(String mailAddress);
}
