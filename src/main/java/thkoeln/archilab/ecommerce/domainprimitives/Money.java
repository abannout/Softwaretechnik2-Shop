package thkoeln.archilab.ecommerce.domainprimitives;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)

@AllArgsConstructor
@NoArgsConstructor
public class Money implements MoneyType {

    private float moneyAmount;
    private String currency;


    @Override
    public Float getAmount() {
        return moneyAmount;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public MoneyType add(MoneyType otherMoney) {
        if (!this.currency.equals(otherMoney.getCurrency()) || otherMoney.getAmount() == null) {
            throw new ShopException("Given money is either null or has a different currency!");
        }
        return new Money(this.moneyAmount+otherMoney.getAmount(),this.currency);
    }

    @Override
    public MoneyType subtract(MoneyType otherMoney) {
        if (!Objects.equals(this.currency, otherMoney.getCurrency()) || otherMoney.getAmount() == null|| otherMoney.getAmount()>this.moneyAmount) {
            throw new ShopException("Given money is either null or has a different currency!");
        }
        return new Money(this.moneyAmount-otherMoney.getAmount(),this.currency);
    }

    @Override
    public MoneyType multiplyBy(int factor)throws ShopException {
        if (factor < 0){
            throw new ShopException("factor is smaller than 0");
        }
        return new Money(this.moneyAmount*factor,this.currency);
    }

    @Override
    public boolean largerThan(MoneyType otherMoney) {
        if (otherMoney.getAmount()==0 || !Objects.equals(otherMoney.getCurrency(), this.currency)){
            throw new ShopException("given money is null or different currency");
        }
        return this.moneyAmount > otherMoney.getAmount();
    }
    public static MoneyType of( Float amount, String currency ){
        if (amount ==null || amount < 0 || currency ==null || !currency.equals("EUR") && !currency.equals("CHF")){
            throw new ShopException("amount is null or currency is not valid");
        }
        return new Money(amount,currency);
    }
    @Override
    public boolean equals(Object obj){
        if (this==obj){
            return true;
        }
        if (obj==null || getClass()!=obj.getClass()){
            return false;
        }
        Money other = (Money) obj;
        return moneyAmount ==other.getAmount() && currency.equals(other.getCurrency());
    }

}
