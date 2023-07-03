package thkoeln.archilab.ecommerce.solution.shoppingcart.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, UUID> {

    List<ShoppingCart> findAllByShoppingCartStatus(ShoppingCartStatus shoppingCartStatus);
    Optional<ShoppingCart> findShoppingCartByAbstractClientEmailMailAddressString(String mail);


}
