package es.ogamebot.screens;

import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author fran
 */
public class ImportadorExportador {
        final private WebClient webClient;
        private HtmlPage page;
        
        public ImportadorExportador(WebClient webClient, String cp) throws IOException{
            this.webClient=webClient;
            //obteniendo los datos
            //se ejecuta solo a las 00:00
            if(new GregorianCalendar().get(Calendar.HOUR_OF_DAY)==0){
                page = this.webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=traderOverview&cp="+cp+"#animation=false&page=traderImportExport");    
            }else{
                page = null;
            }
        }
        
        
        
        /**
         * Método que te permite comprar un objeto en el exportador
            * @throws java.io.IOException
         */
        public void comprar() throws IOException{
            if(page!=null){
            HtmlParagraph gotItem = null;
            //esperar a que cargue la página
            while(gotItem==null){
                try{
                    webClient.waitForBackgroundJavaScript(1000);
                    gotItem =(HtmlParagraph) page.getFirstByXPath("//p[@class='got_item_text']");
                }catch(NullPointerException ex){}
            }
            //comprobar si ya lo hemos comprado     
            if(gotItem.asText().isEmpty()){
                    //obtener el botón para añadir el máximo de deuterio
                    HtmlAnchor max = null;
                    while(max==null){
                        try{
                            webClient.waitForBackgroundJavaScript(1000);
                            max = (HtmlAnchor) page.getFirstByXPath("//a[@class='value-control max js_sliderDeuteriumMax js_valButton tooltipRight js_hideTipOnMobile']");
                        }catch(NullPointerException ex){}
                    }
                    page = max.click();
                    //obtener la cantidad que hemos pagado
                    HtmlInput amount = null;
                    while(amount==null){
                        webClient.waitForBackgroundJavaScript(1000);
                        amount = (HtmlInput) page.getFirstByXPath("//input[@class='resourceAmount js_amount js_sliderDeuteriumInput js_deuterium checkThousandSeparator hideNumberSpin']");
                    }
                    //obtener el botón para pagar
                    HtmlAnchor pay = null;
                    while(pay==null){
                        webClient.waitForBackgroundJavaScript(1000);
                        pay = (HtmlAnchor) page.getFirstByXPath("//a[@class='pay']");
                    }
                    page = pay.click();
                    //tomar el objeto
                    HtmlAnchor take = null;
                    while(take==null){
                        webClient.waitForBackgroundJavaScript(1000);
                        take = (HtmlAnchor) page.getFirstByXPath("//a[@class='bargain import_bargain take']");
                    }
                    page = take.click();
                    webClient.waitForBackgroundJavaScript(2000);
                    //volvemos a obtener el texto del item comprado
                    gotItem =(HtmlParagraph) page.getFirstByXPath("//p[@class='got_item_text']");
                    //imprimimos el resultados
                    Utils.printLog(gotItem.asText()+ " has pagado " + amount.asText()+ " de deuterio."); 
                }else{
                    Utils.printLog("Mi emperador, "+gotItem.asText());
                }
            }
        }
}
