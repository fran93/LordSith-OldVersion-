package es.ogamebot.database;

/**
 *
 * @author fran
 */
public class UpdateSQL {
    private String sql;
    
    public UpdateSQL(String tabla, Object[]set, Object[] parameters){
       sql = "UPDATE "+tabla+" SET ";
       for(int i=0;i<set.length;i++){
           if(i>0){
                sql+=",";
            }
           sql+=set[i];
       }
       sql += " WHERE 1=1 ";
        for(int i=0;i<parameters.length;i++){
            sql+=" AND ";
            sql+=parameters[i];
        }
    }
    
    public String getSql(){
        return sql;
    }
}
