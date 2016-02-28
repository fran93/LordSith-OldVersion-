package es.ogamebot.utils;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *
 * @author fran
 */
public class Loading {

    public static final int TYPE_ATTACK = 0;
    public static final int TYPE_EXPEDITION = 1;

    /**
     * Espera que a que la página cargue
     *
     * @param webClient
     * @param page
     * @param type
     */
    public static void wait(WebClient webClient, HtmlPage page, int type) {
        HtmlDivision planet;
        String text = "";
        String previous;
        ScriptResult result;
        int i;
        switch (type) {
            case 0:
                i = 5;
                //LLegar a la primera pantalla
                while (!text.startsWith("Envío de flota III -")){
                    planet = null;
                    while (planet == null) {
                        webClient.waitForBackgroundJavaScript(i*1000);
                        planet = (HtmlDivision) page.getElementById("planet");
                    }
                    webClient.waitForBackgroundJavaScript(i*1000);
                    result = page.executeJavaScript("trySubmit();");
                    page = (HtmlPage) result.getNewPage();   
                    text = planet.getElementsByTagName("h2").get(0).getTextContent();
                    System.out.println("0 - "+text+" - "+i*1000);
                    //si se queda atascado en alguna pantalla, incrementos el tiempo de espera
                    previous = text;
                    if(previous.equals(text)){
                        i++;
                    }
                };
                //LLegar a la primera pantalla
                while (!text.startsWith("Envío de flota I -")){
                    planet = null;
                    while (planet == null) {
                        webClient.waitForBackgroundJavaScript(i*1000);
                        planet = (HtmlDivision) page.getElementById("planet");
                    }
                    webClient.waitForBackgroundJavaScript(i*1000);
                    result = page.executeJavaScript("trySubmit();");
                    page = (HtmlPage) result.getNewPage();
                    text = planet.getElementsByTagName("h2").get(0).getTextContent();
                    previous = text;
                    System.out.println("0 - "+text+" - "+i*1000);
                    if(previous.equals(text)){
                        i++;
                    }
                };
                break;
            case 1:
                i = 5;
                //LLegar a la primera pantalla
                while (!text.startsWith("Envío de flota I -")){
                    planet = null;
                    while (planet == null) {
                        webClient.waitForBackgroundJavaScript(i*1000);
                        planet = (HtmlDivision) page.getElementById("planet");
                    }
                    result = page.executeJavaScript("trySubmit();");
                    page = (HtmlPage) result.getNewPage();
                    text = planet.getElementsByTagName("h2").get(0).getTextContent();
                    previous = text;
                    if(previous.equals(text)){
                        i++;
                    }
                };
                break;
        }

    }
}
