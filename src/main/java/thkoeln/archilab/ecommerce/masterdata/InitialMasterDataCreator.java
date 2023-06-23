package thkoeln.archilab.ecommerce.masterdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import thkoeln.archilab.ecommerce.usecases.InventoryManagementUseCases;
import thkoeln.archilab.ecommerce.usecases.ItemCatalogUseCases;
import thkoeln.archilab.ecommerce.usecases.ClientRegistrationUseCases;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.HomeAddressType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


/**
 * This class is used to create the initial master data for the application. This includes
 * clients and items. It is executed on application startup, and is used for testing purposes.
 */
@Component
@SuppressWarnings("PMD")
public class InitialMasterDataCreator {

    public final static String[] CLIENT_NAME = new String[]{
            "Max Müller",
            "Sophie Schmitz",
            "Irene Mihalic",
            "Emilia Fischer",
            "Filiz Polat",
            "Lina Wagner",
            "Leon Becker",
            "Agnieszka Kalterer",
            "Felix Bauer",
            "Lara Schulz"
    };
    public final static MailAddressType[] CLIENT_EMAIL = new MailAddressType[] {
            FactoryMethodInvoker.instantiateMailAddress( "max.mueller@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "sophie.schmitz@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "irene@wearefreedomnow.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "emilia.fischer@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "j877d3@gmail.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "lina.wagner@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "leon.becker@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "agna@here.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "felix.bauer@example.com" ),
            FactoryMethodInvoker.instantiateMailAddress( "lara.schulz@example.com" )
    };

