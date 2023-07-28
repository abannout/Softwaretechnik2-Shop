package thkoeln.archilab.ecommerce.solution.shoppingcart.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import thkoeln.archilab.ecommerce.ShopException;

import java.net.URI;
import java.util.UUID;

@RestController
public class ShoppingCartRestController {

    private final CartControllerService cartControllerService;
    private final ShoppingCartService shoppingCartService;
    @Autowired
    public ShoppingCartRestController(CartControllerService shoppingCartService, ShoppingCartService shoppingCartService1) {
        this.cartControllerService = shoppingCartService;
        this.shoppingCartService = shoppingCartService1;
    }

    @GetMapping("/shoppingCarts")
    public ResponseEntity<ShoppingCartDTO> getCartByMail(@RequestParam(value = "mailAddress", required = false) String mailAddress) {
        try {
            var shoppingCart = cartControllerService.getCartDtoByClientMail(mailAddress);


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
            var carPart = cartControllerService.getCartPartByIdAndItemId(shoppingCartId, itemId);
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


            var carPart = cartControllerService.getCartPartByIdAndItemId(shoppingCartId, itemId);
            if (shoppingCartId == null || itemId == null || carPart == null) {
                return ResponseEntity.notFound().build();
            }
            cartControllerService.deletePartFromCart(shoppingCartId, itemId);

                return ResponseEntity.ok("easy");

        } catch (ShopException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/shoppingCarts/{shoppingCartId}/shoppingCartParts")
    public ResponseEntity<ShoppingCartDTO> addOrRemoveCartItem(@PathVariable("shoppingCartId") UUID shoppingCartId,@RequestBody ShoppingCartPartDTO shoppingCartPartDTO){
        try {
            ShoppingCartDTO shoppingCartDTO = cartControllerService.createCartPartFromDTO(shoppingCartId,shoppingCartPartDTO);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shoppingCartDTO.getId()).toUri();
                return ResponseEntity.created(uri).body(shoppingCartDTO);
        }catch (ShopException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }
    }

    @PutMapping("/shoppingCarts/{shoppingCartId}/checkout")
    public ResponseEntity<String> checkoutShoppingCart(@PathVariable("shoppingCartId") UUID shoppingCartId){
       try {
           var cart = cartControllerService.getCartById(shoppingCartId);
            if (cart ==null){
                return ResponseEntity.notFound().build();
            }
           shoppingCartService.checkout(cart.getAbstractClient().mail());
           return ResponseEntity.ok("Checkout successful");
       }catch (ShopException e){
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
       }

    }



}
