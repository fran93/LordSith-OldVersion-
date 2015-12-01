package ogamebot;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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
    
    public Ataque(WebClient webClient, ReadProperties properties, String cp) throws IOException{
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
        readFile();
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
        System.out.println(Utils.getHour() + " - Ataque enviado a "+buff);
    }
    
    /**
     * Método que lee las cordenadas de las granjas
     */
    private void readFile() {
        try (BufferedReader bf = new BufferedReader(new FileReader("granjas.txt"))) {
            while (bf.ready()) {
                String[] split = bf.readLine().split(":");
                cordenadas.add(new Cordenadas(Integer.parseInt(split[0]), 
                Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
   
   public void close(){
       page.cleanUp();
       page.remove();
   }

    private int getFarmPosition() {
        try{
            FileInputStream in = new FileInputStream("ataque.properties");
            Properties propiedades = new Properties();
            propiedades.load(in);
            return Integer.parseInt(propiedades.getProperty("farmPosition"));
        }catch(IOException ex){
            return 0;
        }
    }
    
    private void setFarmPosition(int index){
        try {
            FileOutputStream out = new FileOutputStream("ataque.properties");
            Properties propiedades = new Properties();
            propiedades.setProperty("farmPosition", String.valueOf(index));
            propiedades.store(out, null);          
        } catch (IOException ex) {
            System.out.println(Utils.getHour()+" - Fallo guardando la posición de la granja.");
        }
    }
}
