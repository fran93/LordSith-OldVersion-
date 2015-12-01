package ogamebot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Fran1488
 */
public class ReadProperties {

    private String bigCargo, smallCargo, colonialBigCargo;
    private Double deuterio, colonialResources;
    private int naveExpedicion, energia;

    public ReadProperties() {
        try {
            FileInputStream in = new FileInputStream("ogame.properties");
            Properties propiedades = new Properties();
            propiedades.load(in);
            energia=Integer.parseInt(propiedades.getProperty("energia"));
            deuterio=Double.valueOf(propiedades.getProperty("deuterio"));
            colonialResources=Double.valueOf(propiedades.getProperty("colonialResources"));
            smallCargo=propiedades.getProperty("smallCargo");
            bigCargo=propiedades.getProperty("bigCargo");
            colonialBigCargo=propiedades.getProperty("colonialBigCargo");
            switch (propiedades.getProperty("naveExpedicion")) {
                case "Crucero":
                    naveExpedicion=206;
                    break;
                case "Nave de batalla":
                    naveExpedicion=207;
                    break;
                case "Acorazado":
                    naveExpedicion=215;
                    break;
                case "Destructor":
                    naveExpedicion=213;
                    break;
            }
        } catch (IOException ex) {
            System.out.println("Ha fallado la carga de las propiedades: " + ex.getMessage());
        }
    }
    
    public Double getDeuterio() {
        return deuterio;
    }

    public int getNaveExpedicion() {
        return naveExpedicion;
    }

    public String getBigCargo() {
        return bigCargo;
    }

    public String getSmallCargo() {
        return smallCargo;
    }

    public int getEnergia() {
        return energia;
    }

    public String getColonialBigCargo() {
        return colonialBigCargo;
    }

    public Double getColonialResources() {
        return colonialResources;
    }
}
