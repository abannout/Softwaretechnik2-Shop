package thkoeln.archilab.ecommerce.e1e2resttests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import thkoeln.archilab.ecommerce.e1e2resttests.testdtos.IdDTO;
import thkoeln.archilab.ecommerce.e1e2resttests.testdtos.QuantityAndCommentDTO;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_DATA;

public class RESTHelper {
    private MockMvc mockMvc;

    public static final ResultMatcher NOT_FOUND = status().isNotFound();
    public static final ResultMatcher OK = status().isOk();
    public static final ResultMatcher CREATED = status().isCreated();
    public static final ResultMatcher UNPROCESSABLE_ENTITY = status().isUnprocessableEntity();
    public static final ResultMatcher CONFLICT = status().isConflict();

    public RESTHelper( MockMvc mockMvc ) {
        this.mockMvc = mockMvc;
    }

    public void makeOrderForClient( String mailAddressString, UUID itemId, Integer quantity ) throws Exception {
        UUID shoppingCartId = getShoppingCartId( mailAddressString );
        addItemToShoppingCart( shoppingCartId, itemId, quantity );
        mockMvc.perform( put( "/shoppingCarts/" + shoppingCartId.toString() + "/checkout" ) )
                .andExpect( OK );
    }

    public UUID getShoppingCartId( String mailAddressString ) throws Exception {
        // get the shopping cart and extract its id ...
        MvcResult shoppingCart = mockMvc.perform( get( "/shoppingCarts?mailAddress=" + mailAddressString ) )
                .andExpect( OK )
                .andExpect( jsonPath( "$.id" ).isNotEmpty() )
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        IdDTO idDTO = objectMapper.readValue( shoppingCart.getResponse().getContentAsString(), IdDTO.class );
        return idDTO.getId();
    }

    public void addItemToShoppingCart( UUID shoppingCartId, UUID itemId, Integer quantity,
                                                 String comment, ResultMatcher expectedStatus )
                                                throws Exception {
        ResultMatcher status = expectedStatus == null ? CREATED : expectedStatus;
        ObjectMapper objectMapper = new ObjectMapper();
        QuantityAndCommentDTO dto = new QuantityAndCommentDTO( itemId, quantity, comment );
        String quantityJson = objectMapper.writeValueAsString( dto );
        mockMvc.perform( post( "/shoppingCarts/" + shoppingCartId + "/shoppingCartParts" )
                .contentType( APPLICATION_JSON ).content( quantityJson ) )
                .andExpect( status );
    }


    public void addItemToShoppingCart( UUID shoppingCartId, UUID itemId, Integer quantity )
            throws Exception {
        addItemToShoppingCart( shoppingCartId, itemId, quantity, null, null );
    }



    public void addMultipleItemsToShoppingCart(
            String clientEmailString, Integer[] itemIndices, Integer[] quantities, String[] comments,
            ResultMatcher[] expectedAddStatuses ) throws Exception {
        UUID shoppingCartId = getShoppingCartId( clientEmailString );
        String shoppingCartUri = "shoppingCarts?mailAddress=" + clientEmailString;
        String baseUri = "/shoppingCarts/" + shoppingCartId + "/shoppingCartParts/";

        // add all the items to the shopping cart ...
        Map<Integer, Integer> quantityMap = new HashMap<>();
        Map<Integer, String> commentMap = new HashMap<>();
        for ( int i = 0; i < itemIndices.length; i++ ) {
            UUID itemId = (UUID) ITEM_DATA[itemIndices[i]][0];
            addItemToShoppingCart( shoppingCartId, itemId, quantities[i], comments[i], expectedAddStatuses[i] );
            if ( expectedAddStatuses[i] != null || expectedAddStatuses[i] == CREATED || expectedAddStatuses[i] == OK ) {
                quantityMap.put( itemIndices[i],
                        quantities[i] + quantityMap.getOrDefault( itemIndices[i], 0 ) );
                commentMap.put( itemIndices[i], comments[i] );
            }
        }
    }


