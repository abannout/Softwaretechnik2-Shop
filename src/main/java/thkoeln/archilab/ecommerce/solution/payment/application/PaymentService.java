package thkoeln.archilab.ecommerce.solution.payment.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.client.domain.Client;
import thkoeln.archilab.ecommerce.solution.client.domain.ClientRepository;
import thkoeln.archilab.ecommerce.solution.payment.domain.Payment;
import thkoeln.archilab.ecommerce.solution.payment.domain.PaymentRepository;
import thkoeln.archilab.ecommerce.usecases.PaymentUseCases;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.UUID;

@Service
public class PaymentService implements PaymentUseCases {


    private PaymentRepository paymentRepository;
    private ClientRepository clientRepository;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ClientRepository clientRepository) {
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public UUID authorizePayment(MailAddressType clientMailAddress, MoneyType amount) {
        if (clientMailAddress==null|| clientMailAddress.toString().isEmpty()){
            throw new ShopException("clientMail is empty or null");
        }
        if (amount==null||amount.getAmount() <= 0.00  ) {
            throw new ShopException("amount is negative or zero");
        }
        if (amount.getAmount() > 500.00) {
            throw new ShopException("the payment cannot be processed, because it is over the limit of 500.00 EUR");
        }
        var client = getClient(clientMailAddress.toString());
        var payment = new Payment(client,(Money)amount);
        paymentRepository.save(payment);
        return payment.getUuid();
    }

    @Override
    public MoneyType getPaymentTotal(MailAddressType clientMailAddress) {
        if (clientMailAddress==null|| clientMailAddress.toString().isEmpty()){
            throw new ShopException("clientMail is empty or null");
        }
        var client = getClient(clientMailAddress.toString());
        var total = 0f;
        var currency="";
        var clientPaymentList = paymentRepository.findAllByClientEmailMailAddressString(clientMailAddress.toString());
        for (Payment payment :clientPaymentList
                ) {
            total+=payment.getPayment().getAmount();
            currency=payment.getPayment().getCurrency();
        }
        return new Money(total,currency);
    }

    @Override
    public void deletePaymentHistory() {
        paymentRepository.deleteAll();
    }
    private Client getClient(String clientMail) {

        var client = clientRepository.findByEmailMailAddressString(clientMail);
        if (client.isEmpty()) {
            throw new ShopException("client does not exist");
        }
        return client.get(0);
    }
}
