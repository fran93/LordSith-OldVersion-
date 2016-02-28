package es.ogamebot.utils;

import es.ogamebot.database.ManageDB;
import es.ogamebot.database.SelectSQL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Fran1488
 */
public class ReadDB {

    private String bigCargo, smallCargo, colonialBigCargo;
    private Double deuterio, colonialResources;
    private int naveExpedicion, energia;

    public ReadDB() {
        ManageDB database = new ManageDB();
        try{
            ResultSet rs =database.ejecutarSQLSelect(SelectSQL.GET_PROPERTIES);
            if(rs.next()){
                energia=rs.getInt("planta_energia");
                deuterio=rs.getDouble("sintetizador_deuterio");
                smallCargo=rs.getString("small_cargo");
                bigCargo=rs.getString("big_cargo");
                colonialBigCargo=rs.getString("colonia_big_cargo");
                colonialResources=rs.getDouble("colonial_resources");
            }
            switch (rs.getString("expedicion")) {
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
        }catch(SQLException ex){
            Utils.printLog("Ha fallado la carga de las propiedades: " + ex.getMessage());
        }finally{
            database.killConnection();
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
