package thkoeln.archilab.ecommerce.solution.client.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ClientRepository extends CrudRepository <Client, String> {
     boolean existsClientByEmailMailAddressString(String mail);
     List<Client> findByEmailMailAddressString(String mail);
}
