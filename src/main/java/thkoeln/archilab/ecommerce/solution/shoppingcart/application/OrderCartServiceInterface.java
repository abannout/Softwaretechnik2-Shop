package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractOrder;

public interface OrderCartServiceInterface {
    public void save(AbstractOrder order);
    public void deleteAll();
    public AbstractOrder creatNewOrder();
}
