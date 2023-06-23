package thkoeln.archilab.ecommerce.domainprimitives;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MailAddress implements MailAddressType {
    private String mailAddress;

    @Override
    public String toString() {
        return mailAddress;
    }

    public static MailAddressType of(String mailAddressAsString) throws ShopException {
        if (mailAddressAsString == null) {
            throw new ShopException("Mail address is null");
        }
        int atIndex = mailAddressAsString.indexOf("@");
        if (atIndex == -1 || atIndex != mailAddressAsString.lastIndexOf("@")) {
            throw new ShopException("Invalid mail address");
        }
        String beforeAt = mailAddressAsString.substring(0, atIndex);
        String afterAt = mailAddressAsString.substring(atIndex + 1);
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([.-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+$");
        Matcher matcher = pattern.matcher(mailAddressAsString);
        if (beforeAt.trim().isEmpty() || afterAt.trim().isEmpty() ||
                 !matcher.matches()
                || beforeAt.contains(" ") || afterAt.contains(" ")) {
            throw new ShopException("Invalid mail address");
        }
        if (beforeAt.contains("..")||afterAt.contains("..")){
            throw new ShopException("Invalid mail address");
        }
        if (!(afterAt.endsWith(".de")||afterAt.endsWith(".at")||afterAt.endsWith(".ch")
                ||afterAt.endsWith(".com")||afterAt.endsWith(".org"))){
            throw new ShopException("Invalid mail address");
        }
        return new MailAddress(mailAddressAsString);


    }
    @Override
    public boolean equals(Object obj){
        if (this==obj){
            return true;
        }
        if (obj==null || getClass()!=obj.getClass()){
            return false;
        }
        MailAddress other = (MailAddress) obj;
        return mailAddress.equals(other.getMailAddress());
    }

}
