package com.uniovi.sdientrega132.pageobjects;

import com.uniovi.sdientrega132.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    static public void listUsers(WebDriver driver) {
        //Pinchamos en la opción de lista de notas.
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'user/list')]");
        elements.get(0).click();
        //Esperamos a que se muestren los enlaces de paginación la lista de notas
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
    }

    static public void enterToMenu(WebDriver driver, String menu) {
        //Pinchamos en la opción de menú de Notas: //li[contains(@id, 'marks-menu')]/a
        List<WebElement> elements = PO_View.checkElementBy(driver, "id", menu);
        elements.get(0).click();
    }

    static public void clickOn(WebDriver driver, String contenido, int index) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", contenido);
        elements.get(index).click();
    }

    static public void login(WebDriver driver, String dnip, String passwordp) {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary"); //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, dnip, passwordp); //Comprobamos que entramos en la pagina privada de Alumno
    }

    static public void clickCheck(WebDriver driver, String text, int pos) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", text);
        Assertions.assertEquals(text, elements.get(pos).getText());
    }

    static public void logout(WebDriver driver) {
        String loginText = PO_HomeView.getP().getString("signup.message", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    static public void enviarAceptarPeticion(WebDriver driver, String usuario)
    {
        By boton = By.id( usuario );
        driver.findElement(boton).click();
    }

}
