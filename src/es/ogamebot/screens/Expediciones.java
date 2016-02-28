package es.ogamebot.screens;

import es.ogamebot.utils.ReadDB;
import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import es.ogamebot.database.ManageDB;
import es.ogamebot.database.SelectSQL;
import es.ogamebot.database.UpdateSQL;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    public Expediciones(WebClient webClient, ReadDB properties, String cp) throws IOException {
        this.webClient = webClient;
        //obteniendo datos..
        page = this.webClient.getPage("http://s"+Utils.getUniverso()+"-es.ogame.gameforge.com/game/index.php?page=fleet1&cp="+cp);
        //obteniendo las flotas
        HtmlDivision slots = (HtmlDivision) page.getElementById("slots");
        //obteniendo div de las expediciones
        HtmlDivision exp = (HtmlDivision) slots.getElementsByTagName("div").get(1);
        //En caso de que esté activado el modo comandante, tenemos que escoger el siguiente div
        if(!exp.getAttribute("class").equals("fleft")){
            exp = (HtmlDivision) slots.getElementsByTagName("div").get(2);
        }
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
            //las expediciones serán en el sistema 104 - 109
            if (sistema == 109) {
                sistema = 104;
            }
            //página que te selecciona automáticamente la misión expedición
            page = webClient.getPage("http://s"+Utils.getUniverso()+"-es.ogame.gameforge.com/game/index.php?page=fleet1&mission=15&position=16&galaxy=2&system="+sistema+"&type=1");
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
            Utils.printLog("Expedición enviada al sistema "+sistema+".");
            //la siguiente expedición será enviada a otro sistema
            sistema++;
            setExpPosition(sistema);
        } catch (NullPointerException ex) {
            Utils.printLog("Mi emperador, la expedición no pudo ser enviada por causa de problemas técnicos.");
        }
    }
        
    private int getExpPosition() {
        try{
        ManageDB database = new ManageDB();
        ResultSet rs = database.ejecutarSQLSelect(SelectSQL.GET_EXPEDICION_POSITION);
        if(rs.next()){
            return rs.getInt("posicion");
        }else{
            return 0;
        }
        }catch(SQLException ex){
            return 0;
        }
    }
    
    private void setExpPosition(int index){
        ManageDB database = new ManageDB();
        UpdateSQL update = new UpdateSQL("posiciones", new String[]{"posicion="+index}, new String[]{"id=2"});
        database.ejecutarSQL(update.getSql());
    }

}
