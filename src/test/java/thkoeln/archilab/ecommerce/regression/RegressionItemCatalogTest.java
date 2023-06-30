package thkoeln.archilab.ecommerce.regression;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator;
import thkoeln.archilab.ecommerce.usecases.ShoppingCartUseCases;
import thkoeln.archilab.ecommerce.usecases.InventoryManagementUseCases;
import thkoeln.archilab.ecommerce.usecases.ItemCatalogUseCases;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.InvalidReason.*;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_DATA;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.CLIENT_EMAIL;

@SpringBootTest
@Transactional
public class RegressionItemCatalogTest {

    @Autowired
    private ShoppingCartUseCases shoppingCartUseCases;
    @Autowired
    private ItemCatalogUseCases itemCatalogUseCases;
    @Autowired
    private InventoryManagementUseCases inventoryManagementUseCases;
    @Autowired
    private InitialMasterDataCreator initialMasterDataCreator;

    @BeforeEach
    public void setUp() {
        shoppingCartUseCases.deleteAllOrders();
        itemCatalogUseCases.deleteItemCatalog();
    }



    @Test
    public void testAddItemToCatalog() {
        // given
        initialMasterDataCreator.addAllItems();

        // when
        MoneyType sellPrice = itemCatalogUseCases.getSellPrice( (UUID) ITEM_DATA[4][0] );

        // then
        assertEquals( ITEM_DATA[4][5], sellPrice );
    }

    @Test
    public void testRemoveItemFromCatalog() {
        // given
        initialMasterDataCreator.addAllItems();
        UUID itemId = (UUID) ITEM_DATA[4][0];

        // when
        assertDoesNotThrow( () -> itemCatalogUseCases.getSellPrice( itemId ) );
        itemCatalogUseCases.removeItemFromCatalog( itemId );

        // then
        assertThrows( ShopException.class, () -> itemCatalogUseCases.getSellPrice( itemId ) );
    }




    @Test
    public void testRemoveItemThatIsInInventory() {
        // given
        initialMasterDataCreator.addAllItems();
        UUID itemId = (UUID) ITEM_DATA[4][0];
        inventoryManagementUseCases.addToInventory( itemId, 3 );

        // when
        // then
        assertThrows( ShopException.class, () -> itemCatalogUseCases.removeItemFromCatalog( itemId ) );
    }


    @Test
    public void testClearItemCatalog() {
        // given
        initialMasterDataCreator.addAllItems();

        // when
        itemCatalogUseCases.deleteItemCatalog();

        // then
        assertThrows( ShopException.class, () -> itemCatalogUseCases.getSellPrice( (UUID) ITEM_DATA[4][0] ) );
    }

    @AfterEach
    public void tearDown() {
        initialMasterDataCreator.deleteAll();
    }

}
