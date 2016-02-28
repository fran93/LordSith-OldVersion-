package es.ogamebot.screens;

import es.ogamebot.utils.ReadDB;
import es.ogamebot.model.Cordenadas;
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
import java.util.ArrayList;

/**
 *
 * @author Fran1488
 */
public class Ataque {
    final private WebClient webClient;
    private HtmlPage page;
    private int actuales, maximas, smallShip;
    private ArrayList<Cordenadas> cordenadas = new ArrayList<>();
    private  int index;
    private final String SHIPS_TO_SEND;
    
    public Ataque(WebClient webClient, ReadDB properties, String cp) throws IOException{
        this.webClient=webClient;
        
        //obteniendo los datos
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=fleet1&cp="+cp);
        //obteniendo las flotas
        HtmlDivision slots = (HtmlDivision) page.getElementById("slots");
        //obteniendo div de las flotas
        HtmlDivision exp = (HtmlDivision) slots.getElementsByTagName("div").get(0);
        //obtengo el string actual y el máximo
        String[] dummy = exp.asText().split("/");
        actuales = Integer.parseInt(dummy[0].replaceAll("[\\D]", ""));
        maximas = Integer.parseInt(dummy[1].replaceAll("[\\D]", ""));
        //obtengo la cantidad de naves pequeñas de carga
        smallShip = Integer.parseInt(page.getElementById("button202").asText().replaceAll("[\\D]", ""));
        //cantidad de naves 
        SHIPS_TO_SEND = properties.getSmallCargo();
        //obtener la posición actual de la lista de granjas
        index = getFarmPosition();
    }
    
    /**
     * Devuelve si hay algún hueco libre
     *
     * @return
     */
    public boolean isSlotFree() {
        return maximas > actuales;
    }
    
    private boolean navesDisponibles(){
        return smallShip >= 10;
    }
    
    public void atacar() throws IOException{
        //obtengo el número de flotas actuales
        getFlotasActuales();
        //leo el fichero, de esta forma es posible actualizarlo en caliente
        readBD();
        //mientras haya rayos, se atraca
        while (isSlotFree() && navesDisponibles()) {
            //compruebo que el índice no haya pasado el número de vacas
            if (index >= cordenadas.size()) {
                index = 0;
            }
            //atracamos
            enviarAtaque();
            //aumento el índice para que la próxima vez lea el siguiente objetivo
            index++;
        }
        setFarmPosition(index);
    }
    
  private void enviarAtaque() throws IOException, NullPointerException{
        //obtener las cordenadas
        Cordenadas buff = cordenadas.get(index);
        //página que te selecciona automáticamente la misión atacar
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=fleet1&galaxy="
        + buff.getGalaxia()+ "&system="+buff.getSistema()+"&position="+buff.getPlaneta()+"&type=1&mission=1");
        //añadir 10 pequeñas de carga
        HtmlInput smallShipInput = (HtmlInput) page.getElementById("ship_202");
        smallShipInput.setValueAttribute(SHIPS_TO_SEND);
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
        //consumo un slot de flotas y 10 naves
        actuales++;
        smallShip-=10;
        Utils.printLog("Ataque enviado a "+buff);
    }
    
    /**
     * Método que lee las cordenadas de las granjas
     */
    private void readBD() {
        ManageDB database = new ManageDB();
        try {
            ResultSet rs = database.ejecutarSQLSelect(SelectSQL.GET_GRANJAS);
            while (rs.next()) {
                cordenadas.add(new Cordenadas(rs.getInt("galaxia"),rs.getInt("sistema"), rs.getInt("planeta")));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }finally{
            database.killConnection();
        }
    }
    
   private void getFlotasActuales(){
        HtmlDivision slots = (HtmlDivision) page.getElementById("slots");
        //obteniendo div de las flotas
        HtmlDivision exp = (HtmlDivision) slots.getElementsByTagName("div").get(0);
        //obtengo el string actual y el máximo
        String[] dummy = exp.asText().split("/");
        actuales = Integer.parseInt(dummy[0].replaceAll("[\\D]", ""));
   }
   
    private int getFarmPosition() {
        try{
        ManageDB database = new ManageDB();
        ResultSet rs = database.ejecutarSQLSelect(SelectSQL.GET_GRANJA_POSITION);
        if(rs.next()){
            return rs.getInt("posicion");
        }else{
            return 0;
        }
        }catch(SQLException ex){
            return 0;
        }
    }
    
    private void setFarmPosition(int index){
        ManageDB database = new ManageDB();
        UpdateSQL update = new UpdateSQL("posiciones", new String[]{"posicion="+index}, new String[]{"id=1"});
        database.ejecutarSQL(update.getSql());
    }
}
