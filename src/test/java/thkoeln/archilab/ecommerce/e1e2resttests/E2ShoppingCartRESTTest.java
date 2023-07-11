package thkoeln.archilab.ecommerce.e1e2resttests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static thkoeln.archilab.ecommerce.e1e2resttests.RESTHelper.CONFLICT;
import static thkoeln.archilab.ecommerce.e1e2resttests.RESTHelper.CREATED;
import static thkoeln.archilab.ecommerce.masterdata.FactoryMethodInvoker.instantiateMailAddress;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_DATA;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.CLIENT_EMAIL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class E2ShoppingCartRESTTest {

    @Autowired
    private InitialMasterDataCreator initialMasterDataCreator;
    @Autowired
    private MockMvc mockMvc;

    private RESTHelper restHelper;
    private MailAddressType nonExistingMailAddress;


    @BeforeEach
    public void setUp() {
        initialMasterDataCreator.deleteAll();

        initialMasterDataCreator.addAllItems();
        initialMasterDataCreator.inventoryUpAllItems();
        initialMasterDataCreator.registerAllClients();

        restHelper = new RESTHelper( mockMvc );
        nonExistingMailAddress = instantiateMailAddress( "harry@sally.de" );
    }


    @Test
    public void testEmptyShoppingCart() throws Exception {
        // given
        String validUri = "/shoppingCarts?mailAddress=" + CLIENT_EMAIL[4].toString();
        String invalidUri1 = "/shoppingCarts?mailAddress=" + nonExistingMailAddress.toString();
        String invalidUri2 = "/shoppingCarts?mailAddress=justaninvalidemailaddress";

        // when
        // then
        mockMvc.perform( get( validUri ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.totalQuantity" ).value( 0 ) );
        mockMvc.perform( get( invalidUri1 ) )
                .andExpect( status().isNotFound() );
        mockMvc.perform( get( invalidUri2 ) )
                .andExpect( status().isNotFound() );
    }


    @Test
    public void testInvalidUris() throws Exception {
        // given
        UUID itemId = (UUID) ITEM_DATA[3][0];
        String clientEmailString = CLIENT_EMAIL[4].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );

        String invalidPostUri = "/shoppingCarts/" + UUID.randomUUID() + "/shoppingCartParts";
        String invalidPatchGetDeleteUri1 =
                "/shoppingCarts/" + shoppingCartId + "/shoppingCartParts/" + UUID.randomUUID();
        String invalidPatchGetDeleteUri2 =
                "/shoppingCarts/" + UUID.randomUUID().toString() + "/shoppingCartParts/" + itemId;
        String invalidCheckoutUri1 = "/shoppingCarts/" + shoppingCartId + "/chekkout";
        String invalidCheckoutUri2 = "/shoppingCarts/" + UUID.randomUUID() + "/checkout";

        // when
        // then
        mockMvc.perform( post( invalidPostUri )
                .contentType( APPLICATION_JSON ).content( "[]" ) ).andExpect( status().is4xxClientError() );

        mockMvc.perform( delete( invalidPatchGetDeleteUri1 ) ).andExpect( status().isNotFound() );
        mockMvc.perform( get( invalidPatchGetDeleteUri1 ) ).andExpect( status().isNotFound() );

        mockMvc.perform( delete( invalidPatchGetDeleteUri2 ) ).andExpect( status().isNotFound() );
        mockMvc.perform( get( invalidPatchGetDeleteUri2 ) ).andExpect( status().isNotFound() );

        mockMvc.perform( put( invalidCheckoutUri1 ) ).andExpect( status().isNotFound() );
        mockMvc.perform( put( invalidCheckoutUri2 ) ).andExpect( status().isNotFound() );
    }


    @Test
    public void testQueryNonExistingItem() throws Exception {
        // given
        UUID itemId = (UUID) ITEM_DATA[3][0];
        String clientEmailString = CLIENT_EMAIL[7].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String invalidUri = "/shoppingCarts/" + shoppingCartId + "/shoppingCartParts/" + itemId;

        // when
        // then
        mockMvc.perform( get( invalidUri ) ).andExpect( status().isNotFound() );
    }


    @Test
    public void testDeleteFromShoppingCart() throws Exception {
        // given
        UUID itemId1 = (UUID) ITEM_DATA[1][0];
        String clientEmailString9 = CLIENT_EMAIL[9].toString();
        String clientEmailString7 = CLIENT_EMAIL[7].toString();

        UUID shoppingCartId9 = restHelper.getShoppingCartId( clientEmailString9 );
        UUID shoppingCartId7 = restHelper.getShoppingCartId( clientEmailString7 );

        String baseUri9 = "/shoppingCarts/" + shoppingCartId9 + "/shoppingCartParts/";
        String checkoutUri9 = "/shoppingCarts/" + shoppingCartId9 + "/checkout";
        String checkoutUri7 = "/shoppingCarts/" + shoppingCartId7 + "/checkout";

        // when
        restHelper.addMultipleItemsToShoppingCart( clientEmailString9,
                new Integer[]{1, 4, 6}, new Integer[]{1, 4, 6}, new String[]{"c1", "c4", "c6"} );
        restHelper.checkMultipleShoppingCartParts(
                clientEmailString9, new Integer[]{1, 4, 6}, new Integer[]{1, 4, 6},
                new String[]{"c1", "c4", "c6"} );
        mockMvc.perform( delete( baseUri9 + itemId1 ) )
                .andExpect( status().isOk() );
        restHelper.checkMultipleShoppingCartParts(
                clientEmailString9, new Integer[]{1, 4, 6}, new Integer[]{null, 4, 6},
                new String[]{null, "c4", "c6"}, new Boolean[]{false, true, true} );
        // ... and another customer can still buy 10 of the "Nr. 1" item
        restHelper.addItemToShoppingCart( shoppingCartId7, itemId1, 10,
                "still10", null );

        // then
        mockMvc.perform( put( checkoutUri9 ) ).andExpect( status().isOk() );
        restHelper.checkMultipleOrderPartsInLatestOrder( clientEmailString9,
                new Integer[]{4, 6}, new Integer[]{4, 6}, new String[]{"c4", "c6"} );
        mockMvc.perform( put( checkoutUri7 ) ).andExpect( status().isOk() );
        restHelper.checkMultipleOrderPartsInLatestOrder( clientEmailString7,
                new Integer[]{1}, new Integer[]{10}, new String[]{"still10"} );
    }


    @Test
    public void testAddToAndRemoveFromShoppingCart() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[2].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String checkoutUri = "/shoppingCarts/" + shoppingCartId + "/checkout";

        // when
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{2, 4, 2, 4, 6, 2}, new Integer[]{2, 3, 4, -1, 3, -6},
                new String[]{null, "c4a", "c2a", "c4b", "c6", null} );
        restHelper.checkMultipleShoppingCartParts(
                clientEmailString, new Integer[]{2, 4, 6}, new Integer[]{null, 2, 3},
                new String[]{null, "c4b", "c6"}, new Boolean[]{false, true, true} );

        // then
        mockMvc.perform( put( checkoutUri ) ).andExpect( status().isOk() );
        restHelper.checkMultipleOrderPartsInLatestOrder( clientEmailString,
                new Integer[]{4, 6}, new Integer[]{2, 3}, new String[]{"c4b", "c6"} );
    }


    @Test
    public void testRemoveMoreThanThereIsInShoppingCart() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[2].toString();

        // when
        // then
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{2, 4, 2}, new Integer[]{2, 3, -3}, new String[]{null, null, null},
                new ResultMatcher[]{CREATED, CREATED, CONFLICT} );
    }


    @Test
    public void testAddMoreThanIsInInventory() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[7].toString();

        // when
        // then
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{1, 1, 1, 1}, new Integer[]{2, 3, 4, 2}, new String[]{null, null, null, null},
                new ResultMatcher[]{CREATED, CREATED, CREATED, CONFLICT} );
    }


    @Test
    public void testSuccessfulCheckoutJustWithinPaymentLimit() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[5].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String checkoutUri = "/shoppingCarts/" + shoppingCartId + "/checkout";

        // when
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{11, 13, 10, 6}, new Integer[]{1, 1, 1, 17}, new String[]{"350€", "100€", "35€", "8.50€"} );

        // then
        mockMvc.perform( put( checkoutUri ) ).andExpect( status().isOk() );
        restHelper.checkMultipleOrderPartsInLatestOrder( clientEmailString,
                new Integer[]{11, 13, 10, 6}, new Integer[]{1, 1, 1, 17}, new String[]{"350€", "100€", "35€", "8.50€"} );
    }



    @Test
    public void testUnsuccessfulCheckoutAbovePaymentLimit() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[1].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String checkoutUri = "/shoppingCarts/" + shoppingCartId + "/checkout";

        // when
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{13, 10}, new Integer[]{4, 3}, new String[]{"400€", "105€"} );

        // then
        mockMvc.perform( put( checkoutUri ) ).andExpect( status().isConflict() );
    }


    @Test
    public void testUnsuccessfulCheckoutAboveDeliveryLimit() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[8].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String checkoutUri = "/shoppingCarts/" + shoppingCartId + "/checkout";

        // when
        restHelper.addMultipleItemsToShoppingCart( clientEmailString,
                new Integer[]{13, 6}, new Integer[]{3, 18}, new String[]{"300€", "9€"} );

        // then
        mockMvc.perform( put( checkoutUri ) ).andExpect( status().isConflict() );
    }


    @Test
    public void testNoCheckoutForEmptyShoppingCart() throws Exception {
        // given
        String clientEmailString = CLIENT_EMAIL[9].toString();
        UUID shoppingCartId = restHelper.getShoppingCartId( clientEmailString );
        String checkoutUri = "/shoppingCarts/" + shoppingCartId + "/checkout";

        // when
        // then
        mockMvc.perform( put( checkoutUri ) ).andExpect( status().isConflict() );
    }
}
