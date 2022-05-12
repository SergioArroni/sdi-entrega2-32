package com.uniovi.sdi_entrega_2_32;

import com.uniovi.sdi_entrega_2_32.pageobjects.*;
import com.uniovi.sdi_entrega_2_32.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SdiEntrega132ApplicationTests {
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
            //"C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver ="C:\\nada.exe;
    //static String GeckodriverHugo ="C:\\Users\\Hugo\\Desktop\\TERCER_CURSO_INGENIERIA\\SDI\\PRACTICA\\sesion06\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

    //static String GeckodriverAndrea = "C:\\Users\\andre\\Documents\\CURSO 2021-2022\\CUATRI 2\\SDI\\geckodriver.exe";
    static String GeckodriverSergio = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";

    //Común a Windows y a MACOSX
    static WebDriver driver = getDriver(PathFirefox, GeckodriverSergio);

    static String URL = "http://localhost:8081";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();

    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    //[Prueba1] Registro de Usuario con datos válidos.
    @Test
    @Order(1000)
    public void PR01() {
        SeleniumUtils.registerMacro(driver,"REALTEST@email.es", "Aaaa", "Maria");
    }

    // PR02. Registro de usuario con datos inválidos (email vacío, nombre vacío,
    // apellidos vacíos)
    @Test
    @Order(2)
    public void PR02() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "register", "class", "btn btn-primary");
        // Rellenamos el formulario, dejando vacío el campo de nombre
        PO_SignUpView.fillForm(driver, "fallo@email.es", "", "González", "123456", "123456");

        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());

        // Rellenamos el formulario, dejando vacío el campo de email
        PO_SignUpView.fillForm(driver, "", "Sara", "González", "123456", "123456");

        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());

        // Rellenamos el formulario, dejando vacío el campo de email
        PO_SignUpView.fillForm(driver, "fallo@email.es", "Sara", "", "123456", "123456");

        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());

    }

    //[Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(3)
    public void PR03() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "register", "class", "btn btn-primary");
        // Rellenamos el formulario, con la repeticion de la contraseña incorrecta
        PO_SignUpView.fillForm(driver, "fall@email.com", "Luis", "Ma", "123456", "1234567");

        String check = "Las dos contraseñas no coinciden";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", check);

        Assertions.assertEquals(check, result.get(0).getText());
    }

    //[Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(4)
    public void PR04() {
        // Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "register", "class", "btn btn-primary");
        // Rellenamos el formulario, con un email ya existente
        PO_SignUpView.fillForm(driver, "user01@email.com", "Diego", "Diaz", "123456", "123456");

        String check = "Este email ya esta vinculado con un usuario";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", check);

        Assertions.assertEquals(check, result.get(0).getText());
    }

    //[Prueba5] Inicio de sesión con datos válidos (administrador).
    @Test
    @Order(5)
    public void PR05() {
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario, como administrador
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        SeleniumUtils.idIsPresentOnPage(driver, "userList");
    }

    //[Prueba6] Inicio de sesión con datos válidos (usuario estándar).
    @Test
    @Order(6)
    public void PR06() {
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario, como un usuario normal
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        SeleniumUtils.idIsPresentOnPage(driver, "friends");
    }

    //[Prueba7] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
    @Test
    @Order(7)
    public void PR07() {
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario, como un usuario normal pero con la contraseña y email vacios
        PO_LoginView.fillLoginForm(driver, "", "");
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());
    }

    //[Prueba8] Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña
    //incorrecta).
    @Test
    @Order(8)
    public void PR08() {
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario, como un usuario normal pero con la contraseña incorrecta
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "12345678");
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());

    }

    //[Prueba9] Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
    //sesión (Login).
    @Test
    @Order(9)
    public void PR09() {
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.idIsPresentOnPage(driver, "friends");

        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        SeleniumUtils.idIsPresentOnPage(driver, "login");
    }

    //[Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
    @Test
    @Order(10)
    public void PR10() {
        SeleniumUtils.textIsNotPresentOnPage(driver, "Cierra sesion");
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.idIsPresentOnPage(driver, "friends");

        SeleniumUtils.idIsPresentOnPage(driver, "logout_button");
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        SeleniumUtils.idIsNotPresentOnPage(driver, "logout_button");
    }




    //[Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema.
    @Test
    @Order(11)
    public void PR11() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        SeleniumUtils.textIsPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "admin@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user08@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user01@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user04@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user10@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user11@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "pruebaTest2@email.es");
        SeleniumUtils.textIsPresentOnPage(driver, "pruebaTestReal@email.es");
    }

    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(1002)
    public void PR12() {

        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        driver.findElement(By.id("1")).click();
        driver.findElement(By.id("borrar")).click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "REALTEST@email.es");

    }

    //[Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(1003)
    public void PR13() {

//        SeleniumUtils.registerMacro(driver,"REALTEST@email.es", "Zzz", "Maria");

        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        driver.findElement(By.id("11")).click();
        driver.findElement(By.id("borrar")).click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "REALTEST@email.es");
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos usuarios
    //desaparecen.
    @Test
    @Order(1004)
    public void PR14() {

       SeleniumUtils.registerMacro(driver,"REALTEST1@email.es", "Aaaa", "Maria");

       SeleniumUtils.registerMacro(driver,"REALTEST2@email.es", "Aaa", "Maria");

        SeleniumUtils.registerMacro(driver,"REALTEST3@email.es", "Aa", "Maria");

        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        driver.findElement(By.id("1")).click();
        driver.findElement(By.id("2")).click();
        driver.findElement(By.id("3")).click();
        driver.findElement(By.id("borrar")).click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "REALTEST1@email.es");
        SeleniumUtils.textIsNotPresentOnPage(driver, "REALTEST2@email.es");
        SeleniumUtils.textIsNotPresentOnPage(driver, "REALTEST3@email.es");

    }

        //[Prueba15] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema,
    //excepto el propio usuario y aquellos que sean Administradores
    @Test
    @Order(15)
    public void PR15() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que no esta el administrador ni el usuario registrado
        SeleniumUtils.textIsNotPresentOnPage(driver,"user01@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver,"admin@email.com");


        SeleniumUtils.textIsPresentOnPage(driver,"user08@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user11@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user04@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user10@email.com");
    }

    //[Prueba16] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
    //corresponde con el listado usuarios existentes en el sistema.
    @Test
    @Order(16)
    public void PR16() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        // Escribir en la barra de búsqueda
        PO_PrivateView.fillSearch(driver,"");

        SeleniumUtils.textIsPresentOnPage(driver,"user08@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user11@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user04@email.com");
        SeleniumUtils.textIsPresentOnPage(driver,"user10@email.com");

    }

   // [Prueba17] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
   // muestra la página que corresponde, con la lista de usuarios vacía.
    @Test
    @Order(17)
    public void PR17() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Hacemos una búsqueda escribiendo un texto que no exista
        PO_PrivateView.fillSearch(driver, "fhekufla");

        //Comprobamos que aparece la lista de usuarios vacía
        List<WebElement> result = PO_View.checkElementBy(driver, "id", "tbody");

        int numUsuarios=result.get(0).getSize().getHeight();
        Assertions.assertEquals(0,numUsuarios);

    }

    //[Prueba18] Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
    //corresponde, con la lista de usuarios en los que el texto especificado sea parte de su nombre, apellidos o
    //de su email
    @Test
    @Order(18)
    public void PR18() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        //Hacemos una búsqueda escribiendo un texto que no exista
        PO_PrivateView.fillSearch(driver, "user");

        //Comprobamos que aparece la lista de usuarios vacía
        List<WebElement> result = PO_View.checkElementBy(driver, "class", "usersTable");

        for(int i=0;i<result.size();i++){
            Assertions.assertTrue(result.get(i).getText().contains("user"));

        }

    }
   // [Prueba19] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario.
    //Comprobar que la solicitud de amistad aparece en el listado de invitaciones (punto siguiente).
    @Test
    @Order(19)
    public void PR19() {
        //NOTA: Si lo ejecutas varias veces hay que eliminar la primera invitacion de la base de datos
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Enviamos la peticion al user04
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/invitar/6274d39b467a68732f90f6f3')]", 0);
        // Vamos al formulario de logeo
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        //PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");
        PO_PrivateView.click(driver, "//a[contains(@href, '/invitaciones/listInvitaciones')]", 0);

        SeleniumUtils.textIsPresentOnPage(driver,"Mongo");
    }
    //[Prueba20] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario al
    //que ya le habíamos enviado la invitación previamente. No debería dejarnos enviar la invitación, se podría
    //ocultar el botón de enviar invitación o notificar que ya había sido enviada previamente
    @Test
    @Order(20)
    public void PR20() {
        //NOTA: Si lo ejecutas varias veces hay que eliminar la primera invitacion de la base de datos
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Enviamos la peticion otra vez al user04
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/invitar/6274d39b467a68732f90f6f3')]", 0);
        SeleniumUtils.textIsPresentOnPage(driver,"Ya hay invitacion");
    }
    //[Prueba21] Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un listado que
    //contenga varias invitaciones recibidas.
    @Test
    @Order(21)
    public void PR21() {
        //NOTA: Si lo ejecutas varias veces hay que eliminar la primera invitacion de la base de datos
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Accede directamente a la lista
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");
        //Enviamos la peticion otra vez al user04
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/invitar/6274d39b467a68732f90f6f3')]", 0);
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");
        PO_PrivateView.click(driver, "//a[contains(@href, '/invitaciones/listInvitaciones')]", 0);
        SeleniumUtils.textIsPresentOnPage(driver,"Mongo");
        SeleniumUtils.textIsPresentOnPage(driver,"Lucas");
    }
    //[Prueba22] Sobre el listado de invitaciones recibidas. Hacer clic en el botón/enlace de una de ellas y
    //comprobar que dicha solicitud desaparece del listado de invitaciones.
    @Test
    @Order(22)
    public void PR22() {
        //NOTA: Si lo ejecutas varias veces hay que eliminar la primera invitacion de la base de datos
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        PO_PrivateView.click(driver, "//a[contains(@href, '/invitaciones/listInvitaciones')]", 0);

        PO_PrivateView.click(driver, "//a[contains(@href, '/invitaciones/aceptar/626b9f4d58be38a6ca85cdbe')]", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, '/invitaciones/listInvitaciones')]", 0);
        SeleniumUtils.textIsNotPresentOnPage(driver,"Mongo");
        SeleniumUtils.textIsPresentOnPage(driver,"Lucas");
    }
  
    // PR23. Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene los amigos que deben ser
    @Test
    @Order(23)
    public void PR23() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Se despliega el menú de amigos, y se clica en lista de amigos
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/friends')]", 0);

        SeleniumUtils.textIsPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "Lucas");
        SeleniumUtils.textIsPresentOnPage(driver, "Preso");
        PO_NavView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    // PR24. Crear una publicación y comprobar que se ha creado correctamente
    @Test
    @Order(24)
    public void PR24() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        // Entramos en la ventana de creación
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/create/publication')]", 0);

        // Añadimos un título
        List<WebElement> title = SeleniumUtils.waitLoadElementsBy(driver, "id", "titulo", PO_View.getTimeout());
        title.get(0).sendKeys("Prueba título");

        // Añadimos el contenido
        List<WebElement> content = PO_View.checkElementBy(driver, "id", "texto");
        content.get(0).sendKeys("Prueba contenido");

        // Confirmamos
        content = PO_View.checkElementBy(driver, "id", "enviar");
        content.get(0).click();

        //Hay que comprobar que se añade
        SeleniumUtils.textIsPresentOnPage(driver, "Prueba título");
        SeleniumUtils.textIsPresentOnPage(driver, "Prueba contenido");
    }
    //[Prueba26] Mostrar el listado de publicaciones de un usuario y comprobar que se muestran todas las que
    //existen para dicho usuario
    @Test
    @Order(26)
    public void PR26() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        PO_PrivateView.click(driver, "//a[contains(@href, '/publications/listPublicaciones')]", 0);
        SeleniumUtils.textIsPresentOnPage(driver,"Primera publicación");
        SeleniumUtils.textIsPresentOnPage(driver,"Otra pub");
    }
    // PR27. Probar que se muestran todas las publicaciones de un amigo
    @Test
    @Order(27)
    public void PR27() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Se despliega el menú de amigos, y se clica en lista de amigos
        PO_PrivateView.click(driver, "//a[contains(@href, '/users/friends')]", 0);

        PO_PrivateView.click(driver, "//a[contains(@href, '/users/friends/publications/626b9b0958be38a6ca85cdbd')]", 0);

        SeleniumUtils.textIsPresentOnPage(driver, "Fri May 06 2022");
        SeleniumUtils.textIsPresentOnPage(driver, "Manolo");
        SeleniumUtils.textIsPresentOnPage(driver, "Bombo");
    }

    // PR28. Probar que no se puede acceder a las publicaciones de un usuario por URL sin ser su amigo
    @Test
    @Order(28)
    public void PR28() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        driver.get("http://localhost:8081/users/friends/publications/625f3514ec93e8d1e4a0d938");

        // Comprobamos que no hay publicaciones
        SeleniumUtils.idIsPresentOnPage(driver, "login");

    }

    // PR29. Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver al formulario de login.
    @Test
    @Order(29)
    public void PR29() {

        driver.get("http://localhost:8081/users/listUsers");

        SeleniumUtils.idIsPresentOnPage(driver, "login");

    }

    // PR30. Intentar acceder sin estar autenticado a la opción de listado de invitaciones de amistad recibida de un usuario estándar. Se deberá volver al formulario de login.
    @Test
    @Order(30)
    public void PR30() {

        driver.get("http://localhost:8081/invitaciones/listInvitaciones");

        SeleniumUtils.idIsPresentOnPage(driver, "login");

    }

    // PR31. Intentar acceder estando autenticado como usuario standard a la lista de amigos de otro usuario. Se deberá mostrar un mensaje de acción indebida.
    @Test
    @Order(31)
    public void PR31() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        driver.get("http://localhost:8081/users/friends/publications/625f3514ec93e8d1e4a0d938");

        // Comprobamos que no hay publicaciones
        SeleniumUtils.idIsPresentOnPage(driver, "login");

    }

    /**
     * [Prueba32] Inicio de sesión con datos válidos.
     */
    @Test
    @Order(32)
    public void PR32() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        SeleniumUtils.idIsPresentOnPage(driver, "widget-friends");
    }

    /**
     * [Prueba33] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
     */
    @Test
    @Order(33)
    public void PR33() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "usuario@noexiste.com", "cualquiera");

        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "text", PO_View.getTimeout());
    }

    /**
     * [Prueba34] Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
     */
    @Test
    @Order(34)
    public void PR34() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.idIsPresentOnPage(driver, "widget-friends");

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        SeleniumUtils.textIsPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "Lucas");
        SeleniumUtils.textIsPresentOnPage(driver, "Preso");
        SeleniumUtils.textIsPresentOnPage(driver, "pruebaTestReal@email.es");
        SeleniumUtils.textIsPresentOnPage(driver, "Ave");
        SeleniumUtils.textIsPresentOnPage(driver, "Maria");
        SeleniumUtils.textIsPresentOnPage(driver, "user10@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "Carla");
        SeleniumUtils.textIsPresentOnPage(driver, "Garcia");
    }

    /**
     * [Prueba35] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
     *         concreto, el nombre a buscar debe coincidir con el de un amigo.
     */
    @Test
    @Order(35)
    public void PR35() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.idIsPresentOnPage(driver, "widget-friends");

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        //Hacemos una búsqueda con el nombre del amigo
        WebElement text = driver.findElement(By.id("filter-by-name"));
        text.click();
        text.clear();
        text.sendKeys("Lucas");

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        SeleniumUtils.textIsPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "Lucas");
        SeleniumUtils.textIsPresentOnPage(driver, "Preso");
        SeleniumUtils.textIsNotPresentOnPage(driver, "pruebaTestReal@email.es");
        SeleniumUtils.textIsNotPresentOnPage(driver, "Ave");
        SeleniumUtils.textIsNotPresentOnPage(driver, "Maria");
        SeleniumUtils.textIsNotPresentOnPage(driver, "user10@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver, "Carla");
        SeleniumUtils.textIsNotPresentOnPage(driver, "Garcia");

    }

    /**
     * [Prueba36] Acceder a la lista de mensajes de un amigo, la lista debe contener al menos tres mensajes.
     */
    @Test
    @Order(36)
    public void PR36() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.idIsPresentOnPage(driver, "widget-friends");

        SeleniumUtils.waitLoadElementsBy(driver, "id", "626b9b0958be38a6ca85cdbdmessage", PO_View.getTimeout());

        //Accedemos al chat de un amigo
        WebElement text = driver.findElement(By.id("626b9b0958be38a6ca85cdbdmessage"));
        text.click();

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        SeleniumUtils.textIsPresentOnPage(driver, "que tal el dia");
        SeleniumUtils.textIsPresentOnPage(driver, "Bien!, y el tuyo?");
        SeleniumUtils.textIsPresentOnPage(driver, "Tuve un mal día :(");

    }

    /**
     * [Prueba38] Identificarse en la aplicación y enviar un mensaje a un amigo. Validar que el mensaje enviado
     *     aparece en el chat. Identificarse después con el usuario que recibió el mensaje y validar que tiene un
     *     mensaje sin leer. Entrar en el chat y comprobar que el mensaje pasa a tener el estado leído.
     */
    @Test
    @Order(38)
    public void PR38() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        SeleniumUtils.waitLoadElementsBy(driver, "id", "626b9b0958be38a6ca85cdbdmessage", PO_View.getTimeout());

        //Accedemos al chat de un amigo
        WebElement text = driver.findElement(By.id("626b9b0958be38a6ca85cdbdmessage"));
        text.click();

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        //Enviar mensaje
        PO_PrivateView.fillSendMessage(driver,"Holaa!");

        //Comprobar que aparece en el chat
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Holaa!", PO_View.getTimeout());
        SeleniumUtils.textIsPresentOnPage(driver, "Holaa!");

        //Identificarse con el otro usuario
        driver.navigate().to(URL+"/apiclient/client.html?w=login");

        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");

        SeleniumUtils.waitLoadElementsBy(driver, "id", "626b9f4d58be38a6ca85cdbemessage", PO_View.getTimeout());

        //Accedemos al chat del amigo
        text = driver.findElement(By.id("626b9f4d58be38a6ca85cdbemessage"));
        text.click();

        SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        //Comprobar que el mensaje pasa a estar leído
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", "Holaa!", PO_View.getTimeout());

        Assertions.assertTrue(elements.get(0).getText().contains("leído"));

    }


