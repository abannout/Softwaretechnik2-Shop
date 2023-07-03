package thkoeln.archilab.ecommerce.solution.payment.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {
List<Payment> findAllByClientEmailMailAddressString(String clientMail);
}
