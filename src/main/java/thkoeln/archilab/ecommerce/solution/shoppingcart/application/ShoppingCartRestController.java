package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import thkoeln.archilab.ecommerce.ShopException;

import java.util.UUID;

@RestController
public class ShoppingCartRestController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartRestController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/shoppingCarts")
    public ResponseEntity<ShoppingCartDTO> getCartByMail(@RequestParam(value = "mailAddress", required = false) String mailAddress) {
        try {
            var shoppingCart = shoppingCartService.getCartDtoByClientMail(mailAddress);


            if (StringUtils.isEmpty(mailAddress)) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(shoppingCart);
            }
        } catch (ShopException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/shoppingCarts/{shoppingCartId}/shoppingCartParts/{itemId}")
    public ResponseEntity<ShoppingCartPartDTO> getShoppingCartPart(@PathVariable("shoppingCartId") UUID shoppingCartId, @PathVariable("itemId") UUID itemId) {
        try {
            var carPart = shoppingCartService.getCartPartByIdAndItemId(shoppingCartId, itemId);
            if (shoppingCartId == null || itemId == null || carPart == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(carPart);
        } catch (ShopException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/shoppingCarts/{shoppingCartId}/shoppingCartParts/{itemId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteShoppingCartPart(@PathVariable("shoppingCartId") UUID shoppingCartId, @PathVariable("itemId") UUID itemId) {
        try {


            var carPart = shoppingCartService.getCartPartByIdAndItemId(shoppingCartId, itemId);
            if (shoppingCartId == null || itemId == null || carPart == null) {
                return ResponseEntity.notFound().build();
            }
            boolean removed = shoppingCartService.deletePartFromCart(shoppingCartId, itemId);
            if (removed) {
                return ResponseEntity.ok("easy");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ShopException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
