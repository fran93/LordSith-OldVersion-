package es.ogamebot;

import es.ogamebot.utils.Password;
import es.ogamebot.screens.Mensajes;
import es.ogamebot.screens.Planetas;
import es.ogamebot.screens.Login;
import es.ogamebot.screens.RecursosEdificios;
import es.ogamebot.screens.SubirEdificios;
import es.ogamebot.screens.Defensa;
import es.ogamebot.screens.Expediciones;
import es.ogamebot.screens.Ataque;
import es.ogamebot.screens.ImportadorExportador;
import es.ogamebot.screens.Colonias;
import es.ogamebot.utils.ReadDB;
import es.ogamebot.utils.Utils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
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
            //1 minuto para terminar las peticiones
            webClient.setJavaScriptTimeout(60000);
            webClient.getOptions().setTimeout(60000);
            Utils.exitWhenTimeOut();
            //me logueo
            Login log = new Login(webClient, pass.getUser(), pass.getPassword());
            //obtener todos los planetas para realizar tareas en ellos
            Planetas planet = new Planetas(webClient);
            //archivo de propiedades
            ReadDB properties = new ReadDB();
            //Importador, exportador
            ImportadorExportador importExport = new ImportadorExportador(webClient, planet.get(0));
            importExport.comprar();

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
                    Utils.printLog("El planeta " + planet.get(i) + " está siendo atacado por fuerzas hostiles.");
                    Utils.printLog("Activando procolo de defensa.");
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
        } catch (Exception ex) {
            Utils.printLog("Excepción: " + ex);
            ex.printStackTrace();
        }finally{  
            Utils.printLog("Emperador descansando durante 30 minutos.");
            System.exit(0);
        }
    }
}
