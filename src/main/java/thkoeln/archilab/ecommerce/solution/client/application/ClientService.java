package thkoeln.archilab.ecommerce.solution.client.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.domainprimitives.HomeAddress;
import thkoeln.archilab.ecommerce.domainprimitives.MailAddress;
import thkoeln.archilab.ecommerce.domainprimitives.PostalCode;
import thkoeln.archilab.ecommerce.solution.client.domain.Client;
import thkoeln.archilab.ecommerce.solution.client.domain.ClientRepository;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.ShoppingCartRepository;
import thkoeln.archilab.ecommerce.usecases.ClientRegistrationUseCases;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;


/*
 * interface order-ShoppingCart
 * interface item_Shoppingcart
 * interface ShoppingCart-Item
 *
 * */
@Service
public class ClientService implements ClientRegistrationUseCases {

    private final ClientRepository clientRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ClientCartService clientCartService;
    @Autowired
    public ClientService(ClientRepository clientRepository, ShoppingCartRepository shoppingCartRepository, ClientCartService clientCartService) {
        this.clientRepository = clientRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.clientCartService = clientCartService;
    }

    @Override
    public void register(String name, MailAddressType mailAddress, HomeAddressType homeAddress) {
        if (name == null || homeAddress == null || mailAddress == null
                || name.isEmpty()) {
            throw new ShopException("invalid data!");
        } else if (clientRepository.existsClientByEmailMailAddressString(mailAddress.toString())) {
            throw new ShopException("client already exist");
        } else {
            var postalcode = new PostalCode(homeAddress.getPostalCode().toString());
            var address = new HomeAddress(homeAddress.getStreet(), homeAddress.getCity(), postalcode);
            var client = new Client(new MailAddress(mailAddress.toString()), name, address );
            clientRepository.save(client);

        }
    }

    @Override
    public void changeAddress(MailAddressType clientMailAddress, HomeAddressType homeAddress) {
        if (clientMailAddress == null || homeAddress == null) {
            throw new ShopException("invalid data!");
        } else if (!clientRepository.existsClientByEmailMailAddressString(clientMailAddress.toString())) {
            throw new ShopException("client does not exist");
        } else {
            var optionalClient = clientRepository.findByEmailMailAddressString(clientMailAddress.toString());
            var client = optionalClient.get(0);
            client.setAddress((HomeAddress) homeAddress);
            clientRepository.save(client);
        }
    }

    @Override
    public ClientType getClientData(MailAddressType clientMailAddress) {
        if (!clientRepository.existsClientByEmailMailAddressString(clientMailAddress.toString())) {
            throw new ShopException("client does not exist");
        }
        var optionalClient = clientRepository.findByEmailMailAddressString(clientMailAddress.toString());
        return optionalClient.get(0);
    }

    @Override
    public void deleteAllClients() {
        clientRepository.deleteAll();
    }
}
