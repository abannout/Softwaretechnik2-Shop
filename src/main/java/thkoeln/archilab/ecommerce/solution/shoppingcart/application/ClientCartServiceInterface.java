package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractClient;

public interface ClientCartServiceInterface {
    public void save(AbstractClient abstractClient);
    public boolean existsClientByEmailMailAddress(String email);
    public AbstractClient findByEmailMailAddress(String mail);

}
