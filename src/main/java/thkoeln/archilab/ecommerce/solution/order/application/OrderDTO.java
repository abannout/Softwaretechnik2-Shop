package thkoeln.archilab.ecommerce.solution.order.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {

    private UUID uuid;
    private List<OrderPartDTO> orderParts = new ArrayList<>();

}
