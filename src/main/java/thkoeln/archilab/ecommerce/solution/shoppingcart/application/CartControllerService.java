package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractClient;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.ShoppingCart;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.ShoppingCartPart;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.ShoppingCartRepository;

import java.util.UUID;

@Service
public class CartControllerService {
    private ClientCartServiceInterface clientCartServiceInterface;
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartService shoppingCartService;

    @Autowired
    public CartControllerService(ClientCartServiceInterface clientCartServiceInterface, ShoppingCartRepository shoppingCartRepository, ShoppingCartService shoppingCartService) {
        this.clientCartServiceInterface = clientCartServiceInterface;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public ShoppingCartDTO getCartDtoByClientMail(String mail) {
        if (!clientCartServiceInterface.existsClientByEmailMailAddress(mail)){
            throw new ShopException("client does not exist");
        }
        var cart = shoppingCartRepository.findShoppingCartByAbstractClientEmailMailAddressString(mail);
        if (cart.isEmpty()){
            var client = getClient(mail);
            var newCart = new ShoppingCart();
            newCart.setAbstractClient(client);
            shoppingCartRepository.save(newCart);
            return mapToShoppingCartDTO(newCart);

        }
        return mapToShoppingCartDTO(cart.get());
    }
    public ShoppingCartDTO mapToShoppingCartDTO(ShoppingCart shoppingCart){
        var total = 0;
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setId(shoppingCart.getUuid());
        shoppingCartDTO.setMailAddressString(shoppingCart.getAbstractClient().mail());
        for (ShoppingCartPart shoppingCartPart:shoppingCart.getShoppingCartParts()
        ) {
            total+=shoppingCartPart.getQuantity();

        }
        shoppingCartDTO.setTotalQuantity(total);
        return shoppingCartDTO;
    }

    public ShoppingCartPartDTO getCartPartByIdAndItemId(UUID cartId, UUID itemId){
        var cart = shoppingCartRepository.findById(cartId);
        if (cart.isEmpty() || !cart.get().contains(itemId)){
            return null;
        }
        ShoppingCartPartDTO cartPartDTO = new ShoppingCartPartDTO();
        cartPartDTO.setItemId(itemId);
        cartPartDTO.setQuantity(cart.get().getPart(itemId).getQuantity());
        cartPartDTO.setComment(cart.get().getPart(itemId).getComment());
        return cartPartDTO;
    }
    public void deletePartFromCart (UUID cartId, UUID itemId){
        var removed = false;
        var cart = shoppingCartRepository.findById(cartId);
        if (cart.isEmpty() || !cart.get().contains(itemId)){
            throw new ShopException("item is not in ShoppingCart");
        }
        var toRemove =cart.get().getPart(itemId).getQuantity();
       shoppingCartService.removeItemFromShoppingCart(cart.get().getAbstractClient().mail(), itemId,toRemove);
        shoppingCartRepository.save(cart.get());
        }


    private AbstractClient getClient(String clientMail) {
        if (!clientCartServiceInterface.existsClientByEmailMailAddress(clientMail)) {
            throw new ShopException("client does not exist");
        }
        return clientCartServiceInterface.findByEmailMailAddress(clientMail);
    }
    public ShoppingCartDTO createCartPartFromDTO(UUID shoppingCartId,ShoppingCartPartDTO cartPartDTO){
        var cart = shoppingCartRepository.findById(shoppingCartId);
        if (cart.isEmpty()){
            throw new ShopException("No Cart ! :(");
        }
        if (cartPartDTO.getQuantity() < 0){
            cartPartDTO.setQuantity(Math.abs(cartPartDTO.getQuantity()));
            cart.get().getPart(cartPartDTO.getItemId()).setComment(cartPartDTO.getComment());
            shoppingCartService.removeItemFromShoppingCart(cart.get().getAbstractClient().mail(), cartPartDTO.getItemId(), cartPartDTO.getQuantity());


            shoppingCartRepository.save(cart.get());
            return mapToShoppingCartDTO(cart.get());
        }
        else if (cartPartDTO.getQuantity() > 0){
            shoppingCartService.addItemToShoppingCart(cart.get().getAbstractClient().mail(), cartPartDTO.getItemId(), cartPartDTO.getQuantity());
            cart.get().getPart(cartPartDTO.getItemId()).setComment(cartPartDTO.getComment());
            shoppingCartRepository.save(cart.get());
            return mapToShoppingCartDTO(cart.get());
        }
        else {return mapToShoppingCartDTO(cart.get());}
    }
    public ShoppingCart getCartById (UUID cartId){
        var cart = shoppingCartRepository.findById(cartId);
        return cart.orElse(null);
    }

}
