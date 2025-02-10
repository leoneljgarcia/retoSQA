package org.example.testCase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.BusinessException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.example.util.Constantes;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerificarTope {
    WebDriver driver = new EdgeDriver();
    List<WebElement> products = new ArrayList<>();
    double cartPrice = 0.00;
    int contProductos = 0;
    int maxQuantity = 0;

    private void addToCart(WebElement producto, int index, String urlBase) throws InterruptedException, BusinessException {
        WebElement button = producto.findElements(By.className(Constantes.CLASS_ADD_TO_CART)).getFirst();
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        if (contProductos + 1 > maxQuantity) {
            mostrarMensajeError(Constantes.ERROR_MAX_PRODUCTS);
            driver.quit();
            throw new BusinessException(Constantes.ERROR_MAX_PRODUCTS);
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        String urlActual = driver.getCurrentUrl();
        if (urlActual.contains("product/")) {
            WebElement button2 = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ADD_TO_CART)).getFirst();
            button2.click();
            Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
            driver.get(urlBase);
            products = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM));
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        }
        if (!validarSumaTotal(precioProductoIndice(index))) {
            mostrarMensajeError(Constantes.ERROR_CART_TOTAL);
            throw new BusinessException(Constantes.ERROR_CART_TOTAL);
        }
        if (cartCount() != contProductos + 1) {
            mostrarMensajeError(Constantes.ERROR_ADD_PRODUCT);
            throw new BusinessException(Constantes.ERROR_ADD_PRODUCT);
        } else {
            contProductos++;
        }
    }

    private double precioProductoIndice(int index) throws InterruptedException {
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        WebElement element = products.get(index).findElements(By.className(Constantes.CLASS_PRODUCT_PRICE)).getFirst();
        return Double.parseDouble(element.getText().substring(1));
    }

    private double totalCart() throws InterruptedException {
        WebElement botonCart = driver.findElements(By.className(Constantes.CLASS_MENU_BTN)).getFirst();
        botonCart.click();
        WebElement element = driver.findElements(By.id(Constantes.ID_TOTAL_CART_AMOUNT)).getFirst();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        double cartTotal = Double.parseDouble(element.getText().substring(1));
        driver.findElements(By.className(Constantes.CLASS_PUSHY_LINK)).getFirst().click();
        return cartTotal;
    }

    private int cartCount() {
        return Integer.parseInt(driver.findElement(By.id(Constantes.ID_CART_COUNT)).getText());
    }

    private boolean validarSumaTotal(double productPrice) throws InterruptedException {
        cartPrice += productPrice;
        double cartActual = totalCart();
        return (cartPrice < 100.00) ? (cartActual == cartPrice + Constantes.ENVIO) : (cartActual == cartPrice);
    }


    private void mostrarMensajeError(String mensaje) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("alert('" + mensaje + "');");
        Thread.sleep(Constantes.WAIT_TIME_LONG);
        driver.switchTo().alert().accept();
    }

    public boolean testLimitProducts() throws BusinessException, InterruptedException, IOException {
        driver.get(Constantes.URL_BASE);
        driver.manage().window().maximize();
        File archivo = new File(Constantes.SETTINGS_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(archivo);
        maxQuantity = jsonNode.get("maxQuantity").asInt();
        products = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM));
        int countProductsPage =  driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM)).size();
        for(int i = 0; i < countProductsPage; i++){
            addToCart(products.get(i), i, Constantes.URL_BASE);
        }

        if(countProductsPage <= maxQuantity){
            driver.get(Constantes.URL_PAGE_2);
            int countProductPage2 = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM)).size();
            Thread.sleep(2000);
            products = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM));
            for (int i = 0; i < countProductPage2; i++){
                addToCart(products.get(i), i, Constantes.URL_PAGE_2);
            }
        }
        return true;
    }
}
