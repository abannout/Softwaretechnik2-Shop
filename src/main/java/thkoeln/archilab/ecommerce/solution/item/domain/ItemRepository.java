package thkoeln.archilab.ecommerce.solution.item.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, UUID> {

}
