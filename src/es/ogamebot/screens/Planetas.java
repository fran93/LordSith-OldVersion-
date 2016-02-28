package es.ogamebot.screens;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Fran1488
 */
public class Planetas {
    final private WebClient webClient;
    private final HtmlPage page;
    private final  ArrayList<String> planets = new ArrayList<>();
    private final  ArrayList<Boolean> underAttack = new ArrayList<>();
    
    public Planetas(WebClient webClient) throws IOException{
        this.webClient=webClient;
        
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=overview");
        //obtener la lista de los planetas
        HtmlDivision planetList = (HtmlDivision) page.getElementById("planetList");
        DomNodeList<HtmlElement> planetElements = planetList.getElementsByTagName("div");
        //recorrer la lista y sacar los ids
        for (HtmlElement div : planetElements) {
            planets.add(div.getId().replaceAll("[\\D]", ""));
            underAttack.add(checkIsUnderAttack(div));
        }
        close();
    }
    
    /**
     * Devuelve el número de planetas
     * @return 
     */
    public int size(){
        return planets.size();
    }
    
    /**
     * Devuelve el id de la posición del elemento pasado por parámetro
     * @param id
     * @return 
     */
    public String get(int id){
        return planets.get(id);
    }
    
    
    public boolean IsUnderAttack(int id){
        return underAttack.get(id);
    }

    /**
     * Comprueba si un planeta está bajo ataque
     * @param div 
     */
    private boolean checkIsUnderAttack(HtmlElement div) {
        boolean buff = false;
        DomNodeList<HtmlElement> anchors = div.getElementsByTagName("a");
        //recorrer todos los enlaces si alguno tiene ¡Ataque! devolver true
        for(int i=0;i<anchors.size() && buff==false;i++){
            if(anchors.get(i).getAttribute("title").equals("¡Ataque!")){
                buff = true;
            }
        } 
        return buff;
    }
    
    private void close(){
       page.cleanUp();
       page.remove();
   }
}
