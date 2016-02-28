package es.ogamebot.database;

/**
 *
 * @author fran
 */
public class SelectSQL {
    public static final String GET_GRANJAS = "select * from granjas order by galaxia, sistema, planeta";
    public static final String GET_PROPERTIES = "select * from properties";
    public static final String GET_GRANJA_POSITION = "select posicion from posiciones where id=1";
    public static final String GET_EXPEDICION_POSITION = "select posicion from posiciones where id=2";
    public static final String GET_LOGIN = "select user, password from cuentas where id=1";
    public static final String GET_UNIVERSO = "select universo from cuentas where id=1";

    private String sql;
    
    public SelectSQL(String tabla, Object[] parameters){
        sql = "select * from "+tabla+" where 1=1 ";
        for(int i=0;i<parameters.length;i++){
            sql+=" AND ";
            sql+=parameters[i];
        }
    }
    
    public String getSql(){
        return sql;
    }
    
}
