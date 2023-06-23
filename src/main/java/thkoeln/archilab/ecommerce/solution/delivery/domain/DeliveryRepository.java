package thkoeln.archilab.ecommerce.solution.delivery.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends CrudRepository<Delivery, UUID> {
    List<Delivery> getAllByClientEmailMailAddress(String email);
}
