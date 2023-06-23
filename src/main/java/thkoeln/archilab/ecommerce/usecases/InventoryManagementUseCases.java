package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;

import java.util.UUID;


/**
 * This interface contains methods needed in the context of use cases dealing with managing the shop inventory,
 * i.e. adding and removing items in the warehouse. The interface is probably incomplete, and
 * will grow over time.
 */
public interface InventoryManagementUseCases {
    /**
     * Adds a certain quantity of a given item to the inventory
     * @param itemId
     * @param addedQuantity
     * @throws ShopException if ...
     *      - itemId is null
     *      - the item with that id does not exist
     *      - addedQuantity <= 0
     */
    public void addToInventory( UUID itemId, int addedQuantity );


    /**
     * Removes a certain quantity of a given item from the inventory.
     * If the new total quantity is lower than the currently reserved items, some of currently reserved items
     * (in the clients' shopping baskets) are removed. This means that some of the reserved items are lost for
     * the client. (This is necessary because there probably was a mistake in the inventory management, a mis-counting,
     * or some of the items were stolen from the warehouse, are broken, etc.)
     * @param itemId
     * @param removedQuantity
     * @throws ShopException if ...
     *      - itemId is null
     *      - the item with that id does not exist
     *      - removedQuantity <= 0
     *      - the removed quantity is greater than the current inventory and the currently reserved items together
     */
    public void removeFromInventory( UUID itemId, int removedQuantity );


    /**
     * Changes the total quantity of a given item in the inventory.
     * If the new total quantity is lower than the currently reserved items, some of currently reserved items
     * (in the clients' shopping baskets) are removed. This means that some of the reserved items are lost for
     * the client. (This is necessary because there probably was a mistake in the inventory management, a mis-counting,
     * or some of the items were stolen from the warehouse, are broken, etc.)
     * @param itemId
     * @param newTotalQuantity
     * @throws ShopException if ...
     *      - itemId is null
     *      - the item with that id does not exist
     *      - newTotalQuantity < 0
     */
    public void changeInventoryTo( UUID itemId, int newTotalQuantity );


    /**
     * Get the current total inventory of a given item, including the currently reserved items
     * @param itemId
     * @return the current total inventory of the item
     * @throws ShopException if ...
     *      - itemId is null
     *      - the item with that id does not exist
     */
    public int getAvailableInventory( UUID itemId );
}
