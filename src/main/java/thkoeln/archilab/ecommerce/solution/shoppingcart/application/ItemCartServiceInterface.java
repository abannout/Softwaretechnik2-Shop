package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractItem;

import java.util.Optional;
import java.util.UUID;

public interface ItemCartServiceInterface {
    public Optional<AbstractItem> findById(UUID itemId);
    public boolean existsById(UUID itemId);
    public void save(AbstractItem abstractItem);
}
