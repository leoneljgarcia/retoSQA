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

public class AgregarQuitarProduct {
    WebDriver driver = new EdgeDriver();
    List<WebElement> products = new ArrayList<>();
    double cartPrice = 0.00;
    int contProductos = 0;
    int maxQuantity = 0;

    public boolean procesarPrueba() throws IOException, InterruptedException {
        System.setProperty(Constantes.DRIVER_PROPERTY, Constantes.obtenerLineaPath(Constantes.DRIVER_PATH));
        File archivo = new File(Constantes.obtenerLineaPath(Constantes.SETTINGS_PATH));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(archivo);
        maxQuantity = jsonNode.get("maxQuantity").asInt();
        try {
            driver.get(Constantes.URL_BASE);
            driver.manage().window().maximize();
            products = driver.findElements(By.className(Constantes.CLASS_PRODUCT_ITEM));
            int numItems = products.size();
            for (int i = 0; i < numItems; i++) {
                addToCart(products.get(i), i, Constantes.URL_BASE);
            }
            quitToCard();
        } catch (Exception e) {
            mostrarMensajeError(Constantes.ERROR_SERVER + e.getMessage());
            e.printStackTrace();
        }
        driver.quit();
        return true;
    }

    public void addToCart(WebElement producto, int index, String urlBase) throws InterruptedException, BusinessException {
        WebElement button = producto.findElements(By.className(Constantes.CLASS_ADD_TO_CART)).getFirst();
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        if (contProductos + 1 > maxQuantity) {
            mostrarMensajeError(Constantes.ERROR_MAX_PRODUCTS);
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

    public double precioProductoIndice(int index) throws InterruptedException {
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        WebElement element = products.get(index).findElements(By.className(Constantes.CLASS_PRODUCT_PRICE)).getFirst();
        return Double.parseDouble(element.getText().substring(1));
    }

    public double totalCart() throws InterruptedException {
        WebElement botonCart = driver.findElements(By.className(Constantes.CLASS_MENU_BTN)).getFirst();
        botonCart.click();
        WebElement element = driver.findElements(By.id(Constantes.ID_TOTAL_CART_AMOUNT)).getFirst();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(Constantes.WAIT_TIME_MEDIUM);
        double cartTotal = Double.parseDouble(element.getText().substring(1));
        driver.findElements(By.className(Constantes.CLASS_PUSHY_LINK)).getFirst().click();
        return cartTotal;
    }

    public int cartCount() {
        return Integer.parseInt(driver.findElement(By.id(Constantes.ID_CART_COUNT)).getText());
    }

    public void quitToCard() throws InterruptedException, BusinessException {
        driver.findElements(By.className(Constantes.CLASS_MENU_BTN)).getFirst().click();
        List<WebElement> cartProducts = driver.findElements(By.className(Constantes.CLASS_CART_PRODUCT));
        int numProducts = cartProducts.size();
        while (!cartProducts.isEmpty()) {
            Thread.sleep(Constantes.WAIT_TIME_SHORT);
            WebElement buttonDelete = cartProducts.get(0).findElement(By.className(Constantes.CLASS_BTN_DELETE_FROM_CART));
            double price = Double.parseDouble(cartProducts.get(0).findElement(By.className(Constantes.CLASS_MY_AUTO)).getText().substring(1));
            buttonDelete.click();
            Thread.sleep(Constantes.WAIT_TIME_SHORT);
            cartProducts = driver.findElements(By.className(Constantes.CLASS_CART_PRODUCT));
            if (!(numProducts - 1 == cartProducts.size())) {
                mostrarMensajeError(Constantes.ERROR_DELETE_PRODUCT);
                throw new BusinessException(Constantes.ERROR_DELETE_PRODUCT);
            }
            if (numProducts - 1 > 0 && !validarRestaTotal(price)) {
                mostrarMensajeError(Constantes.ERROR_CART_REDUCED_TOTAL);
                throw new BusinessException(Constantes.ERROR_CART_REDUCED_TOTAL);
            }
            numProducts--;
        }
    }

    public boolean validarSumaTotal(double productPrice) throws InterruptedException {
        cartPrice += productPrice;
        double cartActual = totalCart();
        return (cartPrice < 100.00) ? (cartActual == cartPrice + Constantes.ENVIO) : (cartActual == cartPrice);
    }

    public boolean validarRestaTotal(double productPrice) throws InterruptedException {
        cartPrice = Math.round((cartPrice - productPrice) * 100.0) / 100.0;
        Thread.sleep(Constantes.WAIT_TIME_SHORT);
        double cartActual = Double.parseDouble(driver.findElement(By.id(Constantes.ID_TOTAL_CART_AMOUNT)).getText().substring(1));
        return (cartPrice < 100.00) ? (cartActual == cartPrice + Constantes.ENVIO) : (cartActual == cartPrice);
    }

    public void mostrarMensajeError(String mensaje) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("alert('" + mensaje + "');");
        Thread.sleep(Constantes.WAIT_TIME_LONG);
        driver.switchTo().alert().accept();
    }

}
