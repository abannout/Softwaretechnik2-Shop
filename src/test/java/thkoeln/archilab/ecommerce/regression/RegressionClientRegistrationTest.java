package thkoeln.archilab.ecommerce.regression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator;
import thkoeln.archilab.ecommerce.usecases.ClientRegistrationUseCases;
import thkoeln.archilab.ecommerce.usecases.ClientType;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static thkoeln.archilab.ecommerce.masterdata.FactoryMethodInvoker.instantiateMailAddress;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.CLIENT_ADDRESS;
import static thkoeln.archilab.ecommerce.masterdata.InitialMasterDataCreator.CLIENT_EMAIL;

@SpringBootTest
@Transactional
public class RegressionClientRegistrationTest {

    @Autowired
    private ClientRegistrationUseCases clientRegistrationUseCases;
    @Autowired
    private InitialMasterDataCreator initialMasterDataCreator;

    private MailAddressType nonExistingMailAddress;

    @BeforeEach
    public void setUp() {
        initialMasterDataCreator.deleteAll();
        nonExistingMailAddress = instantiateMailAddress( "this@nononono.de" );
    }


    @Test
    public void testAllClientsRegistered() {
        // given
        initialMasterDataCreator.registerAllClients();

        // when
        ClientType client3 = clientRegistrationUseCases.getClientData( CLIENT_EMAIL[3] );

        // then
        Assertions.assertEquals( initialMasterDataCreator.CLIENT_NAME[3], client3.getName() );
        Assertions.assertEquals( CLIENT_EMAIL[3], client3.getMailAddress() );
        Assertions.assertEquals( CLIENT_ADDRESS[3], client3.getHomeAddress() );
    }


    @Test
    public void testRegisterClientWithDuplicateMailAddress() {
        // given
        initialMasterDataCreator.registerAllClients();

        // when
        // then
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.register( "Gandalf The Grey", CLIENT_EMAIL[5],
                        CLIENT_ADDRESS[5] ) );
    }

    @Test
    public void testRegisterClientWithDuplicateNameOrHomeAddress() {
        // given
        initialMasterDataCreator.registerAllClients();
        MailAddressType newMailAddress = instantiateMailAddress( "some@this.de" );

        // when
        // then
        assertDoesNotThrow(() ->
                clientRegistrationUseCases.register( initialMasterDataCreator.CLIENT_NAME[2], newMailAddress,
                        CLIENT_ADDRESS[2] ) );
    }


    @Test
    public void testDeleteClientsNoMoreClients() {
        // given
        initialMasterDataCreator.registerAllClients();

        // when
        clientRegistrationUseCases.deleteAllClients();

        // then
        assertThrows( ShopException.class, () -> clientRegistrationUseCases.getClientData( CLIENT_EMAIL[0] ) );
    }

}
