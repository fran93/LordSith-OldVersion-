package es.ogamebot.screens;

import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

/**
 *
 * @author Fran1488
 */
public class Defensa {

    final private WebClient webClient;
    private HtmlPage page;
    private final String cp;

    public Defensa(WebClient webClient, String cp) {
        this.webClient = webClient;
        this.cp = cp;
    }
    
    public void buildLanzamisiles() throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=defense&cp=" + cp);

        try {
            //abrir la ventana para introducir la cantidad
            HtmlAnchor laserButton = (HtmlAnchor) page.getElementById("details401");
            webClient.waitForBackgroundJavaScript(5000);
            page = laserButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            //obtener la caja de texto
            HtmlInput caja = (HtmlInput) page.getElementById("number");
            caja.setValueAttribute("9999");
            //obtener el botón en cola 
            HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
            build.click();
        } catch (ElementNotFoundException ex) {
            Utils.printLog("Mi emperador, no ha sido posible la construcción de lanzamisiles. " + ex);
        }
    }

    public void buildSmallLaser(int cantidad) throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=defense&cp=" + cp);

        try {
            //abrir la ventana para introducir la cantidad
            HtmlAnchor laserButton = (HtmlAnchor) page.getElementById("details402");
            webClient.waitForBackgroundJavaScript(5000);
            page = laserButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            //obtener la caja de texto
            HtmlInput caja = (HtmlInput) page.getElementById("number");
            caja.setValueAttribute(String.valueOf(cantidad));
            //obtener el botón en cola 
            HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
            build.click();
        } catch (ElementNotFoundException ex) {
            Utils.printLog("Mi emperador, no ha sido posible la construcción de lásers pequeños. " + ex);
        }
    }

    public void buildGauss() throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=defense&cp=" + cp);

        try {
            //abrir la ventana para introducir la cantidad
            HtmlAnchor gaussButton = (HtmlAnchor) page.getElementById("details404");
            webClient.waitForBackgroundJavaScript(5000);
            page = gaussButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            //obtener la caja de texto
            HtmlInput caja = (HtmlInput) page.getElementById("number");
            caja.setValueAttribute("9999");
            //obtener el botón en cola 
            HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
            build.click();
        } catch (ElementNotFoundException ex) {
            Utils.printLog("Mi emperador, no ha sido posible la construcción de cañones gauss. " + ex);
        }
    }
    
    public void buildIonico() throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=defense&cp=" + cp);

        try {
            //abrir la ventana para introducir la cantidad
            HtmlAnchor gaussButton = (HtmlAnchor) page.getElementById("details405");
            webClient.waitForBackgroundJavaScript(5000);
            page = gaussButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            //obtener la caja de texto
            HtmlInput caja = (HtmlInput) page.getElementById("number");
            caja.setValueAttribute("9999");
            //obtener el botón en cola 
            HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
            build.click();
        } catch (ElementNotFoundException ex) {
            Utils.printLog("Mi emperador, no ha sido posible la construcción de iónicos. " + ex);
        }
    }

    public void buildPlasma() throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=defense&cp=" + cp);

        try {
            //abrir la ventana para introducir la cantidad
            HtmlAnchor gaussButton = (HtmlAnchor) page.getElementById("details406");
            webClient.waitForBackgroundJavaScript(5000);
            page = gaussButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            //obtener la caja de texto
            HtmlInput caja = (HtmlInput) page.getElementById("number");
            caja.setValueAttribute("9999");
            //obtener el botón en cola 
            HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
            build.click();
        } catch (ElementNotFoundException ex) {
            Utils.printLog("Mi emperador, no ha sido posible la construcción de plasmas. " + ex);
        }
    }
    
    public void close(){
       page.cleanUp();
       page.remove();
   }
}
