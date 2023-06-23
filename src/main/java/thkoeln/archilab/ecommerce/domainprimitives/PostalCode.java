package thkoeln.archilab.ecommerce.domainprimitives;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.PostalCodeType;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PostalCode implements PostalCodeType {
   // private static PostalCode instance;
    private String postalCode;

    @Override
    public String toString() {
        return postalCode;
    }

    //Java Singleton Design Pattern
    public static PostalCodeType of(String postalCodeAsString) throws ShopException{
        if (postalCodeAsString == null || postalCodeAsString.length()!=5 || postalCodeAsString.endsWith("0000")){
            throw new ShopException("Invalid postal code!");
        }
        /*if (instance ==null){
            instance = new PostalCode(postalCodeAsString);
        }*/
        return new PostalCode(postalCodeAsString);
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj){
            return true;
        }
        if (obj==null || getClass()!=obj.getClass()){
            return false;
        }
        PostalCode other = (PostalCode) obj;
        return postalCode.equals(other.postalCode);
    }
}

