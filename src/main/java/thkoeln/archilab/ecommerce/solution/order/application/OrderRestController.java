package thkoeln.archilab.ecommerce.solution.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thkoeln.archilab.ecommerce.domainprimitives.MailAddress;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPart;

@RestController
public class OrderRestController {

  private final OrderService orderService;

  @Autowired
  public OrderRestController(OrderService orderService) {
    this.orderService = orderService;
  }


  @GetMapping("/orders")
  public ResponseEntity<List<OrderDTO>> getOrdersByMail(
      @RequestParam(value = "mailAddress", required = false) String mailAddress,
      @RequestParam(value = "filter", required = false) String filter
  ) {
    List<OrderDTO> orderDTOS = new ArrayList<>();

    if (Objects.equals(filter, "latest")) {
      var order = orderService.getLatestOrder(mailAddress);
      if (order != null) {
        orderDTOS.addAll(createOrderDTOList(order, mailAddress));
      }
    } else if (!StringUtils.isEmpty(mailAddress)) {
      List<Order> orders = orderService.getOrders(mailAddress);
      for (Order order : orders) {
        orderDTOS.addAll(createOrderDTOList(order, mailAddress));
      }
    }

    return ResponseEntity.ok(orderDTOS);
  }

  private List<OrderDTO> createOrderDTOList(Order order, String mailAddress) {
    List<OrderPartDTO> orderPartDTOS = new ArrayList<>();
    var totalPrice = 0.0f;
    var currency = "";

    for (OrderPart orderPart : order.orderPartsList()) {
      orderPartDTOS.add(new OrderPartDTO(
          orderPart.getItem().getUuid(),
          orderPart.getQuantity(),
          orderPart.getComment()
      ));
      totalPrice += orderPart.getItem().getSellPrice().getAmount() * orderPart.getQuantity();
      currency = orderPart.getItem().getSellPrice().getCurrency();
    }

    var mail = new MailAddress(mailAddress);
    List<OrderDTO> orderDTOS = new ArrayList<>();
    orderDTOS.add(
        new OrderDTO(order.getUuid(), mail, new Money(totalPrice, currency), orderPartDTOS));
    return orderDTOS;
  }

}
