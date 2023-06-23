package thkoeln.archilab.ecommerce.regression.domainprimitives;

import org.junit.jupiter.api.Test;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MailAddressType;

import static org.junit.jupiter.api.Assertions.*;
import static thkoeln.archilab.ecommerce.masterdata.FactoryMethodInvoker.*;

class RegressionMailAddressTest {

    @Test
    public void testToString() {
        // given
        String input = "test@example.com";

        // when
        MailAddressType instance = instantiateMailAddress( input );

        // then
        assertEquals( input, instance.toString() );
    }

    @Test
    public void testFactoryMethodValid() {
        // given
        // when
        // then
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "99Z@example.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "GGGhh@s77.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "a@4.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "Max.Hammer@example.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "Max.Gideon.Hammer@example.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.this.com" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.de" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.at" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.ch" ) );
        assertDoesNotThrow( () -> instantiateMailAddress( "test@example.org" ) );
    }

    @Test
    public void testFactoryMethodInvalid() {
        // given
        // when
        // then
        assertThrows( ShopException.class, () -> instantiateMailAddress( null ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "testexample.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "@example.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "Max..Gideon.Hammer@example.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@examplecom" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@example..com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@example@that.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test example@that.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test#example@that.com" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@example.biz" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@example.biz" ) );
        assertThrows( ShopException.class, () -> instantiateMailAddress( "test@example.42" ) );
    }

    @Test
    public void testValueObjectEquality() {
        // given
        // when
        MailAddressType instance1 = instantiateMailAddress( "test@example.com" );
        MailAddressType instance2 = instantiateMailAddress( "test@example.com" );
        MailAddressType instance3 = instantiateMailAddress( "different@example.com" );

        // then
        assertEquals( instance1, instance2 );
        assertNotEquals( instance1, instance3 );
    }

    @Test
    public void testImmutability() {
        // given
        // when
        MailAddressType instance = instantiateMailAddress( "test@example.com" );

        // then
        try {
            instance.getClass().getMethod( "setMailAddress", String.class );
            fail( "setMailAddress method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
    }
}
