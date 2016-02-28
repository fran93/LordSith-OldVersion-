package es.ogamebot.utils;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Fran
 */
public class Password {
   String user, password;
   boolean newuser=false;
   
   public Password(){
       if(!getUserFromProperties()){
           inputPassword();
       }
       setUserFromProperties();     
   }
    
    private void inputPassword(){
        Console console = System.console();
        if (console != null) {
            System.out.print("Usuario: ");
            user = console.readLine();
            System.out.print("Contraseña: ");
            password = new String(console.readPassword());
        } else {
            Scanner teclado = new Scanner(System.in);
            System.out.print("Usuario: ");
            user = teclado.nextLine();
            System.out.print("Contraseña: ");
            password = teclado.nextLine();
        }
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    } 
    
    
    private boolean getUserFromProperties() {
        try{
            FileInputStream in = new FileInputStream("ww.properties");
            Properties propiedades = new Properties();
            propiedades.load(in);
            user = propiedades.getProperty("user");
            password = propiedades.getProperty("password");
            return true;
        }catch(IOException ex){
            return false;
        }
        
    }
    
    private void setUserFromProperties(){
        try {
            FileOutputStream out = new FileOutputStream("ww.properties");
            Properties propiedades = new Properties();
            propiedades.setProperty("user", user);
            propiedades.setProperty("password", password);
            propiedades.store(out, null);          
        } catch (IOException ex) {
            Utils.printLog("Fallo guardando el usuario y la contraseña.");
        }
    }
}


