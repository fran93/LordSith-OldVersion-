package es.ogamebot.database;

/**
 *
 * @author fran
 */
public class InsertSQL {
    private String sql;
    
    public InsertSQL(String tabla, Object[] parameters){
        sql = "insert into "+tabla+" values( ";
        for(int i=0;i<parameters.length;i++){
            if(i>0){
                sql+=",";
            }
            sql+=parameters[i];
        }
        sql +=");";
    }
    
    public String getSql(){
        return sql;
    }
}
