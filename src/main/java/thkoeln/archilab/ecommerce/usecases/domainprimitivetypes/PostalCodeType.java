package thkoeln.archilab.ecommerce.usecases.domainprimitivetypes;

import thkoeln.archilab.ecommerce.ShopException;

public interface PostalCodeType {
    /**
     * @return the postal code as a string
     */
    public String toString();

    /**
     * Unfortunately, Java interfaces cannot contain static methods. However, we expect the
     * implementing class to provide a static factory method (simply named "of(...)"),
     * which creates an postal code, given as a string.
     * We specify this factory method here as a comment, using the Javadoc documentation style.
     *
     * @param postalCodeAsString - the postal code as a string.
     *      We will use a much simplified validation method to check if the postal code is valid:
     *      - It must contain exactly 5 digits.
     *      - The last 4 digits must not be 0000 (i.e. 20000 is not a valid postal code, but 20001 is valid)
     * @return a new postal code object matching the given string
     * @throws ShopException if ...
     *      - postalCodeAsString is null
     *      - postalCodeAsString is not a valid postal code (see above)
     */
     // public static PostalCodeType of( String postalCodeAsString );
}
