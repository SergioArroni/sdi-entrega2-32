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

    @Test
    @Order(37)
    public void PR37() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> elements = PO_View.checkElementBy(driver, "text", "Mensaje de 1 a 4");
        elements.get(0).click();

        WebElement nuevo = driver.findElement(By.name("texto"));
        nuevo.sendKeys("Mensaje de 1 a 4");
        nuevo.click();

        List<WebElement> elements2 = PO_View.checkElementBy(driver, "text", "Mensaje de 1 a 4");
        Assertions.assertTrue(elements2.size()>0);

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

    @Test
    @Order(39)
    public void PR39() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> elements = PO_View.checkElementBy(driver, "text", "Mensaje de 1 a 10");
        elements.get(0).click();

        WebElement nuevo = driver.findElement(By.name("texto"));
        nuevo.sendKeys("Mensaje de 1 a 10");
        nuevo.click();

        driver.navigate().to(URL+"/apiclient/client.html?w=login");
        PO_LoginView.fillLoginForm(driver, "user10@email.com", "user10");

        List<WebElement> notSeen = PO_View.checkElementBy(driver, "text", "1");
        Assertions.assertTrue(notSeen.size()>0);

    }

    @Test
    @Order(40)
    public void PR40() {
        driver.navigate().to(URL+"/apiclient/client.html");

        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");

        List<WebElement> elements = PO_View.checkElementBy(driver, "text", "Mensaje de 1 a 10");
        elements.get(0).click();

        WebElement nuevo = driver.findElement(By.name("texto"));
        nuevo.sendKeys("Mensaje de 1 a 10");
        nuevo.click();

        driver.navigate().to(URL+"/apiclient/client.html?w=friends");

        List<WebElement> elements2 = PO_View.checkElementBy(driver, "text", "Mensaje de 1 a 10");
        Assertions.assertTrue(elements2.size()>0);

    }

}

