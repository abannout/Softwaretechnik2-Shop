package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of use cases dealing with the shopping cart.
 * The interface is probably incomplete, and will grow over time.
 */
public interface ShoppingCartUseCases {
    /**
     * Adds a item to the shopping cart of a client
     *
     * @param clientMailAddress
     * @param itemId
     * @param quantity
     * @throws ShopException if ...
     *      - the client with the given mailAddress does not exist,
     *      - the item does not exist,
     *      - the quantity <= 0,
     *      - the item is not available in the requested quantity
     */
    public void addItemToShoppingCart( MailAddressType clientMailAddress, UUID itemId, int quantity );


    /**
     * Removes a item from the shopping cart of a client
     *
     * @param clientMailAddress
     * @param itemId
     * @param quantity
     * @throws ShopException if ...
     *      - clientMailAddress is null,
     *      - the client with the given mailAddress does not exist,
     *      - the item does not exist
     *      - the quantity <= 0,
     *      - the item is not in the shopping cart in the requested quantity
     */
    public void removeItemFromShoppingCart( MailAddressType clientMailAddress, UUID itemId, int quantity );


    /**
     * Sets a comment in the shopping cart part containing a item
     *
     * @param clientMailAddress
     * @param itemId
     * @param comment (can be null)
     * @throws ShopException if ...
     *      - clientMailAddress is null,
     *      - the client with the given mailAddress does not exist,
     *      - the item does not exist
     *      - the item is not in the shopping cart
     */
    public void setCommentForShoppingCartPart( MailAddressType clientMailAddress, UUID itemId, String comment );


    /**
     * Returns the comment in the shopping cart part containing a item
     *
     * @param clientMailAddress
     * @param itemId
     * @return the comment (can be null)
     * @throws ShopException if ...
     *      - clientMailAddress is null,
     *      - the client with the given mailAddress does not exist,
     *      - the item does not exist
     *      - the item is not in the shopping cart
     */
    public String getCommentForShoppingCartPart( MailAddressType clientMailAddress, UUID itemId );


    /**
     * Returns a map showing which items are in the shopping cart of a client and how many of each item
     *
     * @param clientMailAddress
     * @return the shopping cart of the client (map is empty if the shopping cart is empty)
     * @throws ShopException if ...
     *      - clientMailAddress is null,
     *      - the client with the given mailAddress does not exist
     */
    public Map<UUID, Integer> getShoppingCartAsMap( MailAddressType clientMailAddress );


    /**
     * Returns the current value of all items in the shopping cart of a client
     *
     * @param clientMailAddress
     * @return the value of shopping cart of the client
     * @throws ShopException if ...
     *      - clientMailAddress is null,
     *      - the client with the given mailAddress does not exist
     */
    public MoneyType getShoppingCartAsMoneyValue( MailAddressType clientMailAddress );



    /**
     * Get the number units of a specific item that are currently reserved in the shopping carts of all clients
     * @param itemId
     * @return the number of reserved items of that type in all shopping carts
     * @throws ShopException
     *      - itemId is null
     *      - if the item id does not exist
     */
    public int getReservedInventoryInShoppingCarts( UUID itemId );


    /**
     * Checks if the shopping cart of a client is empty
     *
     * @param clientMailAddress
     * @return true if the shopping cart is empty, false otherwise
     * @throws ShopException if ...
     *    - clientMailAddress is null
     *    - the client with the given mail address does not exist
     */
    public boolean isEmpty( MailAddressType clientMailAddress );


    /**
     * Checks if the payment for a specific shopping cart of a client has been authorized to be paid,
     * i.e. the shopping cart is not empty, the client has given his/her payment details, and the payment
     * has been authorized (under the limits of the client's credit card). However, the order
     * has not yet been placed yet, and the logistics details have not yet been given.
     *
     * @param clientMailAddress
     * @return true if the payment has been authorized, false otherwise
     * @throws ShopException if ...
     *      - clientMailAddress is null
     *      - the client with the given mail address does not exist
     */
    public boolean isPaymentAuthorized( MailAddressType clientMailAddress );


    /**
     * Checks out the shopping cart of a client
     *
     * @param clientMailAddress
     * @throws ShopException if ... 
     * @throws ShopException if ...
     *      - clientMailAddress is null
     *      - the client with the given mail address does not exist
     *      - the shopping cart is empty
     */
    public void checkout( MailAddressType clientMailAddress );




    /**
     * Returns a map showing which items have been ordered by a client, and how many of each item
     *
     * @param clientMailAddress
     * @return the order history of the client (map is empty if the client has not ordered anything yet)
     * @Deprecated Might be split into a dedicated OrderUseCases interface later (but still valid in this milestone)
     * @throws ShopException if
     *      - the mail address is null
     *      - the client with the given mail address does not exist
     */
    public Map<UUID, Integer> getOrderHistory( MailAddressType clientMailAddress );



    /**
     * Deletes all orders and shopping carts in the system
     * @Deprecated Might be split into two methods later (delete orders and delete shopping carts), with
     *             the order deletion moved to a dedicated OrderUseCases interface later
     *             (but still valid in this milestone)
     */
    public void deleteAllOrders();
}