/**
    //[Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema
    @Test
    @Order(11)
    public void PR11() {
        //Logueo como administrador
        PO_LoginView.login(driver, "admin@email.com", "admin");

        //Accedemos a la lista de usuarios
        PO_PrivateView.listUsers(driver);

        //Comprobamos que lista todos los usuarios del sistema
        List<User> lista = usersService.getUsers();
        String checkText;
        List<WebElement> result;

        for (int i = 0; i < lista.size(); i++) {
            checkText = lista.get(i).getEmail();
            result = PO_View.checkElementBy(driver, "text", checkText);
            Assertions.assertEquals(checkText, result.get(0).getText());
        }

    }

    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(12)
    public void PR12() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        elementos.get(0).findElement(By.id("selected")).click();
        driver.findElement(By.id("deleteButton")).click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "user01@email.com");

    }

    //[Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(13)
    public void PR13() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        WebElement checkbox = elementos.get(elementos.size() - 2).findElement(By.id("selected"));
        checkbox.click();

        WebElement btnBorrar = driver.findElement(By.id("deleteButton"));
        btnBorrar.click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "user15@email.com");
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos usuarios
    //desaparecen.
    @Test
    @Order(14)
    public void PR14() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());

        elementos.get(0).findElement(By.id("selected")).click();
        elementos.get(1).findElement(By.id("selected")).click();
        elementos.get(2).findElement(By.id("selected")).click();

        driver.findElement(By.id("deleteButton")).click();
        SeleniumUtils.textIsNotPresentOnPage(driver, "user01@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsNotPresentOnPage(driver, "user03@email.com");

    }
/**
    //[Prueba15] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema,
    //excepto el propio usuario y aquellos que sean Administradores
    @Test
    @Order(15)
    public void PR15() {
        //Logueo como usuario estándar
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Accedemos a la lista de usuarios
        PO_PrivateView.listUsers(driver);

        //Comprobamos que lista todos los usuarios del sistema menos los admin y él mismo
        User activeUser = usersService.getUserByEmail("user01@email.com");

        List<WebElement> result = PO_View.checkElementBy(driver, "class", "usersTable");
        String checkText;
        int pageSize = result.size();
        List<User> lista = usersService.getStandardUsers(activeUser);
        int size = lista.size();
        int numPags = size / pageSize;
        int usersInLastPage = size % pageSize;
        for (int i = 0; i < numPags; i++) {
            PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", i + 1);
            for (int j = 0; j < pageSize; j++) {
                checkText = lista.get(i * pageSize + j).getEmail();
                result = PO_View.checkElementBy(driver, "text", checkText);
                Assertions.assertEquals(checkText, result.get(0).getText());
            }
            //Por cada página compruebo que no esté el propio usuario ni el admin
            SeleniumUtils.textIsNotPresentOnPage(driver, "admin@email.com");
            SeleniumUtils.textIsNotPresentOnPage(driver, "user01@email.com");
        }
        //Nos movemos a la última página
        PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", numPags + 1);
        for (int j = 0; j < usersInLastPage; j++) {
            checkText = lista.get(numPags * pageSize + j).getEmail();
            result = PO_View.checkElementBy(driver, "text", checkText);
            Assertions.assertEquals(checkText, result.get(0).getText());
            //Por cada página compruebo que no esté el propio usuario ni el admin
            SeleniumUtils.textIsNotPresentOnPage(driver, "admin@email.com");
            SeleniumUtils.textIsNotPresentOnPage(driver, "user01@email.com");
        }
    }

    //[Prueba16] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
    //corresponde con el listado usuarios existentes en el sistema.
    @Test
    @Order(16)
    public void PR16() {
        //Logueo como usuario estándar
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Accedemos a la lista de usuarios
        PO_PrivateView.listUsers(driver);

        //Hacemos una búsqueda con el campo vacío
        PO_PrivateView.fillSearch(driver, "");

        //Comprobamos que lista todos los usuarios del sistema menos los admin y él mismo
        User activeUser = usersService.getUserByEmail("user01@email.com");

        List<WebElement> result = PO_View.checkElementBy(driver, "class", "usersTable");
        String checkText;
        int pageSize = result.size();
        List<User> lista = usersService.getStandardUsers(activeUser);
        int size = lista.size();
        int numPags = size / pageSize;
        int usersInLastPage = size % pageSize;
        for (int i = 0; i < numPags; i++) {
            PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", i + 1);
            for (int j = 0; j < pageSize; j++) {
                checkText = lista.get(i * pageSize + j).getEmail();
                result = PO_View.checkElementBy(driver, "text", checkText);
                Assertions.assertEquals(checkText, result.get(0).getText());
            }
        }
        //Nos movemos a la última página
        PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", numPags + 1);
        for (int j = 0; j < usersInLastPage; j++) {
            checkText = lista.get(numPags * pageSize + j).getEmail();
            result = PO_View.checkElementBy(driver, "text", checkText);
            Assertions.assertEquals(checkText, result.get(0).getText());
        }
    }

    //[Prueba17] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
    //muestra la página que corresponde, con la lista de usuarios vacía.
    @Test
    @Order(17)
    public void PR17() {
        //Logueo como usuario estándar
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Accedemos a la lista de usuarios
        PO_PrivateView.listUsers(driver);

        //Hacemos una búsqueda escribiendo un texto que no exista
        PO_PrivateView.fillSearch(driver, "hola");

        //Comprobamos que aparece la lista de usuarios vacía
        List<WebElement> result = PO_View.checkElementBy(driver, "id", "tbody");

        int numUsers = result.get(0).getSize().getHeight();
        Assertions.assertEquals(0, numUsers);

    }

    //[Prueba18] Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
    //corresponde, con la lista de usuarios en los que el texto especificado sea parte de su nombre, apellidos o
    //de su email.
    @Test
    @Order(18)
    public void PR18() {
        //Logueo como usuario estándar
        PO_LoginView.login(driver, "user01@email.com", "user01");

        //Accedemos a la lista de usuarios
        PO_PrivateView.listUsers(driver);

        //Hacemos una búsqueda con un texto específico
        PO_PrivateView.fillSearch(driver, "n");

        //Comprobamos que lista todos los usuarios del sistema menos los admin y él mismo
        User activeUser = usersService.getUserByEmail("user01@email.com");

        List<WebElement> result = PO_View.checkElementBy(driver, "class", "usersTable");
        String checkText = "N";
        int pageSize = result.size();
        List<User> lista = usersService.getStandardUsers(activeUser);
        List<User> lista2 = new ArrayList<>();
        for (User user : lista) {
            if (user.getName().contains(checkText) || user.getEmail().contains(checkText)) {
                lista2.add(user);
            }
        }
        int size = lista2.size();
        int numPags = size / pageSize;
        int usersInLastPage = size % pageSize;
        for (int i = 0; i < numPags; i++) {
            PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", i + 1);
            for (int j = 0; j < pageSize; j++) {
                checkText = lista2.get(i * pageSize + j).getEmail();
                result = PO_View.checkElementBy(driver, "text", checkText);
                Assertions.assertEquals(checkText, result.get(0).getText());
            }
        }
        //Nos movemos a la última página
        PO_PrivateView.clickOn(driver, "//a[contains(@class, 'page-link')]", numPags + 1);
        for (int j = 0; j < usersInLastPage; j++) {
            checkText = lista2.get(numPags * pageSize + j).getEmail();
            result = PO_View.checkElementBy(driver, "text", checkText);
            Assertions.assertEquals(checkText, result.get(0).getText());
        }

    }

    @Test
    @Order(19)
    public void PR19() {
        //Vamos al formulario de logueo
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user08@email.com", "user08");

        //Hacemos una búsqueda para el usuario 04
        PO_PrivateView.fillSearch(driver, "user04");
        Long id = usersService.getUserByEmail("user04@email.com").getId();
        PO_PrivateView.enviarAceptarPeticion(driver, "AceptButton" + id);

        PO_PrivateView.logout(driver);

        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        //Esperamos a que aparezca la opción de añadir nota: //a[contains(@href, 'mark/add')]
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/invitation')]", 0);

        String checkText = "user08@email.com";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        PO_PrivateView.logout(driver);
    }

    // PR20. Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario al
    //que ya le habíamos enviado la invitación previamente.
    @Test
    @Order(20)
    public void PR20() {
        //Volvemos a enviar una petición de amistad desde el usuario 8
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user03@email.com", "user03");

        //Hacemos una búsqueda para el usuario 04
        PO_PrivateView.fillSearch(driver, "user05");
        //Comprobamos que el botón de enviar invitación no está disponible
        SeleniumUtils.textIsNotPresentOnPage(driver, PO_Properties.getString("button.send", 0));
        PO_PrivateView.logout(driver);
    }

    // PR21. Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un listado que contenga varias invitaciones recibidas.
    @Test
    @Order(21)
    public void PR21() {
        //Le enviamos otra petición de amistad al user05, que ya tenía una del user01.
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user05@email.com", "user05");

        // Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/invitation')]", 0);

        // Se comprueba que user05 tiene un nuevo amigo
        SeleniumUtils.textIsPresentOnPage(driver, "user04@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user02@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user03@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user08@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user07@email.com");
        PO_PrivateView.logout(driver);

    }

    // PR22. Sobre el listado de invitaciones recibidas. Hacer clic en el botón/enlace de una de ellas y comprobar que dicha solicitud desaparece del listado de invitaciones.
    @Test
    @Order(22)
    public void PR22() {
        //Nos loggeamos con el user06, que tiene 2 invitaciones pendientes.
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user05@email.com", "user05");

        // Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/invitation')]", 0);

        Long id = usersService.getUserByEmail("user03@email.com").getId();
        PO_PrivateView.click(driver, "//button[contains(@id, 'AceptButton" + id + "')]", 0);

        // Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/invitation')]", 0);

        // Se comprueba que user06 tiene 4 peticion de amistad
        List<WebElement> usuarios = PO_PrivateView.checkElementBy(driver, "text", "Aceptar");
        Assertions.assertEquals(4, usuarios.size());
        PO_PrivateView.logout(driver);
    }

    // PR23. Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene los amigos que deben ser
    @Test
    @Order(23)
    public void PR23() {
        //Nos loggeamos con el user04, que tiene 1 amigo (user08)
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Se despliega el menú de amigos, y se clica en lista de amigos
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/list')]", 0);

        // Se comprueba que user04 tiene un nuevo amigo
        SeleniumUtils.textIsPresentOnPage(driver, "user06@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user03@email.com");
        SeleniumUtils.textIsPresentOnPage(driver, "user04@email.com");
        PO_PrivateView.logout(driver);
    }

    // PR24. Crear una publicación y comprobar que se ha creado correctamente
    @Test
    @Order(24)
    public void PR24() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Entramos en la ventana de creación
        PO_NavView.desplegarPublicaciones(driver, "addPublication");

        // Añadimos un título
        List<WebElement> title = SeleniumUtils.waitLoadElementsBy(driver, "id", "title", PO_View.getTimeout());
        title.get(0).sendKeys("Prueba título");

        // Añadimos el contenido
        List<WebElement> content = PO_View.checkElementBy(driver, "id", "text");
        content.get(0).sendKeys("Prueba contenido");

        // Confirmamos
        driver.findElement(By.id("post")).click();

        // clickamos la opción de Publicaciones
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Ultima", PO_View.getTimeout()).get(0).click();

        List<WebElement> nuevaPublicacion = SeleniumUtils.waitLoadElementsBy(driver, "text", "Prueba título", PO_View.getTimeout());

        Assertions.assertTrue(nuevaPublicacion.get(0) != null);
    }

    // PR25. Probar que si se intenta crear una publicación sin título, la aplicación no lo permite
    @Test
    @Order(25)
    public void PR25() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Entramos en la ventana de creación
        PO_NavView.desplegarPublicaciones(driver, "addPublication");

        // Añadimos el contenido
        List<WebElement> content = PO_View.checkElementBy(driver, "id", "text");
        content.get(0).sendKeys("Prueba contenido");

        // Confirmamos
        driver.findElement(By.id("post")).click();

        // Comprobamos que no ha pasado a ninguna otra ventana
        List<WebElement> paginaAgregar = SeleniumUtils.waitLoadElementsBy(driver, "id", "uploadPublication", PO_View.getTimeout());

        Assertions.assertTrue(paginaAgregar.get(0) != null);
    }

    // PR26. Probar que se muestran todas las publicaciones de un usuario
    @Test
    @Order(26)
    public void PR26() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Entramos en la ventana de creación
        PO_NavView.desplegarPublicaciones(driver, "listPublication");

        // Comprobamos que están todas las publicaciones
        List<WebElement> pub1 = SeleniumUtils.waitLoadElementsBy(driver, "text", "publicacion 1 de Andrea", PO_View.getTimeout());
        Assertions.assertTrue(pub1.get(0) != null);

        List<WebElement> pub2 = SeleniumUtils.waitLoadElementsBy(driver, "text", "publicacion 2 de Andrea", PO_View.getTimeout());
        Assertions.assertTrue(pub2.get(0) != null);
    }

    // PR27. Probar que se muestran todas las publicaciones de un amigo
    @Test
    @Order(27)
    public void PR27() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user06@email.com", "user06");

        // Entramos en la ventana de amigos
        PO_NavView.desplegarAmigos(driver, "listFriends");

        SeleniumUtils.waitLoadElementsBy(driver, "text", "Ver publicaciones", PO_View.getTimeout()).get(0).click();

        // Comprobamos que están todas las publicaciones
        List<WebElement> pub1 = SeleniumUtils.waitLoadElementsBy(driver, "text", "publicacion 1 de Andrea", PO_View.getTimeout());
        Assertions.assertTrue(pub1.get(0) != null);

        List<WebElement> pub2 = SeleniumUtils.waitLoadElementsBy(driver, "text", "publicacion 2 de Andrea", PO_View.getTimeout());
        Assertions.assertTrue(pub2.get(0) != null);
    }

    // PR28. Probar que no se puede acceder a las publicaciones de un usuario por URL sin ser su amigo
    @Test
    @Order(28)
    public void PR28() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user02@email.com", "user02");

        driver.get("http://localhost:8090/publication/listFriend/user01@email.com");

        // Comprobamos que están todas las publicaciones
        List<WebElement> pub1 = SeleniumUtils.waitLoadElementsBy(driver, "text", "Error de autenticación: No eres amigo/a de este usuario", PO_View.getTimeout());
        Assertions.assertTrue(pub1.get(0) != null);

    }

    //PR29. Visualizar al menos cuatro páginas en español/inglés/español (comprobando que algunas de las etiquetas cambian al idioma correspondiente). Ejemplo, Página principal/Opciones Principales de
    //Usuario/Listado de Usuarios.
    @Test
    @Order(29)
    public void PR29() {

        //Welcome

        PO_HomeView.checkWelcomeToPage(driver, 0);

        PO_HomeView.changeLanguage(driver, "btnEnglish");

        PO_HomeView.checkWelcomeToPage(driver, 1);

        //Users

        //Nos loggeamos con el user01, que tiene 1 amigo (user06) con publicaciones
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        String checkText = PO_Properties.getString("user.list.message", 1);
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_HomeView.changeLanguage(driver, "btnSpanish");

        checkText = PO_Properties.getString("user.list.message", 0);
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        //Friends

        // Se despliega el menú de usuarios, y se clica en Ver peticiones de amistad
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/invitation')]", 0);

        checkText = PO_Properties.getString("friend.invitation.message", 0);
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_HomeView.changeLanguage(driver, "btnEnglish");

        checkText = PO_Properties.getString("friend.invitation.message", 1);
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        //Publication

        PO_PrivateView.click(driver, "//li[contains(@id, 'publications-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'publication/list')]", 0);

        checkText = PO_Properties.getString("publication.title", 1);
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_HomeView.changeLanguage(driver, "btnSpanish");

        checkText = PO_Properties.getString("publication.title", 0);
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

    }

    // PR30. Intentar acceder al listado de usuarios sin estar autenticado
    @Test
    @Order(30)
    public void PR30() {

        driver.get("http://localhost:8090/user/list");

        String searchText = "Identificate";
        List<WebElement> element = SeleniumUtils.waitLoadElementsBy(driver, "text", searchText, PO_View.getTimeout());
        Assertions.assertTrue(element.get(0).getText().equals(searchText));

    }

    // PR31. Intentar acceder al listado de invitaciones de amistad de un usuario sin estar autenticado
    @Test
    @Order(31)
    public void PR31() {

        driver.get("http://localhost:8090/friend/invitation");

        String searchText = "Identificate";
        List<WebElement> element = SeleniumUtils.waitLoadElementsBy(driver, "text", searchText, PO_View.getTimeout());
        Assertions.assertTrue(element.get(0).getText().equals(searchText));

    }

    // PR32. Intentar acceder a una acción de usuario admin estando autenticado como usuario estándar
    @Test
    @Order(32)
    public void PR32() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        driver.get("http://localhost:8090/logs/list");

        String searchText = "Error";
        List<WebElement> elements = SeleniumUtils.waitLoadElementsBy(driver, "text", searchText, PO_View.getTimeout());
        Assertions.assertTrue(!elements.isEmpty());

    }

    // PR33. Ver los logs como usuario admin. Al menos hay 2 de cada tipo
    @Test
    @Order(33)
    public void PR33() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
    }

    // PR34. Borrar logs como administrador
    @Test
    @Order(34)
    public void PR34() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
    }

    // PR35. Acceder a las publicaciones de un amigo y recomendar una publicación. Comprobar que el
    //número de recomendaciones se ha incrementado en uno y que no aparece el botón/enlace recomendar.
    @Test
    @Order(35)
    public void PR35() {
        //Nos loggeamos con el user01, que tiene 1 amigo (user06) con publicaciones
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Se despliega el menú de amigos, y se clica en lista de amigos
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/list')]", 0);

        //Pulsamos el enlace para ver las publicaciones del user06
        PO_PrivateView.click(driver, "//a[contains(@href, 'publication/listFriend/user06@email.com')]", 0);

        //Comprobamos que el título de la publicación es el que debería ser.
        String checkText = "Hola :P";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        //Pulsamos el enlace para ver las publicaciones del user06
        PO_PrivateView.click(driver, "//button[contains(@class, 'btn btn-success')]", 0);

        // Se despliega el menú de amigos, y se clica en lista de amigos
        PO_PrivateView.click(driver, "//li[contains(@id, 'friends-menu')]/a", 0);
        PO_PrivateView.click(driver, "//a[contains(@href, 'friend/list')]", 0);

        //Pulsamos el enlace para ver las publicaciones del user06
        PO_PrivateView.click(driver, "//a[contains(@href, 'publication/listFriend/user06@email.com')]", 0);

        //Comprobamos que el título de la publicación es el que debería ser.
        checkText = "Hola :P";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        //Comprobamos que el la publicación tiene 1 recomendación
        checkText = "1 Recs.";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        //Comprobamos que el boton ya no está y aparece el texto correspondiente
        checkText = "Ya has recomendado esta publicación";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

        PO_PrivateView.logout(driver);
    }

    // PR36. Utilizando un acceso vía URL u otra alternativa, tratar de recomendar una publicación de un
    //usuario con el que no se mantiene una relación de amistad.
    @Test
    @Order(36)
    public void PR36() {
        //Nos loggeamos con el user04, que no es amigo del user06
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        // Rellenamos el formulario de login con datos válidos
        PO_LoginView.fillLoginForm(driver, "user04@email.com", "user04");

        driver.get("http://localhost:8090/publication/listFriend/user06@email.com");

        //Comprobamos que no aparecen publicaciones de amigos
        String checkText = "No hay publicaciones";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());


        PO_PrivateView.logout(driver);
    }

    //[Prueba37] Como administrador, cambiar el estado de una publicación y comprobar que el estado ha
    //cambiado.
    @Test
    @Order(37)
    public void PR37() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //PO_HomeView.clickOption(driver, "/publication/list", "class", "btn btn-primary");
        driver.get("http://localhost:8090/publication/list");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Long id = publicationsService.getPublications().get(0).getId();
        elementos.get(0).findElement(By.id("censurada" + id)).click();

    }

    //[Prueba38] Como usuario estándar, comprobar que no aparece en el listado propio de publicaciones una
    //publicación censurada.
    @Test
    @Order(38)
    public void PR38() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //PO_HomeView.clickOption(driver, "/publication/list", "class", "btn btn-primary");
        driver.get("http://localhost:8090/publication/list");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        Long id = publicationsService.getPublications().get(0).getId();
        WebElement elem = elementos.get(0).findElement(By.id("censurada" + id));
        elem.click();
        //Despues de crearlo nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Pulsamos el enlace para ver las publicaciones del user01
        driver.get("http://localhost:8090/publication/list");
        SeleniumUtils.textIsNotPresentOnPage(driver, "publicacion 1 de Andrea");

    }

    //[Prueba39] Como usuario estándar, comprobar que, en el listado de publicaciones de un amigo, no
    //aparece una publicación moderada.
    @Test
    @Order(39)
    public void PR39() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //PO_HomeView.clickOption(driver, "/publication/list", "class", "btn btn-primary");
        driver.get("http://localhost:8090/publication/list");

        List<WebElement> elementos = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        Long id = publicationsService.getPublications().get(2).getId();
        WebElement elem = elementos.get(2).findElement(By.id("moderada" + id));
        elem.click();
        //Despues de crearlo nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Pulsamos el enlace para ver las publicaciones del user01
        driver.get("http://localhost:8090/publication/listFriend/user06@email.com");

    }

    @Test
    @Order(40)
    public void PR40() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "user05@email.com", "user05");
        driver.get("http://localhost:8090/publication/edit?id=20&state=Censurada");


        String checkText = "Publicaciones";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        SeleniumUtils.textIsNotPresentOnPage(driver, "Cambiar estado");
        SeleniumUtils.textIsNotPresentOnPage(driver, "Estado actual");

    }

    @Test
    @Order(41)
    public void PR41() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        driver.get("http://localhost:8090/publication/list");
        //Hacemos una búsqueda con el campo vacío
        PO_PrivateView.fillSearch(driver, "");

        String checkText = "Publicaciones";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());

    }

    //[Prueba42] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
    //muestra la página que corresponde, con la lista de publicaciones vacía.
    @Test
    @Order(42)
    public void PR42() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        driver.get("http://localhost:8090/publication/list");
        //Hacemos una búsqueda con el campo vacío
        PO_PrivateView.fillSearch(driver, "asd");


        String checkText = "Publicaciones";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        Assertions.assertEquals("http://localhost:8090/publication/list?searchText=asd", driver.getCurrentUrl());
    }

    //[Prueba43] Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
    //corresponde, con la lista de publicaciones en los que el texto especificado sea parte de título, estado o del
    //email.
    @Test
    @Order(43)
    public void PR43() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); // Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        driver.get("http://localhost:8090/publication/list");
        //Hacemos una búsqueda con el campo vacío
        PO_PrivateView.fillSearch(driver, "Aceptada");

        String checkText = "Publicaciones";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        Assertions.assertEquals("http://localhost:8090/publication/list?searchText=Aceptada", driver.getCurrentUrl());

    }

    // PR44. Probar que se puede crear una publicación con imagen
    @Test
    @Order(44)
    public void PR44() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        PO_NavView.desplegarPublicaciones(driver, "addPublication");

        // Añadimos un título
        List<WebElement> title = SeleniumUtils.waitLoadElementsBy(driver, "id", "title", PO_View.getTimeout());
        title.get(0).sendKeys("Prueba título");

        // Añadimos el contenido
        List<WebElement> content = PO_View.checkElementBy(driver, "id", "text");
        content.get(0).sendKeys("Prueba contenido");

        // Añadimos la imagen
        WebElement uploadElement = driver.findElement(By.id("file"));
        uploadElement.sendKeys("C:\\Productos\\Prueba.png");

        // Confirmamos
        driver.findElement(By.id("post")).click();

        // clickamos la opción de Publicaciones
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Ultima", PO_View.getTimeout()).get(0).click();

        List<WebElement> elements = PO_View.checkElementBy(driver, "class", "img.thumbnail rounded float-left");
        Assertions.assertTrue(elements.size() == 1);
    }

    // PR45. Probar que se puede crear una publicación sin imagen
    @Test
    @Order(45)
    public void PR45() {
        PO_NavView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        // Entramos en la ventana de creación
        PO_NavView.desplegarPublicaciones(driver, "addPublication");

        // Añadimos un título
        List<WebElement> title = SeleniumUtils.waitLoadElementsBy(driver, "id", "title", PO_View.getTimeout());
        title.get(0).sendKeys("Prueba título");

        // Añadimos el contenido
        List<WebElement> content = PO_View.checkElementBy(driver, "id", "text");
        content.get(0).sendKeys("Prueba contenido");

        // Confirmamos
        driver.findElement(By.id("post")).click();

        // clickamos la opción de Publicaciones
        SeleniumUtils.waitLoadElementsBy(driver, "text", "Ultima", PO_View.getTimeout()).get(0).click();

        List<WebElement> nuevaPublicacion = SeleniumUtils.waitLoadElementsBy(driver, "text", "Prueba título", PO_View.getTimeout());

        Assertions.assertTrue(nuevaPublicacion.get(0) != null);
    }
*/
}

