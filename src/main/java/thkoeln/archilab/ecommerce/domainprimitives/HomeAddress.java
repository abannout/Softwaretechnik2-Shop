package thkoeln.archilab.ecommerce.domainprimitives;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.PostalCodeType;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class HomeAddress implements HomeAddressType {
    private String street;
    private String city;
    @Embedded
    private PostalCode postalCode;

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public PostalCodeType getPostalCode() {
        return postalCode;
    }
    public static HomeAddressType of(String street, String city, PostalCodeType postalCode ) throws ShopException {
        if (street == null || street.length()==0||city == null || city.length()==0 || postalCode==null||postalCode.toString().length()==0){
            throw new ShopException("the address is not valid");
        }
        return new HomeAddress(street,city, (PostalCode) postalCode);

    }
    @Override
    public boolean equals(Object obj){
        if (this==obj){
            return true;
        }
        if (obj==null || getClass()!=obj.getClass()){
            return false;
        }
        HomeAddress other = (HomeAddress) obj;
        return street.equals(other.street) && city.equals(other.city)&&postalCode.equals(other.postalCode);
    }
}
