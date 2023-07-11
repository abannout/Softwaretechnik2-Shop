package thkoeln.archilab.ecommerce.e1e2resttests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static thkoeln.archilab.ecommerce.masterdata.FactoryMethodInvoker.instantiateMailAddress;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_DATA;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.CLIENT_EMAIL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class E1OrdertRESTTest {

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
    public void testGetNoOrders() throws Exception {
        // given
        // when
        // then
        mockMvc.perform( get( "/orders" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 0 ) );
        mockMvc.perform( get( "/orders?mailAddress=" + nonExistingMailAddress ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 0 ) );
        mockMvc.perform( get( "/orders?mailAddress=invalidEmailString" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 0 ) );
        mockMvc.perform( get( "/orders?mailAddress=" + CLIENT_EMAIL[3].toString() ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 0 ) );
    }


    @Test void testMakeThreeOrders() throws Exception {
        // given
        String mailAddressString = CLIENT_EMAIL[6].toString();

        UUID itemId2 = (UUID) ITEM_DATA[2][0];
        UUID itemId4 = (UUID) ITEM_DATA[4][0];
        UUID itemId6 = (UUID) ITEM_DATA[6][0];
        Float itemMoneyAmount2 = ((MoneyType) ITEM_DATA[2][5]).getAmount() * 2;
        Float itemMoneyAmount4 = ((MoneyType) ITEM_DATA[4][5]).getAmount() * 4;
        Float itemMoneyAmount6 = ((MoneyType) ITEM_DATA[6][5]).getAmount() * 6;

        String uriAllOrders = "/orders?mailAddress=" + mailAddressString;
        String uriLatestOrder = uriAllOrders + "&filter=latest";

        // when
        restHelper.makeOrderForClient( mailAddressString, itemId2, 2 );
        restHelper.makeOrderForClient( mailAddressString, itemId4, 4 );
        restHelper.makeOrderForClient( mailAddressString, itemId6, 6 );

        // then get all orders ...
        mockMvc.perform( get( uriAllOrders ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 3 ) )

                .andExpect( jsonPath( "$[0].mailAddress.mailAddressString" ).value( mailAddressString ) )
                .andExpect( jsonPath( "$[0].price.amount" ).value( itemMoneyAmount2 ) )

                .andExpect( jsonPath( "$[1].mailAddress.mailAddressString" ).value( mailAddressString ) )
                .andExpect( jsonPath( "$[1].price.amount" ).value( itemMoneyAmount4 ) )

                .andExpect( jsonPath( "$[2].mailAddress.mailAddressString" ).value( mailAddressString ) )
                .andExpect( jsonPath( "$[2].price.amount" ).value( itemMoneyAmount6 ) );

        // ... and the latest order
        mockMvc.perform( get( uriLatestOrder ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 1 ) )
                .andExpect( jsonPath( "$[0].price.amount" ).value( itemMoneyAmount6 ) );
    }


    @Test
    public void testIfMoneySumInOrderIsCorrect() throws Exception {
        // given
        String mailAddressString = CLIENT_EMAIL[3].toString();
        UUID itemId3 = (UUID) ITEM_DATA[3][0];
        UUID itemId4 = (UUID) ITEM_DATA[4][0];
        Integer quantity3 = 14;
        Integer quantity4 = 5;
        Float itemMoneyAmount =
                ((MoneyType) ITEM_DATA[3][5]).getAmount() * quantity3 +
                ((MoneyType) ITEM_DATA[4][5]).getAmount() * quantity4;

        // when
        UUID shoppingCartId = restHelper.getShoppingCartId( mailAddressString );
        restHelper.addItemToShoppingCart( shoppingCartId, itemId3, quantity3 );
        restHelper.addItemToShoppingCart( shoppingCartId, itemId4, quantity4 );
        mockMvc.perform( put( "/shoppingCarts/" + shoppingCartId.toString() + "/checkout" ) )
                .andExpect( status().isOk() );

        // then
        mockMvc.perform( get(
                "/orders?mailAddress=" + mailAddressString + "&filter=latest") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 1 ) )
                .andExpect( jsonPath( "$[0].price.amount" ).value( itemMoneyAmount ) );
    }



}
