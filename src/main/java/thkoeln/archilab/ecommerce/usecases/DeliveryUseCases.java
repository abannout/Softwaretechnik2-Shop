package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of use cases dealing with logistics,
 * i.e. the delivery of items to a client. It is probably incomplete, and will grow over time.
 */
public interface DeliveryUseCases {
    /**
     * Delivers a item to a client. The item is identified by its id, and the client by
     * his/her name, street, city and postal code.
     * @param deliveryRecipient
     * @param deliveryContent - a map of item ids and quantities
     * @return the id of the delivery, if successfully triggered
     * @throws ShopException if ...
     *      - deliveryRecipient is null
     *      - any of the properties in deliveryRecipient (the getXxx(...) methods) return null or empty strings
     *      - deliveryContent is null or empty
     *      - the total number of items in the delivery is > 20
     */
    public UUID triggerDelivery( ClientType deliveryRecipient, Map<UUID, Integer> deliveryContent );


    /**
     * Returns a map showing which items have been delivered to a client, and how many of each item
     *
     * @param clientMailAddress
     * @return the delivery history of the client (map is empty if the client has not had any deliveries yet)
     * @throws ShopException if
     *      - mailAddress is null
     *      - the client with the given mail address does not exist
     */
    public Map<UUID, Integer> getDeliveryHistory( MailAddressType clientMailAddress );



    /**
     *  Deletes all delivery history.
     */
    public void deleteDeliveryHistory();
}
