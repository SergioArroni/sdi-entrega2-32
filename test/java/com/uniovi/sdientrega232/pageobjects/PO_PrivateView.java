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
        enterToMenu(driver, "users-menu");
        //Pinchamos en la opción de lista de notas.
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'user/list')]");
        elements.get(0).click();
    }

    static public void enterToMenu(WebDriver driver, String menu) {
        //Pinchamos en la opción de menú de Notas: //li[contains(@id, 'users-menu')]/a
        List<WebElement> elements = PO_View.checkElementBy(driver, "id", menu);
        elements.get(0).click();
    }

    static public void clickOn(WebDriver driver, String contenido, int index) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", contenido);
        elements.get(index).click();
    }

    static public List<WebElement> click(WebDriver driver, String click, int lugar){
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", click);
        elements.get(lugar).click();
        return elements;
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

    static public void fillSearch(WebDriver driver, String textp) {
        WebElement text = driver.findElement(By.name("searchText"));
        text.click();
        text.clear();
        text.sendKeys(textp);
        //Pulsar el boton de Buscar.
        By boton = By.className("btn"); 
        driver.findElement(boton).click();
    }

}
