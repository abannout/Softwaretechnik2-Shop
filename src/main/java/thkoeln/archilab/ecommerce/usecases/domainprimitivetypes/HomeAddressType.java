package thkoeln.archilab.ecommerce.usecases.domainprimitivetypes;

import thkoeln.archilab.ecommerce.ShopException;

public interface HomeAddressType {
    /**
     * @return the street as a string
     */
    public String getStreet();

    /**
     * @return the city as a string
     */
    public String getCity();

    /**
     * @return the postal code
     */
    public PostalCodeType getPostalCode();

    /**
     * Unfortunately, Java interfaces cannot contain static methods. However, we expect the
     * implementing class to provide a static factory method (simply named "of(...)"),
     * which creates an postal code, given as a string.
     * We specify this factory method here as a comment, using the Javadoc documentation style.
     *
     * @param street the street as a string
     * @param city the city as a string
     * @param postalCode the postal code
     * @return the homeAddress object matching the parameters
     * @throws ShopException if ...
     *      - street is null or empty
     *      - city is null or empty
     *      - postalCode is null
     */
     // public static HomeAddressType of( String street, String city, PostalCodeType postalCode );
}
