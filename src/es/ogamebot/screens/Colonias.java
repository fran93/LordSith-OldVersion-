package es.ogamebot.screens;

import es.ogamebot.utils.ReadDB;
import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

/**
 *
 * @author Fran1488
 */
public class Colonias {
    final private WebClient webClient;
    private HtmlPage page;
    private int actuales, maximas;
    private final String SHIPS_TO_SEND;
    
        public Colonias(WebClient webClient, ReadDB properties, String cp) throws IOException{
            this.webClient=webClient;     
            //obteniendo los datos
            page = this.webClient.getPage("http://s"+Utils.getUniverso()+"-es.ogame.gameforge.com/game/index.php?page=fleet1&galaxy=2&system=106&position=6&type=1&mission=3&cp="+cp);
            //obteniendo las flotas
            HtmlDivision slots = (HtmlDivision) page.getElementById("slots");
            //obteniendo div de las flotas
            HtmlDivision exp = (HtmlDivision) slots.getElementsByTagName("div").get(0);
            //obtengo el string actual y el máximo
            String[] dummy = exp.asText().split("/");
            actuales = Integer.parseInt(dummy[0].replaceAll("[\\D]", ""));
            maximas = Integer.parseInt(dummy[1].replaceAll("[\\D]", ""));
            //cantidad de naves 
            SHIPS_TO_SEND = properties.getColonialBigCargo();
    }
        
        
     /**
     * Devuelve si hay algún hueco libre
     *
     * @return
     */
    public boolean isSlotFree() {
        return maximas > actuales;
    }
    
    
    public void enviarRecursos() throws IOException {
        try {
            //añadir cargueras grandes
            HtmlInput bigShipInput = (HtmlInput) page.getElementById("ship_203");
            bigShipInput.setValueAttribute(SHIPS_TO_SEND);
            //Primera pantalla
            webClient.waitForBackgroundJavaScript(5000);
            ScriptResult result = page.executeJavaScript("trySubmit();");
            page = (HtmlPage) result.getNewPage();
            //Primera pantalla(Por alguna razón hay que llamar a la función dos veces para que funcione)
            webClient.waitForBackgroundJavaScript(5000);
            result = page.executeJavaScript("trySubmit();");
            page = (HtmlPage) result.getNewPage();
            //Segunda pantalla
            webClient.waitForBackgroundJavaScript(5000);
            result = page.executeJavaScript("trySubmit();");
            page = (HtmlPage) result.getNewPage();
            //Tercera Pantalla
            webClient.waitForBackgroundJavaScript(5000);
            //cogemos todos los recursos
            page.executeJavaScript("maxAll();updateVariables();");
            result = page.executeJavaScript("trySubmit();");
            page = (HtmlPage) result.getNewPage();

            Utils.printLog("Recursos enviados al planeta principal desde la colonia");

        } catch (NullPointerException ex) {
            Utils.printLog("Mi emperador, la expedición no pudo ser enviada por causa de problemas técnicos.");
        }
    }
}
