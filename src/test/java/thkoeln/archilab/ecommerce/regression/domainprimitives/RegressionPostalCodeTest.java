package thkoeln.archilab.ecommerce.regression.domainprimitives;

import org.junit.jupiter.api.Test;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.PostalCodeType;

import static org.junit.jupiter.api.Assertions.*;
import static thkoeln.archilab.ecommerce.masterdata.FactoryMethodInvoker.*;

class RegressionPostalCodeTest {

    @Test
    public void testToString() {
        // given
        String input = "12345";

        // when
        PostalCodeType instance = instantiatePostalCode( input );

        // then
        assertEquals( input, instance.toString() );
    }

    @Test
    public void testFactoryMethodValid() {
        // given
        // when
        // then
        assertDoesNotThrow( () -> instantiatePostalCode( "12345" ) );
    }

    @Test
    public void testFactoryMethodInvalid() {
        // given
        // when
        // then
        assertThrows( ShopException.class, () -> instantiatePostalCode( "123456" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( "1234" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( "20000" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( null ) );
    }

    @Test
    public void testValueObjectEquality() {
        // given
        // when
        PostalCodeType instance1 = instantiatePostalCode( "12345" );
        PostalCodeType instance2 = instantiatePostalCode( "12345" );
        PostalCodeType instance3 = instantiatePostalCode( "54321" );

        // then
        assertEquals( instance1, instance2 );
        assertNotEquals( instance1, instance3 );
    }

    @Test
    public void testImmutability() {
        // given
        // when
        PostalCodeType instance = instantiatePostalCode( "12345" );

        // then
        try {
            instance.getClass().getMethod( "setpostalCode", String.class );
            fail( "setpostalCode method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
    }
}
