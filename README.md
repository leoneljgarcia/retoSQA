# 📌 Proyecto de Automatización con Selenium y Azure DevOps

Este proyecto realiza pruebas automatizadas utilizando **Selenium con Java** y se integra con **Azure DevOps** para la ejecución en pipelines.

## 📂 Estructura del Proyecto

- `src/main/java/org/example/` → Código fuente del proyecto.
- `src/test/java/org/example/` → Pruebas unitarias con JUnit y Mockito.
- `pom.xml` → Configuración de dependencias Maven.
- `azure-pipelines.yml` → Definición del pipeline en Azure DevOps.
- `src/main/java/org/example/testCase/VerificarTope.java` → ejecuta el caso de prueba 
-  que valida el límite de productos permitidos
- `src/main/java/org/example/testCase/AgregarQuitarProduct.java` → ejecuta el caso de prueba
-  que agrega y quita productos al cart con sus respectivas validaciones

## 🚀 Requisitos

- **Java 21 o superior**
- **Maven 3.8+**
- **Selenium WebDriver**
- **Azure DevOps (para ejecutar el pipeline)**
- **expressCart**

## 🛠 Instalación


  
1. Modifica los el settings.json de la aplicación expressCart con los valores:  
   currencySymbol: $
   currencyISO: USD
   maxQuantity: 6  
   databaseConnectionString: "ruta de tu base de datos mongoDB"

3. Ve a la carpeta de la aplicación expressCart y ejecuta los siguientes comandos:
   corremos los datos de prueba
   ```sh
   npm run testdata
   npm start

1. clonamos la aplicación :
   ```sh
   git clone https://github.com/leoneljgarcia/retoSQA.git

2. modificamos el archivo paths.txt:

   En la primera línea colocamos la ruta de nuestro WebDrive,
   por defecto ya viene el WebDriver de microsoft edge.

   En la segunda línea ponemos la ruta de nuestro settings.json de expressCart

3. instala dependencias con Maven:
   ```sh
   mvn clean install
   
  
Esto automáticamente abrirá el navegador y empezará a ejecutar las pruebas  

## 🚀 Manejo de excepciones  

para el manejo de excepciones se creó la excepción BusinnesException,
esta se activa en cada caso de prueba, ya sea al momento de agregar un producto al cart,
eliminar un producto del cart, y adicionalmente se ejecuta una prueba para superar el límite
de productos permitidos en el cart.  
  
Las excepciones están documentadas en las clases AgregarQuitarProduct.java y VerificarTope.java,  
ahí encontraremos las validaciones que se hacen y que nos pide el documento del reto.  
  
## Validaciones al momento de agregar un producto:  
   Clase AgregarQuitarProduct.java:  
   Lanza una excepción si ya se superó el número de productos máximo configurado (línea 52).  
   Lanza una excepción si al agregar el producto el precio total no equivale a lo esperado (línea 69).  
   Lanza una excepción si al agregar el producto el total de productos del carrito no equivale a lo esperado (línea 74).  
   Lanza una excepción si al eliminar el producto el total de productos del carrito no equivale a lo esperado (línea 115).  
   Lanza una excepción si al eliminar el producto el precio total no equivale a lo esperado (línea 120).  

   Clase VerificarTope.java:  
   Lanza una excepción si ya se superó el número de productos máximo configurado (línea 55).  
   Lanza una excepción si al agregar el producto el precio total no equivale a lo esperado (línea 73).  
   Lanza una excepción si al agregar el producto el total de productos del carrito no equivale a lo esperado (línea 78).  

Adicionalmente a esto podemos modificar el parámetro maxQuantity = -1 en nuestro settings.json  
para poder observar el mensaje de error interno (500).  
