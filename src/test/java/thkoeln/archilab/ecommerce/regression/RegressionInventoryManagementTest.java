package thkoeln.archilab.ecommerce.regression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator;
import thkoeln.archilab.ecommerce.usecases.InventoryManagementUseCases;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_INVENTORY;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.ITEM_DATA;

@SpringBootTest
@Transactional
public class RegressionInventoryManagementTest {

    @Autowired
    private InventoryManagementUseCases inventoryManagementUseCases;
    @Autowired
    private InitialMasterDataCreator initialMasterDataCreator;


    @BeforeEach
    public void setUp() {
        initialMasterDataCreator.deleteAll();
        initialMasterDataCreator.addAllItems();
    }



    @Test
    public void testAddToInventory() {
        // given
        initialMasterDataCreator.inventoryUpAllItems();
        UUID itemId8 = (UUID) ITEM_DATA[8][0];

        // when
        int inventory8before = inventoryManagementUseCases.getAvailableInventory( itemId8 );
        assertEquals( ITEM_INVENTORY.get( itemId8 ), inventory8before );
        inventoryManagementUseCases.addToInventory( itemId8, 22 );
        int inventory8after = inventoryManagementUseCases.getAvailableInventory( itemId8 );
        inventoryManagementUseCases.addToInventory( itemId8, 1 );
        int inventory8after2 = inventoryManagementUseCases.getAvailableInventory( itemId8 );

        // then
        assertEquals( inventory8before + 22, inventory8after );
        assertEquals( inventory8after + 1, inventory8after2 );
    }


    @Test
    public void testRemoveFromInventory() {
        // given
        initialMasterDataCreator.inventoryUpAllItems();
        UUID itemId6 = (UUID) ITEM_DATA[6][0];
        int inventory6before = ITEM_INVENTORY.get( itemId6 );
        UUID itemId9 = (UUID) ITEM_DATA[9][0];
        int inventory9before = ITEM_INVENTORY.get( itemId9 );
        UUID itemId1 = (UUID) ITEM_DATA[1][0];
        int inventory1before = ITEM_INVENTORY.get( itemId1 );

        // when
        inventoryManagementUseCases.removeFromInventory( itemId6, 1 );
        int inventory6after = inventoryManagementUseCases.getAvailableInventory( itemId6 );
        inventoryManagementUseCases.removeFromInventory( itemId1, 3 );
        int inventory1after = inventoryManagementUseCases.getAvailableInventory( itemId1 );
        inventoryManagementUseCases.removeFromInventory( itemId9, inventory9before );
        int inventory9after = inventoryManagementUseCases.getAvailableInventory( itemId9 );

        // then
        assertEquals( inventory6before - 1, inventory6after );
        assertEquals( inventory1before - 3, inventory1after );
        assertEquals( 0, inventory9after );
    }

}
