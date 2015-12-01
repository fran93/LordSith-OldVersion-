package ogamebot;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Fran1488
 */
public class Expediciones {

    final private WebClient webClient;
    private HtmlPage page;
    private final int actuales, maximas, bigShip, naveExpedicion;
    private int sistema;
    private final String bigCargo;


    public Expediciones(WebClient webClient, ReadProperties properties, String cp) throws IOException {
        this.webClient = webClient;
        //obteniendo datos..
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=fleet1&cp="+cp);
        //obteniendo las flotas
        HtmlDivision slots = (HtmlDivision) page.getElementById("slots");
        //obteniendo div de las expediciones
        HtmlDivision exp = (HtmlDivision) slots.getElementsByTagName("div").get(1);
        //obtengo el string actual y el máximo
        String[] dummy = exp.asText().split("/");
        actuales = Integer.parseInt(dummy[0].replaceAll("[\\D]", ""));
        maximas = Integer.parseInt(dummy[1].replaceAll("[\\D]", ""));
        //obtengo la cantidad de naves grandes de carga
        bigShip = Integer.parseInt(page.getElementById("button203").asText().replaceAll("[\\D]", ""));
        //obtengo las propiedades
        naveExpedicion = properties.getNaveExpedicion();
        bigCargo = properties.getBigCargo();
        //obtener la posición de la última expedición
        sistema = getExpPosition();
    }

    /**
     * Devuelve si hay algún hueco libre
     *
     * @return
     */
    public boolean isSlotFree() {
        return maximas > actuales;
    }

    public boolean navesDisponibles() {
        return bigShip >= 5;
    }

    public void enviarExpedicion() throws IOException {
        try {
            //las expediciones serán en el sistema 105 106 107 
            if (sistema == 108) {
                sistema = 105;
            }
            //página que te selecciona automáticamente la misión expedición
            page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=fleet1&mission=15&position=16&galaxy=2&system="+sistema+"&type=1");
            //añadir cargueras grandes
            HtmlInput bigShipInput = (HtmlInput) page.getElementById("ship_203");
            bigShipInput.setValueAttribute(bigCargo);
            //añadir 1 destructor
            HtmlInput destroyer = (HtmlInput) page.getElementById("ship_"+naveExpedicion);
            destroyer.setValueAttribute("1");
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
            result = page.executeJavaScript("trySubmit();");
            page = (HtmlPage) result.getNewPage();

            System.out.println(Utils.getHour() + " - Expedición enviada al sistema "+sistema+".");
            //la siguiente expedición será enviada a otro sistema
            sistema++;
            setExpPosition(sistema);
        } catch (NullPointerException ex) {
            System.out.println(Utils.getHour() + " - Mi emperador, la expedición no pudo ser enviada por causa de problemas técnicos.");
        }
    }
    
    public void close(){
       page.cleanUp();
       page.remove();
   }
    
    private int getExpPosition() {
        try{
            FileInputStream in = new FileInputStream("expe.properties");
            Properties propiedades = new Properties();
            propiedades.load(in);
            return Integer.parseInt(propiedades.getProperty("expPosition"));
        }catch(IOException ex){
            return 105;
        }
    }
    
    private void setExpPosition(int index){
        try {
            FileOutputStream out = new FileOutputStream("expe.properties");
            Properties propiedades = new Properties();
            propiedades.setProperty("expPosition", String.valueOf(index));
            propiedades.store(out, null);          
        } catch (IOException ex) {
            System.out.println(Utils.getHour()+" - Fallo guardando la posición de la granja.");
        }
    }

}
