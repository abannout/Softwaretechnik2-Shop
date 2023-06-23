package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.UUID;

/**
 * This interface contains methods needed in the context of use cases dealing with payments.
 */
public interface PaymentUseCases {

    /**
     * Authorizes a payment from a credit card for a given amount
     *
     * @param clientMailAddress
     * @param moneyAmount
     * @return the id of the payment, if successfully authorized
     * @throws ShopException if ...
     *      - clientMailAddress is null
     *      - the amount is null
     *      - the amount is 0.00 EUR
     *      - the payment cannot be processed, because it is over the limit of 500.00 EUR
     */
    public UUID authorizePayment( MailAddressType clientMailAddress, MoneyType moneyAmount );


    /**
     * Returns the total amount of payments (over the complete history) for a client
     * (identified by his/her mail address)
     *
     * @param clientMailAddress
     * @return the total amount of payments made using this credit card, or 0.00 EUR if there
     *         weren't any payments yet.
     * @throws ShopException if ...
     *      - clientMailAddress is null
     */
    public MoneyType getPaymentTotal( MailAddressType clientMailAddress );


    /**
     * Deletes all payment history, for all clients.
     */
    public void deletePaymentHistory();
}
