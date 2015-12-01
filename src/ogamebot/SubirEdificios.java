package ogamebot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

/**
 *
 * @author Fran1488
 */
public class SubirEdificios {

    final private WebClient webClient;
    private HtmlPage page;
    private final int tipo;
    private final String cp;
    HtmlListItem edificio;
    HtmlDivision supply, building;
    HtmlAnchor mejorar;

    /**
     * 0 Mina de metal 1 Mina de cristal 2 Sintetizador de deuterio 3 Planta de
     * energía solar
     *
     * @param webClient
     * @param tipo
     * @param cp
     */
    public SubirEdificios(WebClient webClient, int tipo, String cp) {
        this.webClient = webClient;
        this.tipo = tipo;
        this.cp = cp;
    }

    public void subir() throws IOException, InterruptedException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=resources&cp="+cp);

        switch (tipo) {
            case 0:
                //subir un nivel la mina de metal
                edificio = (HtmlListItem) page.getElementById("button1");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(0);
                mejorar.click();
                break;
            case 1:
                //subir un nivel la mina de cristal
                edificio = (HtmlListItem) page.getElementById("button2");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(0);
                mejorar.click();

                break;
            case 2:
                //subir un nivel el sintetizador de deuterio
                edificio = (HtmlListItem) page.getElementById("button3");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(0);
                mejorar.click();
                break;
            case 3:
                //subir un nivel la planta de energía solar
                edificio = (HtmlListItem) page.getElementById("button4");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(0);
                mejorar.click();
                break;
            case 4:
                //subir un nivel el almacén de metal
                edificio = (HtmlListItem) page.getElementById("button7");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(building.getElementsByTagName("a").getLength()-1);
                mejorar.click();
                break;
            case 5:
                //subir un nivel el almacén de cristal
                edificio = (HtmlListItem) page.getElementById("button8");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(building.getElementsByTagName("a").getLength()-1);
                mejorar.click();
                break;
            case 6:
                //subir un nivel el sintetizador de deuterio
                edificio = (HtmlListItem) page.getElementById("button9");
                supply = (HtmlDivision) edificio.getElementsByTagName("div").get(0);
                building = (HtmlDivision) supply.getElementsByTagName("div").get(0);
                mejorar = (HtmlAnchor) building.getElementsByTagName("a").get(building.getElementsByTagName("a").getLength()-1);
                mejorar.click();
                break;   
            case 7:
                //ir al Hangar 
                page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=shipyard&cp="+cp);
                //abrir la ventana para introducir la cantidad
                HtmlAnchor satelite = (HtmlAnchor) page.getElementById("details212");
                webClient.waitForBackgroundJavaScript(5000);
                page = satelite.click();
                webClient.waitForBackgroundJavaScript(5000);
                //obtener la caja de texto
                HtmlInput caja = (HtmlInput) page.getElementById("number");
                caja.setValueAttribute("5");
                //obtener el botón en cola 
                HtmlAnchor build = (HtmlAnchor) page.getFirstByXPath("//a[@class='build-it']");
                build.click();
                break;
        }
        
        page.cleanUp();
        page.remove();
    }
}
