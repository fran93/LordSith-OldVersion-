package ogamebot;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Fran1488
 */
public class Utils {

    public static int getBuildingToBuild(double[] recursos, int[] minas, ReadProperties properties) {
        //subo el almacen de metal cuando  la planta de energía solar -10 / 2 sea mayor al nivel del almacén
        if (((minas[3] - 10) / 2) > minas[4]) {
            System.out.println(getHour()+" - Subiendo almacén de metal a nivel " + (minas[4] + 1));
            return 4;
            //el almacén de cristal tendrá un nivel menos que el de metal
        } else if ((minas[4] - 1) > minas[5]) {
            System.out.println(getHour()+" - Subiendo almacén de cristal a nivel " + (minas[5] + 1));
            return 5;
            //el contenedor de deuterio tendrá un nivel menos que el de cristal
        } else if((minas[5] - 1) > minas[6]){  
            System.out.println(getHour()+" - Subiendo contenedor de deuterio a nivel " + (minas[6] + 1));
            return 6;
         //si no hay energía, se sube la planta de energía solar
        } else if (recursos[0] <= 0) {
            if(minas[3]>=properties.getEnergia()){
                System.out.println(getHour()+" - Montando 5 satélites solares");
                return 7;
            }else{
                System.out.println(getHour()+" - Subiendo planta de energía solar a nivel " + (minas[3] + 1));
                return 3;
            }
            //la proporción del sintetizador de deuterio con respecto al cristal depende de properties
        } else if((minas[1] / properties.getDeuterio()) > minas[2]){
            System.out.println(getHour()+" - Subiendo sintetizador de deuterio a nivel " + (minas[2] + 1));
            return 2;
            //Si la mina de metal -2 es menor a la de cristal, subimos cristal
        } else if ((minas[0] - 2) > minas[1]) {
            System.out.println(getHour()+" - Subiendo mina de cristal a nivel " + (minas[1] + 1));
            return 1;
        } else {
            System.out.println(getHour()+" - Subiendo mina de metal a nivel " + (minas[0] + 1));
            return 0;
        }
    }
    
    /**
     * Devuelve la hora actual
     * @return 
     */
    public static String getHour(){
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }
    
    /**
     * Devuelve si hay exceso de cristal
     * @param recursos
     * @return 
     */
    public static boolean CristalOverflow(double[] recursos){
        return recursos[2] > recursos[1];
    }
    
    /**
     * Método que libera la memoria del navegador
     * @param wc 
     */
    public static void freeMemory(WebClient wc) {
        List<WebWindow> windows = wc.getWebWindows();
        for (WebWindow wd : windows) {
            // wd.getThreadManager().interruptAll();
            wd.getJobManager().removeAllJobs();
            wd.getJobManager().shutdown();
        }
        wc.closeAllWindows();
    }
    
    /**
     * Función que sale de la aplicación cuando pasa un tiempo determinado, esto permite que el programa finaliza en caso de que de excepción
     */
    public static void exitWhenTimeOut(){
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(60000 * 30);
                    System.out.println(Utils.getHour()+" - ¡Despierte mi emperador!");
                    System.exit(0);
                } catch (InterruptedException ex) {
                    System.exit(0);
                }
            }
        };
        t1.start();
    }
}
