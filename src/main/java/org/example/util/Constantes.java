package org.example.util;

public class Constantes {
    // URLs
    public static final String URL_BASE = "http://127.0.0.1:1111/";
    public static final String URL_PAGE_2 = URL_BASE + "page/2";

    // Configuración del WebDriver
    public static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
    public static final String DRIVER_PATH = "C:\\Users\\Victus\\Documents\\pruebas SQA\\msedgedriver.exe";

    // Clases y IDs del HTML
    public static final String CLASS_PRODUCT_ITEM = "product-item";
    public static final String CLASS_ADD_TO_CART = "add-to-cart";
    public static final String CLASS_PRODUCT_ADD_TO_CART = "product-add-to-cart";
    public static final String CLASS_MENU_BTN = "menu-btn";
    public static final String ID_TOTAL_CART_AMOUNT = "total-cart-amount";
    public static final String CLASS_PUSHY_LINK = "pushy-link";
    public static final String ID_CART_COUNT = "cart-count";
    public static final String CLASS_CART_PRODUCT = "cart-product";
    public static final String CLASS_PRODUCT_PRICE = "product-price";
    public static final String CLASS_MY_AUTO = "my-auto";
    public static final String CLASS_BTN_DELETE_FROM_CART = "btn-delete-from-cart";

    // Mensajes de Error
    public static final String ERROR_MAX_PRODUCTS = "Error: Se ha superado el límite de productos";
    public static final String ERROR_CART_TOTAL = "La suma del producto agregado no coincide con el total del carrito";
    public static final String ERROR_ADD_PRODUCT = "Error al intentar agregar producto";
    public static final String ERROR_DELETE_PRODUCT = "Error al intentar eliminar el producto";
    public static final String ERROR_CART_REDUCED_TOTAL = "El precio total no coincide con la resta del artículo eliminado";
    public static final String ERROR_SERVER = "Error en el servidor: ";

    // Tiempos de Espera
    public static final int WAIT_TIME_SHORT = 1000;
    public static final int WAIT_TIME_MEDIUM = 2000;
    public static final int WAIT_TIME_LONG = 3000;

    // Otros
    public static final double ENVIO = 10.00;
    public static final String SETTINGS_PATH = "C:/Users/Victus/Documents/pruebas SQA/expressCart/config/settings.json";
    private Constantes() {
    }
}
