package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractItem;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.*;
import thkoeln.archilab.ecommerce.usecases.*;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.*;

@Service
public class ShoppingCartService implements ShoppingCartUseCases, InventoryManagementUseCases {

    private ShoppingCartRepository shoppingCartRepository;
    private ClientCartServiceInterface clientCartServiceInterface;

    private ItemCartServiceInterface itemCartServiceInterface;
    private OrderCartServiceInterface orderCartServiceInterface;
    private PaymentUseCases paymentUseCases;
    private DeliveryUseCases deliveryUseCases;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ClientCartServiceInterface clientCartServiceInterface,
                               ItemCartServiceInterface itemCartServiceInterface, OrderCartServiceInterface orderCartServiceInterface, PaymentUseCases paymentUseCases,
                               DeliveryUseCases deliveryUseCases) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.clientCartServiceInterface = clientCartServiceInterface;
        this.itemCartServiceInterface = itemCartServiceInterface;
        this.orderCartServiceInterface = orderCartServiceInterface;
        this.paymentUseCases = paymentUseCases;
        this.deliveryUseCases = deliveryUseCases;
    }

    @Override
    public void addItemToShoppingCart(MailAddressType clientMailAddress, UUID itemId, int quantity) {
        if (!clientCartServiceInterface.existsClientByEmailMailAddress(clientMailAddress.toString())) {
            throw new ShopException("client does not exist");
        } else if (!itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist");
        } else if (quantity < 0) {
            throw new ShopException("Quantity is negative");
        } else if (itemCartServiceInterface.findById(itemId).isPresent() && itemCartServiceInterface.findById(itemId).get().getAvalibleQuantity() < quantity) {
            throw new ShopException("The item is not available in the requested quantity");
        } else {
            var item = getItem(itemId);
            var client = getClient(clientMailAddress.toString());
            var shoppingCartPart = new ShoppingCartPart(item, quantity);
            if (getShoppingCartByClientMail(clientMailAddress.toString()) == null) {
                var newShoppingCart = new ShoppingCart();
                item.setReservedQuantity(item.reservedQuantity() + quantity);
                newShoppingCart.setAbstractClient(client);
                newShoppingCart.addOrderPart(shoppingCartPart);
                itemCartServiceInterface.save(item);
                shoppingCartRepository.save(newShoppingCart);
            } else {

                var existingShoppingCart = getShoppingCartByClientMail(clientMailAddress.toString());
                if (existingShoppingCart.contains(itemId)) {
                    for (ShoppingCartPart shoppingcartpart1 : existingShoppingCart.getShoppingCartParts()
                    ) {
                        if (shoppingcartpart1.getAbstractItem().getUuid() == itemId) {
                            shoppingcartpart1.setQuantity(shoppingcartpart1.getQuantity() + quantity);
                            item.setReservedQuantity(item.reservedQuantity() + quantity);
                        }
                    }
                    itemCartServiceInterface.save(item);
                    shoppingCartRepository.save(existingShoppingCart);
                } else {

                    item.setReservedQuantity(item.reservedQuantity() + quantity);
                    existingShoppingCart.addOrderPart(shoppingCartPart);
                    itemCartServiceInterface.save(item);
                    shoppingCartRepository.save(existingShoppingCart);
                }
            }
        }

    }

    @Override
    public void removeItemFromShoppingCart(MailAddressType clientMailAddress, UUID itemId, int quantity) {
        var client = getClient(clientMailAddress.toString());
        if (!itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist");
        } else if (quantity < 0) {
            throw new ShopException("Quantity is negative");
        } else if (!getShoppingCartAsMap(clientMailAddress).containsKey(itemId) || getShoppingCartAsMap(clientMailAddress).get(itemId) < quantity) {
            throw new ShopException("quantity is not the same as quantity in shopping cart");
        } else {
            var shoppingCart = getShoppingCartByClientMail(clientMailAddress.toString());

            var item = getItem(itemId);
            for (ShoppingCartPart shoppingCartPart : shoppingCart.getShoppingCartParts()
            ) {
                if (shoppingCartPart.getAbstractItem().getUuid() == itemId) {
                    shoppingCartPart.setQuantity(shoppingCartPart.getQuantity() - quantity);
                    item.setReservedQuantity(item.reservedQuantity() - quantity);
                    if (shoppingCartPart.getQuantity() == 0) {
                        shoppingCart.removeOrderPart(shoppingCartPart);
                    }
                    break;
                }
            }
            itemCartServiceInterface.save(item);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public void setCommentForShoppingCartPart(MailAddressType clientMailAddress, UUID itemId, String comment) {
        if (clientMailAddress == null || !clientCartServiceInterface.existsClientByEmailMailAddress(clientMailAddress.toString())) {
            throw new ShopException("item does not exist");
        }
        if (itemId == null || !itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist");
        }
        var shoppingCart = getShoppingCartByClientMail(clientMailAddress.toString());
        if (!shoppingCart.contains(itemId)) {
            throw new ShopException("item is not in Shopping cart");
        }
        for (ShoppingCartPart part : shoppingCart.getShoppingCartParts()
        ) {
            if (part.getAbstractItem().uuid() == itemId) {
                part.setComment(comment);
            }
        }
        shoppingCartRepository.save(shoppingCart);

    }

    @Override
    public String getCommentForShoppingCartPart(MailAddressType clientMailAddress, UUID itemId) {
        if (clientMailAddress == null || !clientCartServiceInterface.existsClientByEmailMailAddress(clientMailAddress.toString())) {
            throw new ShopException("item does not exist");
        }
        if (itemId == null || !itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist");
        }
        var shoppingCart = getShoppingCartByClientMail(clientMailAddress.toString());
        if (!shoppingCart.contains(itemId)) {
            throw new ShopException("item is not in Shopping cart");
        }
        var comment = "";
        for (ShoppingCartPart shoppingCartPart : shoppingCart.getShoppingCartParts()
        ) {
            if (shoppingCartPart.getAbstractItem().uuid() == itemId) {
                comment = shoppingCartPart.getComment();
                break;
            }
        }
        return comment;
    }

    @Override
    public Map<UUID, Integer> getShoppingCartAsMap(MailAddressType clientMailAddress) {
        var client = getClient(clientMailAddress.toString());
        HashMap<UUID, Integer> hashMap = new HashMap<>();
        var shoppingBasket = getShoppingCartByClientMail(clientMailAddress.toString());
        if (isEmpty(clientMailAddress)) {
            return hashMap;
        }
        for (ShoppingCartPart shoppingCartPart : shoppingBasket.getShoppingCartParts()) {
            hashMap.put(shoppingCartPart.getAbstractItem().getUuid(), shoppingCartPart.getQuantity());
        }

        return hashMap;
    }

    @Override
    public MoneyType getShoppingCartAsMoneyValue(MailAddressType clientMailAddress) {
        var client = getClient(clientMailAddress.toString());
        float sum = 0;
        var currency = "";
        var shoppingCart = getShoppingCartByClientMail(clientMailAddress.toString());
        if (isEmpty(clientMailAddress)) {
            return new Money();
        }
        for (ShoppingCartPart shoppingCartPart : shoppingCart.getShoppingCartParts()
        ) {
            sum += shoppingCartPart.getQuantity() * shoppingCartPart.getAbstractItem().sellPrice().getAmount();
            currency = shoppingCartPart.getAbstractItem().sellPrice().getCurrency();
        }

        return new Money(sum, currency);
    }

    @Override
    public int getReservedInventoryInShoppingCarts(UUID itemId) {
        return getItem(itemId).reservedQuantity();
    }

    //
    @Override
    public boolean isEmpty(MailAddressType clientMailAddress) {
        if (clientMailAddress.toString().isEmpty()) {
            throw new ShopException("the mail address is null or empty");
        }
        var cart = getShoppingCartByClientMail(clientMailAddress.toString());
        return cart == null || cart.getShoppingCartStatus() == ShoppingCartStatus.EMPTY;
    }

    @Override
    public boolean isPaymentAuthorized(MailAddressType clientMailAddress) {
        if (!isEmpty(clientMailAddress)) {
            return getShoppingCartByClientMail(clientMailAddress.toString()).getShoppingCartStatus() == ShoppingCartStatus.PAYMENT_AUTHORIZED;
        } else return false;
    }

    @Override
    public void checkout(MailAddressType clientMailAddress) {
        var client = getClient(clientMailAddress.toString());
        var order = orderCartServiceInterface.creatNewOrder();
        var cart = getShoppingCartByClientMail(clientMailAddress.toString());
        if (isEmpty(clientMailAddress)) {
            throw new ShopException("shopping cart is empty");
        }
        if (cart.getShoppingCartStatus() == ShoppingCartStatus.FILLED) {
            paymentUseCases.authorizePayment(clientMailAddress, getShoppingCartAsMoneyValue(clientMailAddress));
            cart.changeStatusToPaymentAuthorized();
        }
        if (cart.getShoppingCartStatus() == ShoppingCartStatus.PAYMENT_AUTHORIZED) {
            deliveryUseCases.triggerDelivery((ClientType) client, getShoppingCartAsMap(clientMailAddress));
            var orderPartList = new ArrayList<>(cart.getShoppingCartParts());

            for (ShoppingCartPart shoppingCartPart : orderPartList
            ) {
                var item = getItem(shoppingCartPart.getAbstractItem().getUuid());
                item.setReservedQuantity(item.reservedQuantity() - shoppingCartPart.getQuantity());
                item.setQuantity(item.quantity() - shoppingCartPart.getQuantity());
                item.setExistInOrder(true);
                itemCartServiceInterface.save(item);
            }
            cart.changeStatusToDeliveryTriggered();
            ShoppingCart oldShoppingCart = new ShoppingCart();
            oldShoppingCart.getShoppingCartParts().addAll(cart.getShoppingCartParts());

            oldShoppingCart.changeStatusToDeliveryTriggered();
            shoppingCartRepository.save(oldShoppingCart);
            order.setOrderParts(orderPartList);
            orderCartServiceInterface.save(order);
            client.addOrder((Order) order);
            cart.emptyShoppingCart();
            clientCartServiceInterface.save(client);
            shoppingCartRepository.save(cart);
        }
    }

    @Override
    public Map<UUID, Integer> getOrderHistory(MailAddressType clientMailAddress) {
        HashMap<UUID, Integer> hashMap = new HashMap<>();
        for (AbstractOrder order : getClient(clientMailAddress.toString()).orders()) {
            for (ShoppingCartPart shoppingCartPart : order.getOrderParts()) {
                UUID itemUuid = shoppingCartPart.getAbstractItem().getUuid();
                int quantity = shoppingCartPart.getQuantity();
                int previousQuantity = hashMap.getOrDefault(itemUuid, 0);
                hashMap.put(itemUuid, previousQuantity + quantity);
            }
        }
        return hashMap;
    }

    @Override
    public void deleteAllOrders() {
        orderCartServiceInterface.deleteAll();
        shoppingCartRepository.deleteAll();
    }

    private AbstractItem getItem(UUID itemId) {
        var item = itemCartServiceInterface.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ShopException("item does not exist");
        }
    }

    private AbstractClient getClient(String clientMail) {
        if (!clientCartServiceInterface.existsClientByEmailMailAddress(clientMail)) {
            throw new ShopException("client does not exist");
        }
        return clientCartServiceInterface.findByEmailMailAddress(clientMail);
    }


    @Override
    public void addToInventory(UUID itemId, int addedQuantity) {
        if (!itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist!");
        } else if (addedQuantity <= 0) {
            throw new ShopException("quantity is negative!");
        } else {
            var optionalItem = itemCartServiceInterface.findById(itemId);

            if (optionalItem.isPresent()) {
                var item = optionalItem.get();
                item.setQuantity(item.quantity() + addedQuantity);
            }

        }
    }

    @Override
    public void removeFromInventory(UUID itemId, int removedQuantity) {
        var item = getItem(itemId);
        if (removedQuantity <= 0) {
            throw new ShopException("quantity is negative!");
        } else if (removedQuantity > item.quantity()) {
            throw new ShopException("the removed quantity is greater than the current inventory and the currently reserved items together");
        } else {
            var newTotalQuantity = item.quantity() - removedQuantity;
            if (newTotalQuantity >= item.reservedQuantity()) {
                item.setQuantity(newTotalQuantity);
            } else {
                item.setQuantity(newTotalQuantity);
                item.setReservedQuantity(newTotalQuantity);
                //adjustShoppingCart(itemObj, removedQuantity);
                changeShoppingCart(item, removedQuantity);

            }
        }
    }

    @Override
    public void changeInventoryTo(UUID itemId, int newTotalQuantity) {
        var item = getItem(itemId);
        if (newTotalQuantity < 0) {
            throw new ShopException("quantity is negative!");
        } else {
            if (item.quantity() == newTotalQuantity) {
                return;
            }
            if (newTotalQuantity >= item.reservedQuantity()) {
                item.setQuantity(newTotalQuantity);
            } else if (newTotalQuantity == 0) {
                var remove = item.quantity();
                item.setQuantity(newTotalQuantity);
                item.setReservedQuantity(newTotalQuantity);
                //adjustShoppingCart(itemObj, remove);
                changeShoppingCart(item, remove);
            } else {
                var removedQuantity = item.reservedQuantity() - newTotalQuantity;

                item.setQuantity(newTotalQuantity);
                item.setReservedQuantity(newTotalQuantity);
                //adjustShoppingCart(itemObj, removedQuantity);
                changeShoppingCart(item, removedQuantity);

            }
        }
    }


    @Override
    public int getAvailableInventory(UUID itemId) {
        if (!itemCartServiceInterface.existsById(itemId)) {
            throw new ShopException("item does not exist!");
        }
        return getItem(itemId).quantity();

    }

    private void changeShoppingCart(AbstractItem item, int quantityToRemove) {
        List<ShoppingCart> newShoppingCartList = new ArrayList<>();
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAllByShoppingCartStatus(ShoppingCartStatus.FILLED);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            for (ShoppingCartPart shoppingCartPart : shoppingCart.getShoppingCartParts()) {
                if (shoppingCartPart.getAbstractItem().getUuid() == item.getUuid()) {
                    int updatedQuantity;
                    if (shoppingCartPart.getQuantity() < quantityToRemove) {
                        quantityToRemove -= shoppingCartPart.getQuantity();
                        updatedQuantity = 0;
                    } else {
                        updatedQuantity = shoppingCartPart.getQuantity() - quantityToRemove;
                        quantityToRemove = 0;
                    }
                    var newShoppingCartPart = new ShoppingCartPart(item, updatedQuantity);
                    for (int i = 0; i < shoppingCart.getShoppingCartParts().size(); i++) {
                        ShoppingCartPart part = shoppingCart.getShoppingCartParts().get(i);
                        if (part.getAbstractItem().getUuid() == shoppingCartPart.getAbstractItem().getUuid()) {
                            shoppingCart.getShoppingCartParts().set(i, newShoppingCartPart);
                        }
                    }
                    newShoppingCartList.add(shoppingCart);
                }
            }
        }
        shoppingCartRepository.saveAll(newShoppingCartList);
    }

    public ShoppingCart getShoppingCartByClientMail(String mail) {
        var cart = shoppingCartRepository.findShoppingCartByAbstractClientEmailMailAddress(mail);
        return cart.orElse(null);
    }
