package thkoeln.archilab.ecommerce.masterdata;

import lombok.Setter;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;

import java.util.Objects;

@Setter
public class MockClient implements ClientType {
    private String name;
    private MailAddressType mailAddress;
    private HomeAddressType homeAddress;


    public MockClient( String name, MailAddressType mailAddress, HomeAddressType homeAddress) {
        this.name = name;
        this.mailAddress = mailAddress;
        this.homeAddress = homeAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MailAddressType getMailAddress() {
        return mailAddress;
    }

    @Override
    public HomeAddressType getHomeAddress() {
        return homeAddress;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof MockClient ) ) return false;
        MockClient that = (MockClient) o;
        return Objects.equals( getName(), that.getName() ) && Objects.equals( mailAddress, that.mailAddress ) && Objects.equals( homeAddress, that.homeAddress );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getName(), mailAddress, homeAddress );
    }
}