    public void addMultipleItemsToShoppingCart(
            String clientEmailString, Integer[] itemIndices, Integer[] quantities,
            String[] comments ) throws Exception {
        ResultMatcher[] expectedAddStatuses = new ResultMatcher[itemIndices.length];
        for ( int i = 0; i < itemIndices.length; i++ ) {
            expectedAddStatuses[i] = CREATED;
        }
        addMultipleItemsToShoppingCart( clientEmailString, itemIndices, quantities,
                comments, expectedAddStatuses );
    }


    public void checkMultipleShoppingCartParts( String clientEmailString,
                                                         Integer[] itemIndices, Integer[] expectedQuantities,
                                                         String[] expectedComments, Boolean[] expectedToBeFound ) throws Exception {
        UUID shoppingCartId = getShoppingCartId( clientEmailString );
        String shoppingCartUri = "/shoppingCarts?mailAddress=" + clientEmailString;
        String baseUri = "/shoppingCarts/" + shoppingCartId + "/shoppingCartParts/";

        // check that the shopping cart contains all the items ...
        int totalQuantity = 0;
        for ( int i = 0; i < itemIndices.length; i++ ) {
            if ( expectedQuantities[i] != null ) {
                totalQuantity += expectedQuantities[i];
            }
        }
        mockMvc.perform( get( shoppingCartUri ) )
                .andExpect( OK )
                .andExpect( jsonPath( "$.totalQuantity" ).value( totalQuantity ) );

        // check that each item has the correct quantity and comment ...
        for ( int i = 0; i < itemIndices.length; i++ ) {
            UUID itemId = (UUID) ITEM_DATA[itemIndices[i]][0];
            if ( expectedToBeFound[i] ) {
                mockMvc.perform( get( baseUri + itemId ) )
                        .andExpect( OK )
                        .andExpect( jsonPath( "$.quantity" ).value( expectedQuantities[i] ) )
                        .andExpect( jsonPath( "$.comment" ).value( expectedComments[i] ) );
            } else {
                mockMvc.perform( get( baseUri + itemId ) )
                        .andExpect( NOT_FOUND );
            }
        }
    }


    public void checkMultipleShoppingCartParts( String clientEmailString,
                                                         Integer[] itemIndices, Integer[] expectedQuantities,
                                                         String[] expectedComments ) throws Exception {
        Boolean[] expectedToBeFound = new Boolean[itemIndices.length];
        for ( int i = 0; i < itemIndices.length; i++ ) {
            expectedToBeFound[i] = true;
        }
        checkMultipleShoppingCartParts( clientEmailString, itemIndices, expectedQuantities,
                expectedComments, expectedToBeFound );
    }



    public void checkMultipleOrderPartsInLatestOrder(
            String clientEmailString, Integer[] itemIndices, Integer[] expectedQuantities,
            String[] expectedComments ) throws Exception {
        String latestOrderUri = "/orders?mailAddress=" + clientEmailString + "&filter=latest";

        // Calculate the total price of the order ...
        float totalPrice = 0f;
        for ( int i = 0; i < itemIndices.length; i++ ) {
            totalPrice += expectedQuantities[i] * ((MoneyType) ITEM_DATA[itemIndices[i]][5]).getAmount();
        }

        // ... and check that the order has the correct total price (and other data)
        ResultActions resultActions = mockMvc.perform( get( latestOrderUri ) ).andExpect( OK )
                .andExpect( jsonPath( "$.length()" ).value( 1 ) )
                .andExpect( jsonPath( "$[0].price.amount" ).value( totalPrice ) )
                .andExpect( jsonPath( "$[0].mailAddress.mailAddressString" ).value( clientEmailString ) );

        // Now check that all the expected parts are there
        for ( int i = 0; i < itemIndices.length; i++ ) {
            UUID itemId = (UUID) ITEM_DATA[itemIndices[i]][0];
            resultActions.andExpect(
                    jsonPath( "$[0].orderParts.length()" ).value( itemIndices.length ) );
            resultActions.andExpect(
                    jsonPath( "$[0].orderParts[?(@.itemId == '" + itemId + "')].quantity" )
                            .value( expectedQuantities[i] ) );
            if ( expectedComments[i] != null ) {
                resultActions.andExpect(
                        jsonPath( "$[0].orderParts[?(@.itemId == '" + itemId + "')].comment" )
                                .value( expectedComments[i] ) );
            }
        }
    }

}