//    public ShoppingCartDTO getCartDtoByClientMail(String mail) {
//        if (!clientCartServiceInterface.existsClientByEmailMailAddress(mail)){
//            throw new ShopException("client does not exist");
//        }
//        var cart = shoppingCartRepository.findShoppingCartByAbstractClientEmailMailAddress(mail);
//        if (cart.isEmpty()){
//            var client = getClient(mail);
//            var newCart = new ShoppingCart();
//            newCart.setAbstractClient(client);
//            shoppingCartRepository.save(newCart);
//            return mapToShoppingCartDTO(newCart);
//
//        }
//        return mapToShoppingCartDTO(cart.get());
//    }
//    public ShoppingCartDTO mapToShoppingCartDTO(ShoppingCart shoppingCart){
//        var total = 0;
//        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
//        shoppingCartDTO.setId(shoppingCart.getUuid());
//        shoppingCartDTO.setMailAddressString(shoppingCart.getAbstractClient().mail());
//        for (ShoppingCartPart shoppingCartPart:shoppingCart.getShoppingCartParts()
//             ) {
//            total+=shoppingCartPart.getQuantity();
//
//        }
//        shoppingCartDTO.setTotalQuantity(total);
//        return shoppingCartDTO;
//    }
//
//    public ShoppingCartPartDTO getCartPartByIdAndItemId(UUID cartId,UUID itemId){
//        var cart = shoppingCartRepository.findById(cartId);
//        if (cart.isEmpty() || !cart.get().contains(itemId)){
//            return null;
//        }
//        ShoppingCartPartDTO cartPartDTO = new ShoppingCartPartDTO();
//        cartPartDTO.setItemId(itemId);
//        cartPartDTO.setQuantity(cart.get().getPart(itemId).getQuantity());
//        cartPartDTO.setComment(cart.get().getPart(itemId).getComment());
//        return cartPartDTO;
//    }
//    public boolean deletePartFromCart (UUID cartId, UUID itemId){
//        var removed = false;
//        var cart = shoppingCartRepository.findById(cartId);
//        if (cart.isEmpty() || !cart.get().contains(itemId)){
//           throw new ShopException("item is not in ShoppingCart");
//        }
//        for (ShoppingCartPart shoppingCartPart:cart.get().getShoppingCartParts()
//             ) {
//            if (shoppingCartPart.getAbstractItem().uuid()==itemId){
//               cart.get().removeOrderPart(shoppingCartPart);
//               removed = true;
//                break;
//            }
//
//        }
//        shoppingCartRepository.save(cart.get());
//        return removed;
//    }
}
