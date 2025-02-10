package org.example;

import org.example.exception.BusinessException;
import org.example.testCase.AgregarQuitarProduct;
import org.example.testCase.VerificarTope;
import org.example.util.Constantes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class MainTest {

    private VerificarTope verificarTope;
    private AgregarQuitarProduct agregarQuitarProduct;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcesarPrueba() throws IOException, InterruptedException {
        agregarQuitarProduct = new AgregarQuitarProduct();
        Assertions.assertTrue(agregarQuitarProduct.procesarPrueba());
    }

    @Test
    public void testLimitProducts (){
        verificarTope = new VerificarTope();
        Exception exception = Assertions.assertThrows(BusinessException.class, () -> {
            verificarTope.testLimitProducts();
        });
        Assertions.assertEquals(Constantes.ERROR_MAX_PRODUCTS, exception.getMessage());
    }
}
