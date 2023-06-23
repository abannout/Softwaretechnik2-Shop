package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;

/**
 * This interface contains methods needed in the context of use cases dealing with registering a client.
 * The interface is probably incomplete, and will grow over time.
 */
public interface ClientRegistrationUseCases {
    /**
     * Registers a new client
     *
     * @param name
     * @param mailAddress
     * @param homeAddress
     * @throws ShopException if ...
     *      - name is null or empty
     *      - mailAddress is null
     *      - client with the given mail address already exists
     *      - homeAddress is null
     */
    public void register( String name, MailAddressType mailAddress, HomeAddressType homeAddress );


    /**
     * Changes the homeAddress of a client
     *
     * @param clientMailAddress
     * @param homeAddress
     * @throws ShopException if ...
     *      - mailAddress is null
     *      - client with the given mail address already exists
     *      - homeAddress is null
     */
    public void changeAddress( MailAddressType clientMailAddress, HomeAddressType homeAddress );


    /**
     * Returns the data of a client
     * @param clientMailAddress
     * @return the client data
     * @throws ShopException if ...
     *      - mailAddress is null
     *      - the client with the given mail address does not exist
     */
    public ClientType getClientData( MailAddressType clientMailAddress );



    /**
     * Deletes all clients, including all orders and shopping carts
     */
    public void deleteAllClients();
}
