package es.ogamebot.screens;

import es.ogamebot.utils.ReadDB;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import java.io.IOException;

/**
 *
 * @author Fran1488
 */
public class RecursosEdificios {

    final private WebClient webClient;
    private final HtmlPage page;
    private HtmlListItem edificio;
    private HtmlSpan level;
    private final Double colonialMaxResources;
    private final double[] recursos = new double[4];
    private final int[] minas = new int[7];
    private final boolean construyendo;

    public RecursosEdificios(WebClient webClient, String cp, ReadDB properties) throws IOException {
        this.webClient = webClient;
        
        //obteniendo los datos....
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=resources&cp="+cp);
        //obtener nivel mina de metal
        edificio = (HtmlListItem) page.getElementById("button1");
        //convertir el valor en entero
        minas[0] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener nivel mina de cristal
        edificio = (HtmlListItem) page.getElementById("button2");
        //convertir el valor en entero     
        minas[1] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener nivel mina de deuterio
        edificio = (HtmlListItem) page.getElementById("button3");
        //convertir el valor en entero
        minas[2] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", "")); 
        
        //obtener el nivel de la planta de energía solar
        edificio = (HtmlListItem) page.getElementById("button4");
        //convertir el valor en entero
        minas[3] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener el nivel del almacen de metal
        edificio = (HtmlListItem) page.getElementById("button7");
        //convertir el valor en entero
        minas[4] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener el nivel del almacen de cristal
        edificio = (HtmlListItem) page.getElementById("button8");
        //convertir el valor en entero
        minas[5] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener el nivel del contenedor de deuterio
        edificio = (HtmlListItem) page.getElementById("button9");
        //convertir el valor en entero
        minas[6] = Integer.parseInt(edificio.asText().replaceAll("[\\D]", ""));
        
        //obtener energía
        HtmlListItem energia = (HtmlListItem) page.getElementById("energy_box");
        level = (HtmlSpan)energia.getElementsByTagName("span").get(0);
        recursos[0] = Double.parseDouble(level.asText());      
        
        //obtener metal
        HtmlListItem metal = (HtmlListItem) page.getElementById("metal_box");
        level = (HtmlSpan)metal.getElementsByTagName("span").get(0);
        recursos[1] = Double.parseDouble(level.asText().replaceAll("\\.", "")); 
        
        //obtener cristal
        HtmlListItem cristal = (HtmlListItem) page.getElementById("crystal_box");
        level = (HtmlSpan)cristal.getElementsByTagName("span").get(0);
        //prevenir Empty string exception
        recursos[2] = Double.parseDouble(level.asText().replaceAll("\\.", "")); 
        //obtener deuterio
        HtmlListItem deuterio = (HtmlListItem) page.getElementById("deuterium_box");
        level = (HtmlSpan)deuterio.getElementsByTagName("span").get(0);
        //prevenir Empty string exception
        recursos[3] = Double.parseDouble(level.asText().replaceAll("\\.", "")); 
        
        //obtener los recursos de la colonia
        colonialMaxResources=properties.getColonialResources();
        
        //obtener si se está construyendo algo
        construyendo = !page.asText().contains("No hay edificios en construcción");     
        close();
    }

    public double[] getRecursos() {
        return recursos;
    }

    public int[] getMinas() {
        return minas;
    }

    public boolean isConstruyendo() {
        return construyendo;
    }
    
    public boolean sendResources(){
        return recursos[1]+recursos[2]+recursos[3]>colonialMaxResources;
    }
    
    private void close(){
       page.cleanUp();
       page.remove();
   }

}
