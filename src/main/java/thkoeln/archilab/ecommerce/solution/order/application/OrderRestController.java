package thkoeln.archilab.ecommerce.solution.order.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPart;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

@RestController
public class OrderRestController {
    private OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByMail(@RequestParam(value = "mailaddress", required = false) String mailAddress,@RequestParam(value = "filter",defaultValue = "latest",required = false) String filter) {

        List<OrderDTO> orderDTOS = new ArrayList<>();
        if (Objects.equals(filter, "latest")){
           var order = orderService.getLatestOrder(mailAddress);
            List<OrderPartDTO> orderpartsDTO = new ArrayList<>();
            if (order ==null){return ResponseEntity.ok(orderDTOS);}
            for (OrderPart orderPart : order.orderPartsList()
            ) {
                orderpartsDTO.add(new OrderPartDTO(orderPart.getItem().getUuid(), orderPart.getQuantity(), orderPart.getComment()));
            }
            orderDTOS.add(new OrderDTO(order.getUuid(), orderpartsDTO));
            return ResponseEntity.ok(orderDTOS);
        }

        if (StringUtils.isEmpty(mailAddress)) {
            return ResponseEntity.ok(orderDTOS);
        }
        List<Order> orders = orderService.getOrders(mailAddress);
        if (!orders.isEmpty()) {
            for (Order order : orders
            ) {
                List<OrderPartDTO> orderpartsDTO = new ArrayList<>();
                for (OrderPart orderPart : order.orderPartsList()
                ) {
                    orderpartsDTO.add(new OrderPartDTO(orderPart.getItem().getUuid(), orderPart.getQuantity(), orderPart.getComment()));
                }
                orderDTOS.add(new OrderDTO(order.getUuid(), orderpartsDTO));
            }
        }
        return ResponseEntity.ok().body(orderDTOS);
    }
}
