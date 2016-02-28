package es.ogamebot.screens;

import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import es.ogamebot.database.ManageDB;
import es.ogamebot.database.SelectSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Fran1488
 */
public class Login {

    final private WebClient webClient;
    private HtmlPage page;

    public Login(WebClient webClient) throws IOException {
        this.webClient = webClient;

        ManageDB database = new ManageDB();
        try {
            ResultSet rs = database.ejecutarSQLSelect(SelectSQL.GET_LOGIN);
            if (rs.next()) {
                page = this.webClient.getPage("http://es.ogame.gameforge.com/");
                //cambio el universo
                HtmlSelect select = (HtmlSelect) page.getElementById("serverLogin");
                HtmlOption option = select.getOptionByValue("s"+Utils.getUniverso()+"-es.ogame.gameforge.com");
                select.setSelectedAttribute(option, true);

                //pongo el usuario
                HtmlInput userInput = (HtmlInput) page.getElementById("usernameLogin");
                userInput.setValueAttribute(rs.getString("user"));
                //pongo la contraseña
                HtmlInput passwordInput = (HtmlInput) page.getElementById("passwordLogin");
                passwordInput.setValueAttribute(rs.getString("password"));

                //le doy al botón de logueo
                HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("loginSubmit");
                page = button.click();

                //compruebo que estoy logueado
                if (page.getUrl().equals(new URL("http://es.ogame.gameforge.com/main/loginError/?kid=&error=2"))) {
                    Utils.printLog("Usuario o contraseña incorrecto.");
                    System.exit(0);
                } else {
                    Utils.printLog("Bienvenido mi emperador, espero sus órdenes.");
                }
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            database.killConnection();
        }

    }

    public void salir() throws IOException {
        page = webClient.getPage("http://s"+Utils.getUniverso()+"-es.ogame.gameforge.com/game/index.php?page=logout");
        page.cleanUp();
        page.remove();
    }
}
