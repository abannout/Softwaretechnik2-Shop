package thkoeln.archilab.ecommerce.usecases;

import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;

/**
 * This interface expresses the essence of a shop client
 */
public interface ClientType {
    public String getName();
    public MailAddressType getMailAddress();
    public HomeAddressType getHomeAddress();
}
