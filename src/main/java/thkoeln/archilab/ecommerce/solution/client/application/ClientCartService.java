package thkoeln.archilab.ecommerce.solution.client.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.solution.client.domain.Client;
import thkoeln.archilab.ecommerce.solution.client.domain.ClientRepository;
import thkoeln.archilab.ecommerce.solution.shoppingcart.application.ClientCartServiceInterface;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractClient;

@Service
public class ClientCartService implements ClientCartServiceInterface {
    private ClientRepository clientRepository;
    @Autowired
    public ClientCartService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void save(AbstractClient abstractClient) {
        clientRepository.save((Client) abstractClient);
    }

    @Override
    public boolean existsClientByEmailMailAddress(String email) {
        return clientRepository.existsClientByEmailMailAddress(email);
    }



    @Override
    public AbstractClient findByEmailMailAddress(String mail) {
        var client= clientRepository.findByEmailMailAddress(mail);
        if (client.isEmpty()){
            throw new ShopException("client does not exist!");
        }
        return client.get(0);
    }
}
