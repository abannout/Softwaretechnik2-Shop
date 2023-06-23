package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.UUID;


/**
 * This interface contains methods needed in the context of use cases dealing with managing the item catalog.
 * The interface is probably incomplete, and will grow over time.
 */

public interface ItemCatalogUseCases {
    /**
     * Adds a new item to the shop catalog
     * @param itemId
     * @param name
     * @param description
     * @param size
     * @param purchasePrice
     * @param sellPrice
     * @throws ShopException if ...
     *      - itemId is null,
     *      - the item with that id already exists,
     *      - name or description are null or empty,
     *      - the size is <= 0 (but can be null!),
     *      - the purchase price is null,
     *      - the sell price is null,
     *      - the sell price is lower than the purchase price
     */
    public void addItemToCatalog( UUID itemId, String name, String description, Float size,
                                           MoneyType purchasePrice, MoneyType sellPrice );


    /**
     * Removes a item from the shop catalog
     * @param itemId
     * @throws ShopException if
     *      - itemId is null,
     *      - the item with that id does not exist
     *      - the item is still in inventory
     *      - the item is still reserved in a shopping cart, or part of a completed order
     */
    public void removeItemFromCatalog( UUID itemId );


    /**
     * Get the sell price of a given item
     * @param itemId
     * @return the sell price
     * @throws ShopException if ...
     *      - itemId is null,
     *      - the item with that id does not exist
     */
    public MoneyType getSellPrice( UUID itemId );


    /**
     * Clears the item catalog, i.e. removes all items from the catalog, including all the inventory,
     * all the reservations and all the orders.
     */
    public void deleteItemCatalog();

}