    public final static HomeAddressType[] CLIENT_ADDRESS = new HomeAddressType[] {
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Hauptstraße 1", "Berlin",
                    FactoryMethodInvoker.instantiatePostalCode( "10115" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Bahnhofstraße 12", "München",
                    FactoryMethodInvoker.instantiatePostalCode( "80331" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Kirchplatz 3", "Hamburg",
                    FactoryMethodInvoker.instantiatePostalCode( "20095" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Goethestraße 7", "Frankfurt am Main",
                    FactoryMethodInvoker.instantiatePostalCode( "60311" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Mühlenweg 15", "Köln",
                    FactoryMethodInvoker.instantiatePostalCode( "50667" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Schulstraße 9", "Düsseldorf",
                    FactoryMethodInvoker.instantiatePostalCode( "40213" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Dorfstraße 21", "Stuttgart",
                    FactoryMethodInvoker.instantiatePostalCode( "70173" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Marktstraße 5", "Leipzig",
                    FactoryMethodInvoker.instantiatePostalCode( "04109" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Rosenweg 14", "Dortmund",
                    FactoryMethodInvoker.instantiatePostalCode( "44135" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Wiesenstraße 6", "Essen",
                    FactoryMethodInvoker.instantiatePostalCode( "45127" ) )
    };

    public final static ClientType[] mockClients;

    public static final String EUR = "EUR";

    public static final Object[][] ITEM_DATA = new Object[][]{
            {UUID.randomUUID(), "TCD-34 v2.1", "Universelles Verbindungsstück für den einfachen Hausgebrauch bei der Schnellmontage",
                    1.5f, FactoryMethodInvoker.instantiateMoney( 5.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 10.0f, EUR )},
            {UUID.randomUUID(), "EFG-56", "Hochleistungsfähiger Kondensator für elektronische Schaltungen",
                    0.3f, FactoryMethodInvoker.instantiateMoney( 2.5f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 4.0f, EUR )},
            {UUID.randomUUID(), "MNP-89ff", "Langlebiger und robuster Motor für industrielle Anwendungen",
                    7.2f, FactoryMethodInvoker.instantiateMoney( 50.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 80.0f, EUR )},
            {UUID.randomUUID(), "Gh-25", "Kompakter und leichter Akku für mobile Geräte",
                    null, FactoryMethodInvoker.instantiateMoney( 6.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 8.0f, EUR )},
            {UUID.randomUUID(), "MultiBeast2", "Vielseitiger Adapter für verschiedene Steckertypen",
                    null, FactoryMethodInvoker.instantiateMoney( 0.6f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 1.0f, EUR )},
            {UUID.randomUUID(), "ABC-99 v4.2", "Leistungsstarker Prozessor für Computer und Server",
                    1.0f, FactoryMethodInvoker.instantiateMoney( 150.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 250.0f, EUR )},
            {UUID.randomUUID(), "Stuko22", "Ersatzteil Spitze für Präzisionswerkzeug zum Löten und Schrauben",
                    null, FactoryMethodInvoker.instantiateMoney( 0.3f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 0.5f, EUR )},
            {UUID.randomUUID(), "Btt2-Ah67", "Kraftstoffeffiziente und umweltfreundliche Hochleistungsbatterie",
                    6.0f, FactoryMethodInvoker.instantiateMoney( 80.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 120.0f, EUR )},
            {UUID.randomUUID(), "JKL-67", "Wasserdichtes Gehäuse",
                    3.0f, FactoryMethodInvoker.instantiateMoney( 1.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 1.2f, EUR )},
            {UUID.randomUUID(), "MNO-55-33", "Modulares Netzteil für flexible Stromversorgung",
                    5.5f, FactoryMethodInvoker.instantiateMoney( 25.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 45.0f, EUR )},
            {UUID.randomUUID(), "PQR-80", "Effizienter Kühler für verbesserte Wärmeableitung",
                    4.0f, FactoryMethodInvoker.instantiateMoney( 20.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 35.0f, EUR )},
            {UUID.randomUUID(), "STU-11 Ld", "Hochwertiger Grafikchip für leistungsstarke PCs",
                    null, FactoryMethodInvoker.instantiateMoney( 200.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 350.0f, EUR )},
            {UUID.randomUUID(), "VWX-90 FastWupps", "Schnellladegerät für eine Vielzahl von Geräten",
                    null, FactoryMethodInvoker.instantiateMoney( 15.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 25.0f, EUR )},
            {UUID.randomUUID(), "YZZ-22 v1.8", "Leichter und stabiler Rahmen aus Aluminium",
                    3.5f, FactoryMethodInvoker.instantiateMoney( 60.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 100.0f, EUR )}
    };

    private ClientRegistrationUseCases clientRegistrationUseCases;
    private ItemCatalogUseCases itemCatalogUseCases;
    private InventoryManagementUseCases inventoryManagementUseCases;

    // item 0 is out of inventory, item 1 and 2 have fixed quantities of 10 and 20, respectively, and the
    // others have a random inventory between 5 and 100
    public static final Map<UUID, Integer> ITEM_INVENTORY = new HashMap<>();

    static {
        mockClients = new ClientType[CLIENT_NAME.length];
        for ( int i = 0; i < CLIENT_NAME.length; i++ ) {
            mockClients[i] = new MockClient(
                    CLIENT_NAME[i], CLIENT_EMAIL[i], CLIENT_ADDRESS[i]);
        }
        Random random = new Random();
        for ( Object[] itemData : ITEM_DATA ) {
            ITEM_INVENTORY.put( (UUID) itemData[0], random.nextInt( 100 ) + 20 );
        }
        ITEM_INVENTORY.put( (UUID) ITEM_DATA[0][0], 0 );
        ITEM_INVENTORY.put( (UUID) ITEM_DATA[1][0], 10 );
        ITEM_INVENTORY.put( (UUID) ITEM_DATA[2][0], 20 );
        ITEM_INVENTORY.put( (UUID) ITEM_DATA[3][0], 30 );

    }


    public enum InvalidReason {
        NULL, EMPTY;

        public Object getInvalidValue( Object originalValue ) {
            switch (this) {
                case NULL:
                    return null;
                case EMPTY:
                    return "";
                default:
                    return null;
            }
        }
    }


    @Autowired
    public InitialMasterDataCreator(
            ClientRegistrationUseCases clientRegistrationUseCases,
            ItemCatalogUseCases itemCatalogUseCases,
            InventoryManagementUseCases inventoryManagementUseCases ) {
        this.clientRegistrationUseCases = clientRegistrationUseCases;
        this.itemCatalogUseCases = itemCatalogUseCases;
        this.inventoryManagementUseCases = inventoryManagementUseCases;
    }

    public void registerAllClients() {
        for ( int i = 0; i < InitialMasterDataCreator.CLIENT_NAME.length; i++ ) {
            registerClient( InitialMasterDataCreator.CLIENT_NAME[i], CLIENT_EMAIL[i], CLIENT_ADDRESS[i] );
        }
    }


    public void registerClient( String name, MailAddressType mailAddress, HomeAddressType homeAddress ) {
        clientRegistrationUseCases.register( name, mailAddress, homeAddress );
    }

    public void addAllItems() {
        for ( Object[] itemData : ITEM_DATA ) {
            addItemDataToCatalog( itemData );
        }
    }

    public void addItemDataToCatalog( Object[] itemData ) {
        itemCatalogUseCases.addItemToCatalog( (UUID) itemData[0], (String) itemData[1], (String) itemData[2],
                (Float) itemData[3], (MoneyType) itemData[4], (MoneyType) itemData[5] );
    }


    public Object[] getItemDataInvalidAtIndex( int index, InvalidReason reason ) {
        Object[] itemData = ITEM_DATA[1];
        Object[] itemDataInvalid = new Object[itemData.length];
        System.arraycopy( itemData, 0, itemDataInvalid, 0, itemData.length );
        itemDataInvalid[index] = itemData[index].getClass().cast(
                reason.getInvalidValue( itemData[index] ) );
        return itemDataInvalid;
    }


    public void inventoryUpAllItems() {
        for ( Object[] itemData : ITEM_DATA ) {
            if ( ITEM_INVENTORY.get( itemData[0] ) > 0 )
                inventoryManagementUseCases.addToInventory( (UUID) itemData[0], ITEM_INVENTORY.get( itemData[0] ) );
        }
    }
}
