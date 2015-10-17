package ogamebot;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
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
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
        //redirecciones permitidas, nada de excepciones en javascript, y css deshabilitado
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setTimeout(60000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //desactivar los warnings
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
        //se piden los datos al usuario
        Console console = System.console();
        String user;
        String password;
        if(console!=null){
            System.out.print("Usuario: ");
            user = console.readLine();
            System.out.print("Contraseña: ");
            password = new String(console.readPassword());
        }else{
            Scanner teclado = new Scanner(System.in);
            System.out.print("Usuario: ");
            user = teclado.nextLine();
            System.out.print("Contraseña: ");
            password = teclado.nextLine();
        }

        while (true) {
            try {
                //me logueo
                Login log = new Login(webClient, user, password);
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
                    if(planet.IsUnderAttack(i)){
                        System.out.println(Utils.getHour()+" - El planeta "+planet.get(i)+" está siendo atacado por fuerzas hostiles.");
                        System.out.println(Utils.getHour()+" - Activando procolo de defensa.");
                        //construyo defensas
                        Defensa defender = new Defensa(webClient, planet.get(i));
                        defender.buildPlasma();
                        defender.buildGauss();
                        //dependiendo de si hay más cristal o metal, construimos unas defensas u otras
                        
                        if(Utils.CristalOverflow(resources.getRecursos())){
                            defender.buildIonico();
                        }else{
                            defender.buildSmallLaser();      
                        }
                        defender.buildLanzamisiles();
                    }
                    //Envío los recursos al planeta principal
                    Colonias colonias = new Colonias(webClient, properties, planet.get(i));
                    if(colonias.isSlotFree() && resources.sendResources() && i!=0){
                        colonias.enviarRecursos();
                    }                  
                }
                
                //ataques y expediciones
                Ataque ata = new Ataque(webClient, properties, planet.get(0));       
                Expediciones exp = new Expediciones(webClient, properties, planet.get(0));
                if (ata.isSlotFree()) {
                    if(exp.isSlotFree()){
                        exp.enviarExpedicion();
                    }
                    ata.atacar();
                }
                //Mensajes
                Mensajes mensaje = new Mensajes(webClient);
                mensaje.BorrarOtrosMensajes();
                mensaje.BorrarMensajesBatalla();
                mensaje.imprimirResultado();    
                //me deslogueo
                log.salir();
                //liberar la memoria
                webClient.closeAllWindows();
                //esperar 30 minutos
                System.out.println(Utils.getHour()+ " - Emperador descansando durante 30 minutos.");
                Thread.sleep(60000 * 30);
            } catch (ArrayIndexOutOfBoundsException | IOException | InterruptedException | NullPointerException | 
                    NumberFormatException | ClassCastException ex) {
                System.out.println(Utils.getHour() + " - Excepción: " + ex);
                ex.printStackTrace();
            }
        }
    }

}
