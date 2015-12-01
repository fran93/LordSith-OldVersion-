package ogamebot;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Fran1488
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //desactivar los warnings
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Password pass = new Password();
        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38)) {
            //redirecciones permitidas, nada de excepciones en javascript, y css deshabilitado
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setCssEnabled(false);
            //30 segundos para terminar las peticiones
            webClient.setJavaScriptTimeout(30000);
            webClient.getOptions().setTimeout(30000);
            Utils.exitWhenTimeOut();
            //me logueo
            Login log = new Login(webClient, pass.getUser(), pass.getPassword());
            //obtener todos los planetas para realizar tareas en ellos
            Planetas planet = new Planetas(webClient);
            //archivo de propiedades
            ReadProperties properties = new ReadProperties();
            //recorrer los planetas
            for (int i = 0; i < planet.size(); i++) {
                //obtengo los edificos y los recursos disponibles
                RecursosEdificios resources = new RecursosEdificios(webClient, planet.get(i), properties);
                //compruebo que la cola esté libre
                if (!resources.isConstruyendo()) {
                    double[] recursos = resources.getRecursos();
                    int[] minas = resources.getMinas();
                    //se sube la mina
                    new SubirEdificios(webClient, Utils.getBuildingToBuild(recursos, minas, properties), planet.get(i)).subir();
                }
                //compruebo que el planeta esté siendo atacado
                if (planet.IsUnderAttack(i)) {
                    System.out.println(Utils.getHour() + " - El planeta " + planet.get(i) + " está siendo atacado por fuerzas hostiles.");
                    System.out.println(Utils.getHour() + " - Activando procolo de defensa.");
                    if(i==0){
                        //en el principal construyo 1000 peques
                        Defensa defender = new Defensa(webClient, planet.get(i));
                        defender.buildPlasma();
                        defender.buildGauss();
                        defender.close();
                    }else{
                        //en las colonias mando los recursos al principal
                        Colonias colonias = new Colonias(webClient, properties, planet.get(i));
                        colonias.enviarRecursos();
                    }
                }
                //Envío los recursos al planeta principal
                Colonias colonias = new Colonias(webClient, properties, planet.get(i));
                if (colonias.isSlotFree() && resources.sendResources() && i != 0) {
                    colonias.enviarRecursos();
                }
                colonias.close();
            }
            //ataques y expediciones
            Ataque ata = new Ataque(webClient, properties, planet.get(0));
            Expediciones exp = new Expediciones(webClient, properties, planet.get(0));
            if (ata.isSlotFree()) {
                if (exp.isSlotFree()) {
                    exp.enviarExpedicion();
                }
                ata.atacar();
            }
            exp.close();
            ata.close();
            //Mensajes
            Mensajes mensaje = new Mensajes(webClient);
            mensaje.BorrarOtrosMensajes();
            mensaje.BorrarMensajesBatalla();
            mensaje.imprimirResultado();
            mensaje.close();
            //me deslogueo
            log.salir();
            //liberar memoria
            Utils.freeMemory(webClient);
            //esperar 30 minutos      
        } catch (ArrayIndexOutOfBoundsException | IOException | InterruptedException | NullPointerException |
                NumberFormatException | ClassCastException ex) {
            System.out.println(Utils.getHour() + " - Excepción: " + ex);
            ex.printStackTrace();
        }finally{  
            System.out.println(Utils.getHour() + " - Emperador descansando durante 30 minutos.");
            System.exit(0);
        }
    }
}
