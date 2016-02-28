package es.ogamebot.screens;

import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import java.io.IOException;

/**
 *
 * @author Fran1488
 */
public class Mensajes {
    private int cantidad;
    private final WebClient webClient;
    private HtmlPage page;
    
    public Mensajes(WebClient webClient) throws IOException{
        cantidad=0;
        this.webClient = webClient;
    }
    /**
     * Se borran los otros mensajes
     * @throws IOException 
     */
    public void BorrarOtrosMensajes() throws IOException, IndexOutOfBoundsException{
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=messages");
        HtmlAnchor others = (HtmlAnchor)page.getElementById("ui-id-21");
        //esperar a que el elemento cargue
        while(others==null){
            webClient.waitForBackgroundJavaScript(2000);
            others = (HtmlAnchor)page.getElementById("ui-id-21");
        }
        page = others.click();
        //obtenemos el listado de los mensajes
        HtmlDivision othersMessages = (HtmlDivision)page.getElementById("ui-id-22");
        HtmlUnorderedList otherUl = (HtmlUnorderedList)othersMessages.getFirstChild();
        //esperar a que el elemento cargue
        while(otherUl==null){
            webClient.waitForBackgroundJavaScript(2000);
            otherUl = (HtmlUnorderedList)othersMessages.getFirstChild();
        }
        DomNodeList<HtmlElement> messages = otherUl.getElementsByTagName("li");
        boolean hayMensajes = true;
        Utils.printLog("Revisando otros mensajes...");
        for (int i=5;i<messages.getLength()-5 && hayMensajes;i++) {
            hayMensajes = !messages.get(5).asText().equals("Actualmente no hay mensajes en esta pestaña");
            if(hayMensajes){
                HtmlSpan subject = (HtmlSpan) messages.get(i).getElementsByTagName("span").get(0);
                if (subject.asText().equals("Retorno de una flota") || 
                    subject.asText().equals("Llegada a un planeta")) {
                    //controlar error index of bound exception
                    if(i<messages.size()){
                        int buff = i -5;
                        ScriptResult result = page.executeJavaScript("$('#ui-id-22 .msg:eq("+buff+")').find('.msg_head').find('span:eq(1)').click();");
                        page = (HtmlPage) result.getNewPage();
                        cantidad++;
                        //resetear el bucle
                        i=5;
                        messages = otherUl.getElementsByTagName("li"); 
                    }
                }
            }
        }   
    }
    /**
     * Se borran las batallas que tengan 0 pérdidas.
     * @throws IOException 
     */
    public void BorrarMensajesBatalla() throws IOException, IndexOutOfBoundsException{
        page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=messages");
        HtmlAnchor battles = (HtmlAnchor)page.getElementById("ui-id-15");
        //esperar a que el elemento cargue
        while(battles==null){
            webClient.waitForBackgroundJavaScript(2000);
            battles = (HtmlAnchor)page.getElementById("ui-id-15");
        }
        page = battles.click();
        //obtenemos el listado de los mensajes
        HtmlDivision battlesMessages = (HtmlDivision)page.getElementById("ui-id-16");
        HtmlUnorderedList battleUl = (HtmlUnorderedList)battlesMessages.getFirstChild();
        //esperar a que el elemento cargue
        while(battleUl==null){
            webClient.waitForBackgroundJavaScript(2000);
            battleUl = (HtmlUnorderedList)battlesMessages.getFirstChild();
        }
        DomNodeList<HtmlElement> messages = battleUl.getElementsByTagName("li");
        boolean hayMensajes = true;
        Utils.printLog("Revisando informes de batalla...");
        for (int i=5;i<messages.getLength()-5 && hayMensajes;i++) {
            hayMensajes = !messages.get(5).asText().equals("Actualmente no hay mensajes en esta pestaña");
            if(hayMensajes){
                    //controlar index of bound exception
                    while(messages.get(i).getElementsByTagName("span").isEmpty()){
                        webClient.waitForBackgroundJavaScript(2000);
                    }
                    HtmlSpan content = (HtmlSpan)messages.get(i).getElementsByTagName("span").get(6);   
                    //controlar index of bound exception
                    while(content.getElementsByTagName("div").isEmpty()){
                        webClient.waitForBackgroundJavaScript(2000);
                    }
                    HtmlDivision left = (HtmlDivision)content.getElementsByTagName("div").get(0);
                    HtmlDivision right = (HtmlDivision) content.getElementsByTagName("div").get(1);
                    HtmlSpan spanLeft = (HtmlSpan)left.getElementsByTagName("span").get(0);
                    HtmlSpan spanRight = (HtmlSpan)right.getElementsByTagName("span").get(0);
                    int atacante = Integer.parseInt(spanLeft.asText().replaceAll("[\\D]", ""));
                    int defensor = Integer.parseInt(spanRight.asText().replaceAll("[\\D]", ""));
                if (atacante == 0 && defensor == 0) {
                    //controlar error index of bound exception
                    if(i<messages.size()){
                        int buff = i -5;
                        ScriptResult result = page.executeJavaScript("$('#ui-id-16 .msg:eq("+buff+")').find('.msg_head').find('span:eq(2)').click();");
                        page = (HtmlPage) result.getNewPage();
                        cantidad++;
                        //resetear el bucle
                        i=5;
                        messages = battleUl.getElementsByTagName("li");  
                    }
                }
            }
        }
    }
    
    
    public void imprimirResultado(){
        if(cantidad>0){
            Utils.printLog("Mi emperador, se han descartado "+cantidad+" mensajes que no eran importantes.");
        }
    }
    
    /**
     * Cierra la página del navegador
     */
    public void close(){
        page.cleanUp();
        page.remove();
    }
}
