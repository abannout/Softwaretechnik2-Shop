package thkoeln.archilab.ecommerce.usecases.domainprimitivetypes;

import thkoeln.archilab.ecommerce.ShopException;

public interface MailAddressType {
    /**
     * @return the mail address as a string
     */
    public String toString();

    /**
     * Unfortunately, Java interfaces cannot contain static methods. However, we expect the
     * implementing class to provide a static factory method (simply named "of(...)"),
     * which creates an mail address, given as a string.
     * We specify this factory method here as a comment, using the Javadoc documentation style.
     *
     * @param mailAddressAsString - the mail address as a string.
     *      We will use a much simplified validation method to check if the mail address is valid:
     *      - it must contain exactly one '@' character.
     *      - the part before the '@' and the part after the '@' must not be empty, contain of
     *        at least one of these characters (A..Z, a..z, or 0..9) and must not contain any whitespace characters
     *      - the parts before and after the '@' may contain one or several '.' as separators
     *      - two '.' characters must not be directly next to each other (so "test@this..example.com" is invalid)
     *      - the part after the '@' must end with ".de", ".at", ".ch", ".com", ".org"
     *        (for simplicity we do not allow any other domains)
     * @return a new MailAddressType object matching the given mail address
     * @throws ShopException if ...
     *      - mailAddressAsString is null
     *      - mailAddressAsString is not a valid mail address (see above)
     */
     // public static MailAddressType of( String mailAddressAsString );
}
