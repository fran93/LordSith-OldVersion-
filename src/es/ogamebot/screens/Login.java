package es.ogamebot.screens;

import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import java.io.IOException;
import java.net.URL;
/**
 *
 * @author Fran1488
 */
public class Login {

    final private WebClient webClient;
    private HtmlPage page;

    public Login(WebClient webClient, String user, String password) throws IOException {
        this.webClient = webClient;
        
        page = this.webClient.getPage("http://es.ogame.gameforge.com/");
         //cambio el universo
        HtmlSelect select = (HtmlSelect) page.getElementById("serverLogin");
        HtmlOption option = select.getOptionByValue("s132-es.ogame.gameforge.com");
        select.setSelectedAttribute(option, true);

        //pongo el usuario
        HtmlInput userInput = (HtmlInput) page.getElementById("usernameLogin");
        userInput.setValueAttribute(user);
        //pongo la contraseña
        HtmlInput passwordInput = (HtmlInput) page.getElementById("passwordLogin");
        passwordInput.setValueAttribute(password);

        //le doy al botón de logueo
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("loginSubmit");
        page = button.click();

        //compruebo que estoy logueado
        if(page.getUrl().equals(new URL("http://es.ogame.gameforge.com/main/loginError/?kid=&error=2"))){
            Utils.printLog("Usuario o contraseña incorrecto.");
            System.exit(0);
        }else{
            Utils.printLog("Bienvenido mi emperador, espero sus órdenes.");
        }
    }
    
    public void salir() throws IOException {
        page = webClient.getPage("http://s132-es.ogame.gameforge.com/game/index.php?page=logout");
        page.cleanUp();
        page.remove();
    }
}
